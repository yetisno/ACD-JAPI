package org.yetiz.lib.acd.api.v1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.Entity.*;
import org.yetiz.lib.acd.Utils;
import org.yetiz.lib.utils.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yeti on 2015/4/13.
 */
public class Trash {
	private static String root = "trash";

	/**
	 * Move node to Trash
	 *
	 * @param acdSession
	 * @param node
	 * @return
	 */
	public static NodeInfo moveNodeToTrash(ACDSession acdSession, NodeInfo node) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = acdSession.getMetadataUrl(Utils.stringFormatter(
			"{}/{}", root, node.getId()));
		Request request = new RequestBuilder()
			.setUrl(acdSession.getContentUrl(resourceEndpoint))
			.setMethod("PUT")
			.build();
		Response response = acdSession.execute(request);
		NodeInfo rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), NodeInfo.class);
		return rtnNodeInfo;
	}

	/**
	 * Get node list at Trash
	 *
	 * @param acdSession
	 * @param startToken Default: null
	 * @return
	 */
	public static NodeInfoList getNodeList(ACDSession acdSession, String startToken) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = root;
		if (startToken != null) {
			resourceEndpoint = Utils.stringFormatter("{}&startToken={}", resourceEndpoint, startToken);
		}
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());

		JsonObject responseObject = ((JsonObject) new JsonParser().parse(Utils.getResponseBody(response)));
		List<NodeInfo> data = new ArrayList<NodeInfo>();
		for (Iterator<JsonElement> iterator = responseObject.get("data").getAsJsonArray().iterator();
		     iterator.hasNext(); ) {
			JsonObject object = ((JsonObject) iterator.next());
			if (object.get("kind").getAsString().equals("FILE")) {
				data.add(Utils.getGson().fromJson(object, FileInfo.class));
			}
			if (object.get("kind").getAsString().equals("FOLDER")) {
				data.add(Utils.getGson().fromJson(object, FolderInfo.class));
			}
			if (object.get("kind").getAsString().equals("ASSET")) {
				data.add(Utils.getGson().fromJson(object, AssetInfo.class));
			}
		}
		NodeInfoList nodeInfoList = new NodeInfoList(responseObject.get("count").getAsLong(),
			(responseObject.has("nextToken") ? responseObject.get("nextToken").getAsString() : ""), data);
		return nodeInfoList;
	}

	/**
	 * Restore node from trash.
	 *
	 * @param acdSession
	 * @param node       key is required.
	 * @return
	 */
	public static NodeInfo restore(ACDSession acdSession, NodeInfo node) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = acdSession.getMetadataUrl(Utils.stringFormatter(
			"{}/{}/restore", root, node.getId()));
		Request request = new RequestBuilder()
			.setUrl(acdSession.getContentUrl(resourceEndpoint))
			.setMethod("POST")
			.build();
		Response response = acdSession.execute(request);
		NodeInfo rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), NodeInfo.class);
		return rtnNodeInfo;
	}
}
