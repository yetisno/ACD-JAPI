package org.yetiz.lib.acd;

import com.google.gson.JsonObject;
import com.ning.http.client.*;
import org.yetiz.lib.acd.Entity.Endpoint;
import org.yetiz.lib.acd.api.Account;
import org.yetiz.lib.acd.exception.ACDResponseException;
import org.yetiz.lib.acd.exception.AuthorizationRequiredException;
import org.yetiz.lib.acd.exception.BadParameterException;
import org.yetiz.lib.acd.exception.InvalidAuthTokenException;
import org.yetiz.lib.utils.Log;

import java.util.Calendar;

/**
 * Created by yeti on 2015/4/13.
 */
public class ACDSession {
	private AsyncHttpClient asyncHttpClient = null;
	private ACDToken acdToken = null;
	private Configure configure = null;
	private String contentUrl = "";
	private String metadataUrl = "https://drive.amazonaws.com/drive/v1/";

	private ACDSession() {
		AsyncHttpClientConfig asyncHttpClientConfig = new AsyncHttpClientConfig.Builder()
			.setFollowRedirect(false)
			.build();
		this.asyncHttpClient = new AsyncHttpClient(asyncHttpClientConfig);
	}


	protected static ACDSession getACDSessionByCode(Configure configure, String code) {
		Log.v("getACDSession");
		ACDSession acdSession = new ACDSession();
		acdSession.configure = configure;
		Response response;
		try {
			String body = "grant_type=authorization_code" +
				"&code=" + Utils.urlEncode(code) +
				"&client_id=" + Utils.urlEncode(configure.getClientId()) +
				"&client_secret=" + Utils.urlEncode(configure.getClientSecret()) +
				"&redirect_uri=" + Utils.urlEncode(configure.getRedirectUri());
			response = acdSession.asyncHttpClient.preparePost("https://api.amazon.com/auth/o2/token")
				.addHeader("Content-Type", Utils.getContentType())
				.setBody(body).execute().get();
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
		int statusCode = response.getStatusCode();
		if (statusCode == 400) {
			throw new BadParameterException(response);
		} else if (statusCode == 200) {
			JsonObject object = Utils.convertBodyToJson(response);
			Integer expires_in = object.get("expires_in").getAsInt();
			String token_type = object.get("token_type").getAsString();
			String refresh_token = object.get("refresh_token").getAsString();
			String access_token = object.get("access_token").getAsString();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, expires_in);
			acdSession.acdToken = new ACDToken(
				token_type,
				calendar.getTime(),
				refresh_token,
				access_token);
			acdSession.refreshEndpoint();
			acdSession.updateTokenOfConfigureFile();
		}
		return acdSession;
	}

	protected static ACDSession getACDSessionByToken(Configure configure, ACDToken acdToken) {
		Log.v("getACDSession");
		ACDSession acdSession = new ACDSession();
		acdSession.configure = configure;
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
		Log.d("Refresh Token.");
		try {
			String body = "grant_type=refresh_token" +
				"&refresh_token=" + Utils.urlEncode(acdToken.getRefreshToken()) +
				"&client_id=" + Utils.urlEncode(configure.getClientId()) +
				"&client_secret=" + Utils.urlEncode(configure.getClientSecret());
			response = asyncHttpClient.preparePost("https://api.amazon.com/auth/o2/token")
				.addHeader("Content-Type", Utils.getContentType())
				.setBody(body).execute().get();
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
		int statusCode = response.getStatusCode();
		if (statusCode == 400) {
			throw new AuthorizationRequiredException(configure.getClientId(), configure.getRedirectUri(),
				configure.isWritable());
		} else if (statusCode == 200) {
			JsonObject object = Utils.convertBodyToJson(response);
			Integer expires_in = object.get("expires_in").getAsInt();
			String token_type = object.get("token_type").getAsString();
			String refresh_token = object.get("refresh_token").getAsString();
			String access_token = object.get("access_token").getAsString();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, expires_in);
			acdToken = new ACDToken(
				token_type,
				calendar.getTime(),
				refresh_token,
				access_token);
			updateTokenOfConfigureFile();
			refreshEndpoint();
		} else
			throw new ACDResponseException(response);
	}

	public ACDToken getToken() {
		return acdToken;
	}

	/**
	 * Fire by request and return checked response, if the token is expired, it'll renew token and re-fire request.
	 *
	 * @param request
	 * @return the response of request
	 */
	public Response execute(Request request) {
		Log.v("Execute Request");
		if (acdToken.isExpire())
			refreshToken();
		try {
			request = setAuthHeader(request);
			long time = System.currentTimeMillis();
			Response response = asyncHttpClient.executeRequest(request).get();
			ACDResponseChecker.check(response);
			Log.d("Execute Time", new Long(System.currentTimeMillis() - time).toString() + "ms");
			return response;
		} catch (InvalidAuthTokenException e) {
			if (configure.isAutoRefresh()) {
				Log.d("Auto Refresh Token");
				refreshToken();
				return execute(request);
			}
			throw e;
		} catch (ACDResponseException e) {
			throw e;
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
	}

	private Request setAuthHeader(Request request) {
		return new RequestBuilder(request).setHeader("Authorization", acdToken.getAuthorizationString()).build();
	}

	private void updateTokenOfConfigureFile() {
		if (!configure.isAutoConfigureUpdate())
			return;
		configure.setTokenType(acdToken.getTokenType());
		configure.setAccessToken(acdToken.getAccessToken());
		configure.setRefreshToken(acdToken.getRefreshToken());
		configure.save();
	}

	private void refreshEndpoint() {
		Endpoint endpoint = Account.getEndpoint(this);
		contentUrl = endpoint.getContentUrl();
		metadataUrl = endpoint.getMetadataUrl();
	}

	public String getContentUrl() {
		return contentUrl;
	}

	/**
	 * combine contentUrl, resourceUrl and return
	 *
	 * @param resourceUrl
	 * @return
	 */
	public String getContentUrl(String resourceUrl) {
		return getContentUrl() + resourceUrl;
	}

	public String getMetadataUrl() {
		return metadataUrl;
	}

	/**
	 * combine metadataUrl, resourceUrl and return
	 *
	 * @param resourceUrl
	 * @return
	 */
	public String getMetadataUrl(String resourceUrl) {
		return getMetadataUrl() + resourceUrl;
	}

	public void close() {
		destroy();
	}

	public void destroy() {
		asyncHttpClient.close();
	}

	public Configure getConfigure() {
		return configure;
	}

	public void setConfigure(Configure configure) {
		this.configure = configure;
	}

}
