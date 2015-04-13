package org.yetiz.lib.acd.api;

import com.google.gson.Gson;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.Entity.Endpoint;
import org.yetiz.lib.acd.Utils;

import java.io.IOException;

/**
 * Created by yeti on 2015/4/13.
 */
public class Account {
	public static Endpoint getEndpoint(ACDSession acdSession) {
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl() + "account/endpoint")
			.setMethod("GET")
			.addHeader("Authorization", acdSession.getToken().getAuthorizationString())
			.build());
		Endpoint endpoint = null;
		if (response.getStatusCode() == 200) {
			try {
				endpoint = new Gson().fromJson(response.getResponseBody(Utils.getCharset()), Endpoint.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return endpoint;
	}
}
