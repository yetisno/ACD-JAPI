package org.yetiz.lib.acd;

import com.google.gson.JsonObject;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.yetiz.lib.acd.Entity.Endpoint;
import org.yetiz.lib.acd.api.v1.Account;
import org.yetiz.lib.acd.exception.ACDResponseException;
import org.yetiz.lib.acd.exception.AuthorizationRequiredException;
import org.yetiz.lib.acd.exception.BadParameterException;
import org.yetiz.lib.acd.exception.InvalidAuthTokenException;
import org.yetiz.lib.utils.Log;

import java.io.IOException;
import java.io.UncheckedIOException;
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
		AsyncHttpClientConfig asyncHttpClientConfig = new DefaultAsyncHttpClientConfig.Builder()
			.setFollowRedirect(false)
			.build();
		this.asyncHttpClient = new DefaultAsyncHttpClient(asyncHttpClientConfig);
	}


	/**
	 * Constructor that allows timeouts to be configured. Large files overstay default timeout and connection will terminate
	 *
	 * @param timeoutMs
	 */
	private ACDSession(int timeoutMs) {
		Log.d("Timeout set to " + timeoutMs);
		AsyncHttpClientConfig asyncHttpClientConfig = new DefaultAsyncHttpClientConfig.Builder()
			.setFollowRedirect(false)
			.setConnectTimeout(timeoutMs)
			.setReadTimeout(timeoutMs)
			.setRequestTimeout(timeoutMs)
			.setPooledConnectionIdleTimeout(timeoutMs)
			.build();
		this.asyncHttpClient = new DefaultAsyncHttpClient(asyncHttpClientConfig);
	}

	public static ACDSession getACDSessionByCode(Configure configure, String code, Integer timeout) {
		Log.d(Utils.getCurrentMethodName());
		if (code == null || code.isEmpty()) {
			throw new AuthorizationRequiredException(configure.getClientId(), configure.getRedirectUri(), configure
				.isWritable());
		}
		ACDSession acdSession = newAcdSession(timeout);
		acdSession.configure = configure;
		Response response;
		try {
			String body = "grant_type=authorization_code" +
				"&code=" + Utils.urlEncode(code) +
				"&client_id=" + Utils.urlEncode(configure.getClientId()) +
				"&client_secret=" + Utils.urlEncode(configure.getClientSecret()) +
				"&redirect_uri=" + Utils.urlEncode(configure.getRedirectUri());
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
			int expires_in = object.get("expires_in").getAsInt();
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

	private static ACDSession newAcdSession(Integer timeout) {
		ACDSession acdSession = null;
		if (timeout != null) {
			acdSession = new ACDSession(timeout);
		} else {
			acdSession = new ACDSession();
		}
		return acdSession;
	}

	public static ACDSession getACDSessionByCode(Configure configure, String code) {
		return getACDSessionByCode(configure, code, null);
	}

	public static ACDSession getACDSessionByToken(Configure configure, ACDToken acdToken, Integer timeout) {
		Log.d(Utils.getCurrentMethodName());
		if (acdToken == null) {
			throw new NullPointerException("acdToken is not nullable.");
		}
		ACDSession acdSession = newAcdSession(timeout);
		acdSession.configure = configure;
		acdSession.acdToken = acdToken;
		if (acdToken.isExpire()) {
			acdSession.refreshToken();
		} else {
			acdSession.refreshEndpoint();
		}
		return acdSession;
	}

	public static ACDSession getACDSessionByToken(Configure configure, ACDToken acdToken) {
		return getACDSessionByToken(configure, acdToken, null);
	}

	public void refreshToken() {
		Response response;
		Log.d(Utils.getCurrentMethodName());
		try {
			String body = "grant_type=refresh_token" +
				"&refresh_token=" + Utils.urlEncode(acdToken.getRefreshToken()) +
				"&client_id=" + Utils.urlEncode(configure.getClientId()) +
				"&client_secret=" + Utils.urlEncode(configure.getClientSecret());
			response = asyncHttpClient.preparePost("https://api.amazon.com/auth/o2/token")
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
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
		Log.d(Utils.getCurrentMethodName());
		if (acdToken.isExpire())
			refreshToken();
		try {
			request = setAuthHeader(request);
			long time = System.currentTimeMillis();
			Response response = asyncHttpClient.executeRequest(request).get();
			ResponseCode.check(response);
			Log.d("Execute Time", (System.currentTimeMillis() - time) + "ms");
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
        if (configure.hasRefresher()) {
            configure.refresh(acdToken.getRefreshToken());
        }
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
		try {
			asyncHttpClient.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public Configure getConfigure() {
		return configure;
	}

	public void setConfigure(Configure configure) {
		this.configure = configure;
	}
}
