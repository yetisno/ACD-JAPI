package org.yetiz.lib.acd.api.v1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.client.multipart.FilePart;
import com.ning.http.client.multipart.StringPart;
import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.Entity.*;
import org.yetiz.lib.acd.Utils;
import org.yetiz.lib.acd.exception.BadContentException;
import org.yetiz.lib.utils.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yeti on 2015/4/13.
 */
public class Nodes {
	public static String root = "nodes";

	/**
	 * Upload file
	 *
	 * @param acdSession       ACDSession ACDSession
	 * @param uploadedFileInfo name and kind are required. labels, properties and parents are optional.
	 * @param uploadFile       Upload File Path
	 * @return
	 */
	public static FileInfo uploadFile(ACDSession acdSession, FileInfo uploadedFileInfo, File uploadFile) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = root + "?suppress=deduplication";
		CreateStructure createStructure = new CreateStructure();
		createStructure.name = uploadedFileInfo.getName();
		createStructure.kind = uploadedFileInfo.getKind();
		createStructure.parents = uploadedFileInfo.getParents();
		createStructure.labels = uploadedFileInfo.getLabels();
		createStructure.description = null;
		Request request = new RequestBuilder()
			.setUrl(acdSession.getContentUrl(resourceEndpoint))
			.setMethod("POST")
			.addBodyPart(new StringPart("metadata", Utils.getGson().toJson(createStructure)))
			.addBodyPart(new FilePart("content", uploadFile))
			.build();
		Response response = acdSession.execute(request);
		FileInfo rtnProperties = Utils.getGson().fromJson(Utils.getResponseBody(response), FileInfo.class);
		return rtnProperties;
	}

	/**
	 * overwrite target file by <code>uploadFile</code>
	 *
	 * @param acdSession         ACDSession
	 * @param overwritedFileInfo
	 * @param uploadFile         Upload File Path
	 * @return
	 */
	public static FileInfo overwriteFile(ACDSession acdSession, FileInfo overwritedFileInfo, File uploadFile) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", overwritedFileInfo.getId(), "/content");
		CreateStructure createStructure = new CreateStructure();
		Request request = new RequestBuilder()
			.setUrl(acdSession.getContentUrl(resourceEndpoint))
			.setMethod("PUT")
			.addBodyPart(new FilePart("content", uploadFile))
			.build();
		Response response = acdSession.execute(request);
		FileInfo rtnProperties = Utils.getGson().fromJson(Utils.getResponseBody(response), FileInfo.class);
		return rtnProperties;
	}

	/**
	 * Download file as <code>InputStream</code>
	 *
	 * @param acdSession         ACDSession
	 * @param downloadedFileInfo target file info
	 * @return
	 */
	public static InputStream downloadFile(ACDSession acdSession, FileInfo downloadedFileInfo) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", downloadedFileInfo.getId(), "/content");
		CreateStructure createStructure = new CreateStructure();
		Request request = new RequestBuilder()
			.setUrl(acdSession.getContentUrl(resourceEndpoint))
			.setMethod("GET")
			.build();
		Response response = acdSession.execute(request);
		try {
			return response.getResponseBodyAsStream();
		} catch (IOException e) {
			throw new BadContentException("getResponseBodyAsStream()");
		}
	}

	/**
	 * Get List of all FileInfo with sort="" and limit=200 and tempLink=true and asset=NONE
	 *
	 * @param acdSession ACDSession
	 * @param startToken <code>nextToken</code> from previous request for access more content. Default: null
	 * @return
	 */
	public static FileInfoList getFileInfoLists(ACDSession acdSession, String startToken) {
		return getFileInfoLists(acdSession, startToken, null, null, true, null);
	}

	/**
	 * Get List of all FileInfo.
	 *
	 * @param acdSession   ACDSession
	 * @param startToken   <code>nextToken</code> from previous request for access more content. Default: null
	 * @param sort         Sortable fields - createdDate, modifiedDate, contentDate, name, size and contentType. <br>
	 *                     Example: sort:["name ASC","contentProperties.size DESC"].  Default: ""
	 * @param limit        for pagination, Default: 200
	 * @param withTempLink if true, the response will contain tempLink for download. Default: false
	 * @param withAsset    "ALL", "NONE". Default: "NONE"
	 * @return
	 */
	public static FileInfoList getFileInfoLists(ACDSession acdSession, String startToken, String sort, Integer limit,
	                                            Boolean withTempLink,
	                                            String withAsset) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint;
		resourceEndpoint = root.concat("?filters=kind:FILE");
		if (startToken != null) {
			resourceEndpoint = Utils.stringFormatter("{}&startToken={}", resourceEndpoint, startToken);
		}
		if (startToken != sort) {
			resourceEndpoint = Utils.stringFormatter("{}&sort={}", resourceEndpoint, sort);
		}
		if (limit != null && limit > 0) {
			resourceEndpoint = Utils.stringFormatter("{}&limit={}", resourceEndpoint, limit);
		}
		if (withTempLink) {
			resourceEndpoint = Utils.stringFormatter("{}&tempLink=true", resourceEndpoint);
		}
		if (withAsset != null) {
			resourceEndpoint = Utils.stringFormatter("{}&asset={}", resourceEndpoint, withAsset);
		}
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		FileInfoList fileInfoList = Utils.getGson().fromJson(Utils.getResponseBody(response), FileInfoList.class);
		return fileInfoList;
	}

	/**
	 * get File Metadata with withAsset="ALL" and withTempLink = true
	 *
	 * @param acdSession ACDSession
	 * @param id         Node ID
	 * @return
	 */
	public static FileInfo getFileMetadata(ACDSession acdSession, String id) {
		return getFileMetadata(acdSession, id, true, "ALL");
	}

	/**
	 * get File Metadata
	 *
	 * @param acdSession   ACDSession
	 * @param id           Node ID
	 * @param withTempLink if true, the response will contain tempLink for download. Default: false
	 * @param withAsset    "ALL", "NONE". Default: "NONE"
	 * @return
	 */
	public static FileInfo getFileMetadata(ACDSession acdSession, String id, Boolean withTempLink, String withAsset) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint;
		resourceEndpoint = Utils.stringAppender(root, "/", id, "?");
		if (withTempLink) {
			resourceEndpoint = Utils.stringAppender(resourceEndpoint, "tempLink=true");
		}
		if (withAsset != null && withAsset != "") {
			resourceEndpoint = Utils.stringAppender(resourceEndpoint, "&asset=", withAsset);
		}
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());

		FileInfo fileInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FileInfo.class);
		return fileInfo;
	}

	/**
	 * update File Metadata, only accept name, description and labels
	 *
	 * @param acdSession ACDSession
	 * @param fileInfo   File Info
	 * @return
	 */
	public static FileInfo updateFileMetadata(ACDSession acdSession, FileInfo fileInfo) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", fileInfo.getId());
		PatchStructure patchStructure = new PatchStructure();
		patchStructure.name = fileInfo.getName();
		patchStructure.description = fileInfo.getDescription();
		patchStructure.labels = fileInfo.getLabels();
		String body = Utils.getGson().toJson(patchStructure);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("PATCH")
			.setBody(body)
			.build());

		FileInfo rtnFileInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FileInfo.class);
		return rtnFileInfo;
	}

	/**
	 * Get root folder
	 *
	 * @param acdSession ACDSession
	 * @return
	 */
	public static FolderInfo getRootFolder(ACDSession acdSession) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "?filters=isRoot:true");
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());

		FolderInfoList folderInfoList = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfoList.class);
		return folderInfoList.getCount() == 1 ? folderInfoList.getList().get(0) : null;

	}

	/**
	 * Create Folder by <code>folderInfo</code><br>
	 * name is required. <br>
	 * kind must be <code>FOLDER</code>. <br>
	 * description is optional. <br>
	 * labels is optional. <br>
	 * parents is optional. <br>
	 *
	 * @param acdSession ACDSession
	 * @param folderInfo Folder Info create folder according to <code>folderInfo</code>.
	 * @return the created <code>FolderInfo</code>
	 */
	public static FolderInfo createFolder(ACDSession acdSession, FolderInfo folderInfo) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = root;
		CreateStructure createStructure = new CreateStructure();
		createStructure.name = folderInfo.getName();
		createStructure.description = folderInfo.getDescription();
		createStructure.labels = folderInfo.getLabels();
		createStructure.kind = "FOLDER";
		createStructure.parents = folderInfo.getParents();
		String body = Utils.getGson().toJson(createStructure);
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("POST")
			.setBody(body)
			.build();
		Response response = acdSession.execute(request);

		FolderInfo rtnFolderInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfo.class);
		return rtnFolderInfo;
	}

	/**
	 * get Folder metadata
	 *
	 * @param acdSession ACDSession
	 * @param id         Node ID
	 * @return
	 */
	public static FolderInfo getFolderMetadata(ACDSession acdSession, String id) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", id);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());

		FolderInfo folderInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfo.class);
		return folderInfo;
	}

	/**
	 * update Folder Metadata, only accept name, description and labels
	 *
	 * @param acdSession ACDSession
	 * @param folderInfo Folder Info
	 * @return
	 */
	public static FolderInfo updateFolderMetadata(ACDSession acdSession, FolderInfo folderInfo) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", folderInfo.getId());
		PatchStructure patchStructure = new PatchStructure();
		patchStructure.name = folderInfo.getName();
		patchStructure.description = folderInfo.getDescription();
		patchStructure.labels = folderInfo.getLabels();
		String body = Utils.getGson().toJson(patchStructure);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("PATCH")
			.setBody(body)
			.build());

		FolderInfo rtnFolderInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfo.class);
		return rtnFolderInfo;
	}

	/**
	 * Get List of all FolderInfo. each query return 200 max FolderInfo
	 *
	 * @param acdSession ACDSession
	 * @param startToken <code>nextToken</code> from previous request for access more content. Default: null
	 * @return
	 */
	public static FolderInfoList getFolderInfoLists(ACDSession acdSession, String startToken) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint;
		resourceEndpoint = root.concat("?filters=kind:FOLDER");
		if (startToken != null) {
			resourceEndpoint = Utils.stringFormatter("{}&startToken={}", resourceEndpoint, startToken);
		}
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());

		FolderInfoList folderInfoList = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfoList
			.class);
		return folderInfoList;
	}

	/**
	 * Add child to <code>parent</code> Node.
	 *
	 * @param acdSession ACDSession
	 * @param parent     Parent
	 * @param child      Child
	 */
	public static void addChildToNode(ACDSession acdSession, NodeInfo parent, NodeInfo child) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", parent.getId(), "/children/", child.getId());
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("PUT")
			.build();
		acdSession.execute(request);
	}

	/**
	 * remove child from <code>parent</code> Node.
	 *
	 * @param acdSession ACDSession
	 * @param parent     Parent
	 * @param child      Child
	 */
	public static void removeChildFromNode(ACDSession acdSession, NodeInfo parent, NodeInfo child) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", parent.getId(), "/children/", child.getId());
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("DELETE")
			.build();
		Log.i("removeChildFromNode", request.toString());
		acdSession.execute(request);

	}

	/**
	 * Get child list of target <code>parent</code>, with filters=kind:(FOLDER* OR FILE*)
	 *
	 * @param acdSession ACDSession
	 * @param parent     Parent Parent    target node
	 * @param startToken <code>nextToken</code> from previous request for access more content. Default: null
	 * @return
	 */
	public static NodeInfoList getChildList(ACDSession acdSession, NodeInfo parent, String
		startToken) {
		return getChildList(acdSession, parent, "kind:(FOLDER* OR FILE*)", startToken);
	}

	/**
	 * Get List of target <code>parent</code> Node, each query return max to 200 Node.
	 *
	 * @param acdSession ACDSession
	 * @param parent     Parent target Node
	 * @param filters    filter return result, Example: kind:(FOLDER* OR FILE*)
	 * @param startToken <code>nextToken</code> from previous request for access more content. Default: null
	 */
	public static NodeInfoList getChildList(ACDSession acdSession, NodeInfo parent, String filters, String
		startToken) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", parent.getId(), "/children?");
		if (filters != null && !filters.equals("")) {
			resourceEndpoint = Utils.stringFormatter("{}&filters={}", resourceEndpoint, filters);
		}
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
	 * add property to <code>node</code> in <code>owner</code> group.
	 *
	 * @param acdSession ACDSession
	 * @param node       Node
	 * @param property   require key and value.
	 * @return
	 */
	public static Property addProperty(ACDSession acdSession, NodeInfo node, Property property) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", node.getId(), "/properties/",
			acdSession.getConfigure().getOwner(), "/",
			property.getKey());
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setBody(Utils.stringFormatter("{\"value\":\"{}\"}", property.getValue()))
			.setMethod("PUT")
			.build());
		Property rtnProperty = Utils.getGson().fromJson(Utils.getResponseBody(response), Property.class);
		return rtnProperty;
	}

	/**
	 * Get all properties of <code>node</code> with target <code>owner</code> Group.
	 *
	 * @param acdSession ACDSession
	 * @param node       Node
	 * @param owner      Owner      can't be null.
	 * @return
	 */
	public static Properties getProperties(ACDSession acdSession, NodeInfo node, String owner) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", node.getId(), "/properties/", owner);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		Properties rtnProperties = Utils.getGson().fromJson(Utils.getResponseBody(response), Properties.class);
		rtnProperties.setOwner(owner);
		return rtnProperties;
	}

	/**
	 * Get Property value of <code>node</code> by <code>property</code>.
	 *
	 * @param acdSession ACDSession
	 * @param node       Node
	 * @param owner      Owner
	 * @param key        Key
	 * @return
	 */
	public static Property getProperty(ACDSession acdSession, NodeInfo node, String owner, String key) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", node.getId(), "/properties/", owner, "/",
			key);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		Property rtnProperty = Utils.getGson().fromJson(Utils.getResponseBody(response), Property.class);
		rtnProperty.setOwner(owner);
		return rtnProperty;
	}

	/**
	 * Delete target <code>property</code> in <code>node</code>.
	 *
	 * @param acdSession ACDSession
	 * @param node       Node
	 * @param key        Key
	 */
	public static void deleteProperty(ACDSession acdSession, NodeInfo node, String key) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "/", node.getId(), "/properties/",
			acdSession.getConfigure().getOwner(), "/", key);
		acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("DELETE")
			.build());
	}

	public static class PatchStructure {
		protected String name;
		protected String description;
		protected String[] labels = new String[]{};
	}

	public static class CreateStructure extends PatchStructure {
		protected String kind;
		protected String[] parents = new String[]{};
	}
}
