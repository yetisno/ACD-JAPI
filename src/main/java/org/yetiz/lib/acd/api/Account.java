package org.yetiz.lib.acd.api;

import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.Entity.AccountInfo;
import org.yetiz.lib.acd.Entity.AccountQuota;
import org.yetiz.lib.acd.Entity.AccountUsage;
import org.yetiz.lib.acd.Entity.Endpoint;
import org.yetiz.lib.acd.Utils;
import org.yetiz.lib.utils.Log;

/**
 * Created by yeti on 2015/4/13.
 */
public class Account {

	public static Endpoint getEndpoint(ACDSession acdSession) {
		Log.v("GetEndpoint");
		String resourceEndpoint = "account/endpoint";
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.addHeader("Authorization", acdSession.getToken().getAuthorizationString())
			.build());
		Endpoint endpoint = Utils.getGson().fromJson(Utils.getResponseBody(response), Endpoint.class);
		return endpoint;
	}

	public static AccountInfo getAccountInfo(ACDSession acdSession) {
		Log.v("getAccountInfo");
		String resourceEndpoint = "account/info";
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.addHeader("Authorization", acdSession.getToken().getAuthorizationString())
			.build());
		AccountInfo accountInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), AccountInfo.class);
		return accountInfo;
	}

	public static AccountQuota getAccountQuota(ACDSession acdSession){
		Log.v("getAccountQuota");
		String resourceEndpoint = "account/quota";
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.addHeader("Authorization", acdSession.getToken().getAuthorizationString())
			.build());
		AccountQuota accountQuota = Utils.getGson().fromJson(Utils.getResponseBody(response), AccountQuota.class);
		return accountQuota;
	}

	public static AccountUsage getAccountUsage(ACDSession acdSession){
		Log.v("getAccountUsage");
		String resourceEndpoint = "account/usage";
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.addHeader("Authorization", acdSession.getToken().getAuthorizationString())
			.build());
		AccountUsage accountUsage = Utils.getGson().fromJson(Utils.getResponseBody(response), AccountUsage.class);
		return accountUsage;
	}
}
