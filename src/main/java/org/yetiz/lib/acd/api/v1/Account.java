package org.yetiz.lib.acd.api.v1;

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
	private static String root = "account";

	public static Endpoint getEndpoint(ACDSession acdSession) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringFormatter("{}/endpoint", root);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		Endpoint endpoint = Utils.getGson().fromJson(Utils.getResponseBody(response), Endpoint.class);
		return endpoint;
	}

	public static AccountInfo getAccountInfo(ACDSession acdSession) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringFormatter("{}/info", root);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		AccountInfo accountInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), AccountInfo.class);
		return accountInfo;
	}

	public static AccountQuota getAccountQuota(ACDSession acdSession) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringFormatter("{}/quota", root);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		AccountQuota accountQuota = Utils.getGson().fromJson(Utils.getResponseBody(response), AccountQuota.class);
		return accountQuota;
	}

	public static AccountUsage getAccountUsage(ACDSession acdSession) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringFormatter("{}/usage", root);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		AccountUsage accountUsage = Utils.getGson().fromJson(Utils.getResponseBody(response), AccountUsage.class);
		return accountUsage;
	}
}
