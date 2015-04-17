package org.yetiz.lib.acd.api.v1;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.Entity.ChangesInfo;
import org.yetiz.lib.acd.Utils;
import org.yetiz.lib.utils.Log;

/**
 * Created by yeti on 2015/4/13.
 */
public class Changes {
	private static String root = "changes";

	public static ChangesInfo getChanges(ACDSession acdSession, String checkpoint) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = root;
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("POST")
			.setBody(checkpoint != null ? Utils.stringFormatter("checkpoint={}", checkpoint) : "")
			.build();
		Response response = acdSession.execute(request);
		ChangesInfo changesInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), ChangesInfo.class);
		return changesInfo;
	}
}
