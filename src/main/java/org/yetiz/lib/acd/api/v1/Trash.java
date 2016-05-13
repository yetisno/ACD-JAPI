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
	 * Move node to Trash, if the kind is not FILE, FOLDER, ASSET, it return null.
	 *
	 * @param acdSession
	 * @param node
	 * @return
	 */
	public static NodeInfo moveNodeToTrash(ACDSession acdSession, NodeInfo node) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringFormatter("{}/{}", root, node.getId());
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setBody("{}")
			.setMethod("PUT")
			.build();
		Response response = acdSession.execute(request);
		String kind = ((JsonObject) new JsonParser().parse(Utils.getResponseBody(response))).get("kind").getAsString();
		NodeInfo rtnNodeInfo = null;
		if (kind.equals("FILE")) {
			rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FileInfo.class);
		}
		if (kind.equals("FOLDER")) {
			rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfo.class);
		}
		if (kind.equals("ASSET")) {
			rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), AssetInfo.class);
		}
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
			String kind = object.get("kind").getAsString();
			if (kind.equals("FILE")) {
				data.add(Utils.getGson().fromJson(object, FileInfo.class));
			}
			if (kind.equals("FOLDER")) {
				data.add(Utils.getGson().fromJson(object, FolderInfo.class));
			}
			if (kind.equals("ASSET")) {
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
		String resourceEndpoint = Utils.stringFormatter("{}/{}/restore", root, node.getId());
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("POST")
			.build();
		Response response = acdSession.execute(request);
		String kind = ((JsonObject) new JsonParser().parse(Utils.getResponseBody(response))).get("kind").getAsString();
		NodeInfo rtnNodeInfo = null;
		if (kind.equals("FILE")) {
			rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FileInfo.class);
		}
		if (kind.equals("FOLDER")) {
			rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfo.class);
		}
		if (kind.equals("ASSET")) {
			rtnNodeInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), AssetInfo.class);
		}
		return rtnNodeInfo;
	}
}
