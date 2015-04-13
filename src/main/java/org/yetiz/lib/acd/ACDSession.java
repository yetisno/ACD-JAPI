package org.yetiz.lib.acd;

import com.google.gson.JsonObject;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Request;
import com.ning.http.client.Response;
import com.ning.http.client.multipart.StringPart;
import org.yetiz.lib.acd.Entity.Endpoint;
import org.yetiz.lib.acd.api.Account;
import org.yetiz.lib.acd.exception.BadParameterException;

import java.util.Calendar;

/**
 * Created by yeti on 2015/4/13.
 */
public class ACDSession {
	private AsyncHttpClient asyncHttpClient = null;
	private boolean autoRefresh = true;
	private String client_id = "";
	private String client_secret = "";
	private ACDToken acdToken = null;
	private String contentUrl = "";
	private String metadataUrl = "https://drive.amazonaws.com/drive/v1/";

	private ACDSession() {
		AsyncHttpClientConfig asyncHttpClientConfig = new AsyncHttpClientConfig.Builder()
			.setFollowRedirect(false)
			.build();
		this.asyncHttpClient = new AsyncHttpClient(asyncHttpClientConfig);
	}


	protected static ACDSession getACDSessionByCode(String client_id, String client_secret, String code, String
		redirectUri) {
		ACDSession acdSession = new ACDSession();
		acdSession.client_id = client_id;
		acdSession.client_secret = client_secret;
		Response response;
		try {
			String body = "grant_type=authorization_code" +
				"&code=" + Utils.urlEncode(code) +
				"&client_id=" + Utils.urlEncode(client_id) +
				"&client_secret=" + Utils.urlEncode(client_secret) +
				"&redirect_uri=" + redirectUri;
			response = acdSession.asyncHttpClient.preparePost("https://api.amazon.com/auth/o2/token")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.setBody(body).execute().get();
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
		int statusCode = response.getStatusCode();
		if (statusCode == 400) {
			throw new BadParameterException(response);
		} else if (statusCode == 200) {
			JsonObject object = Utils.convertBodyToJson(response);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, object.get("expires_in").getAsInt());
			acdSession.acdToken = new ACDToken(
				object.get("token_type").getAsString(),
				calendar.getTime(),
				object.get("refresh_token").getAsString(),
				object.get("access_token").getAsString());
			acdSession.refreshEndpoint();
		}
		return acdSession;
	}

	protected static ACDSession getACDSessionByToken(String client_id, String client_secret, ACDToken acdToken) {
		ACDSession acdSession = new ACDSession();
		acdSession.client_id = client_id;
		acdSession.client_secret = client_secret;
		acdSession.acdToken = acdToken;
		if (acdToken.isExpire()) {
			acdSession.refreshToken();
		} else {
			acdSession.refreshEndpoint();
		}
		return acdSession;
	}

	private void refreshToken() {
		Response response;
		try {
			response = asyncHttpClient.preparePost("https://api.amazon.com/auth/o2/token")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.addBodyPart(new StringPart("grant_type", "refresh_token"))
				.addBodyPart(new StringPart("refresh_token", acdToken.getRefreshToken()))
				.addBodyPart(new StringPart("client_id", client_id))
				.addBodyPart(new StringPart("client_secret", client_secret)).execute().get();
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
		int statusCode = response.getStatusCode();
		if (statusCode == 400) {
			throw new BadParameterException(response);
		} else if (statusCode == 200) {
			JsonObject object = Utils.convertBodyToJson(response);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, object.get("expires_in").getAsInt());
			acdToken = new ACDToken(
				object.get("token_type").getAsString(),
				calendar.getTime(),
				object.get("refresh_token").getAsString(),
				object.get("access_token").getAsString());
			refreshEndpoint();
		}
	}

	public ACDToken getToken() {
		return acdToken;
	}

	public Response execute(Request request) {
		if (acdToken.isExpire())
			refreshToken();
		try {
			return asyncHttpClient.executeRequest(request).get();
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
	}

	private void refreshEndpoint() {
		Endpoint endpoint = Account.getEndpoint(this);
		contentUrl = endpoint.getContentUrl();
		metadataUrl = endpoint.getMetadataUrl();
//		200 : OK.
//		400 : Bad input parameter. Error message should indicate which one and why.
//		401 : The client passed in the invalid Auth token, Client should refresh the token and then try again.
//		403 : Forbidden
//		500 : Servers are not working as expected. The request is probably valid but needs to be requested again later.
//		503 : Service Unavailable.
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public String getMetadataUrl() {
		return metadataUrl;
	}

	public void close() {
		destroy();
	}

	public void destroy() {
		asyncHttpClient.close();
	}
}
