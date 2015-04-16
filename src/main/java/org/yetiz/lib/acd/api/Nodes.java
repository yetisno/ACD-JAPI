package org.yetiz.lib.acd.api;

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
	public static String root = "nodes/";

	/**
	 * POST : {{contentUrl}}/nodes?suppress={suppress}
	 *
	 * @param acdSession
	 * @param uploadedFileInfo name and kind required. labels, properties and parents are optional.
	 * @param uploadFile
	 * @return
	 */
	public static FileInfo uploadFile(ACDSession acdSession, FileInfo uploadedFileInfo, File uploadFile) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = "nodes";
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
	 * @param acdSession
	 * @param overwritedFileInfo
	 * @param uploadFile
	 * @return
	 */
	public static FileInfo overwriteFile(ACDSession acdSession, FileInfo overwritedFileInfo, File uploadFile) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, overwritedFileInfo.getId(), "/content");
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
	 * @param acdSession
	 * @param downloadedFileInfo
	 * @return
	 */
	public static InputStream downloadFile(ACDSession acdSession, FileInfo downloadedFileInfo) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, downloadedFileInfo.getId(), "/content");
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
	 * Get List of all FileInfo.
	 *
	 * @param acdSession
	 * @param startToken   <code>nextToken</code> from previous request for access more content. Default: null
	 * @param sort         Sortable fields - createdDate, modifiedDate, contentDate, name, size and contentType. <br />
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
	 * Get List of all FileInfo with sort="" and limit=200 and tempLink=true and asset=NONE
	 *
	 * @param acdSession
	 * @param startToken <code>nextToken</code> from previous request for access more content. Default: null
	 * @return
	 */
	public static FileInfoList getFileInfoLists(ACDSession acdSession, String startToken) {
		return getFileInfoLists(acdSession, startToken, null, null, true, null);
	}

	/**
	 * get File Metadata
	 *
	 * @param acdSession
	 * @param id
	 * @param withTempLink if true, the response will contain tempLink for download. Default: false
	 * @param withAsset    "ALL", "NONE". Default: "NONE"
	 * @return
	 */
	public static FileInfo getFileMetadata(ACDSession acdSession, String id, Boolean withTempLink, String withAsset) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint;
		resourceEndpoint = Utils.stringAppender(root, id, "?");
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
	 * get File Metadata with withAsset="ALL" and withTempLink = true
	 *
	 * @param acdSession
	 * @param id
	 * @return
	 */
	public static FileInfo getFileMetadata(ACDSession acdSession, String id) {
		return getFileMetadata(acdSession, id, true, "ALL");
	}

	/**
	 * update File Metadata, only accept name, description and labels
	 *
	 * @param acdSession
	 * @param fileInfo
	 * @return
	 */
	public static FileInfo updateFileMetadata(ACDSession acdSession, FileInfo fileInfo) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, fileInfo.getId());
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
	 * @param acdSession
	 * @return
	 */
	public static FolderInfo getRootFolder(ACDSession acdSession) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, "?filters=isRoot:true");
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());

		FolderInfo folderInfo = Utils.getGson().fromJson(Utils.getResponseBody(response), FolderInfo.class);
		return folderInfo;

	}

	/**
	 * Create Folder by <code>folderInfo</code><br />
	 * name is required. <br />
	 * kind must be <code>FOLDER</code>. <br />
	 * description is optional. <br />
	 * labels is optional. <br />
	 * parents is optional. <br />
	 *
	 * @param acdSession
	 * @param folderInfo create folder according to <code>folderInfo</code>.
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
	 * @param acdSession
	 * @param id
	 * @return
	 */
	public static FolderInfo getFolderMetadata(ACDSession acdSession, String id) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, id);
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
	 * @param acdSession
	 * @param folderInfo
	 * @return
	 */
	public static FolderInfo updateFolderMetadata(ACDSession acdSession, FolderInfo folderInfo) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, folderInfo.getId());
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
	 * @param acdSession
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
	 * @param acdSession
	 * @param parent
	 * @param child
	 */
	public static void addChildToNode(ACDSession acdSession, NodeInfo parent, NodeInfo child) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, parent.getId(), "/children/", child.getId());
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("PUT")
			.build();
		acdSession.execute(request);
	}

	/**
	 * remove child from <code>parent</code> Node.
	 *
	 * @param acdSession
	 * @param parent
	 * @param child
	 */
	public static void removeChildFromNode(ACDSession acdSession, NodeInfo parent, NodeInfo child) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, parent.getId(), "/children/", child.getId());
		Request request = new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("DELETE")
			.build();
		Log.i("removeChildFromNode", request.toString());
		acdSession.execute(request);

	}

	/**
	 * Get List of target <code>parent</code> Node, only show FOLDER or FILE. Each query return max to 200 Node.
	 *
	 * @param acdSession
	 * @param parent     target Node
	 * @param startToken <code>nextToken</code> from previous request for access more content. Default: null
	 */
	public static NodeInfoList getChildsListOfNode(ACDSession acdSession, NodeInfo parent, String startToken) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, parent.getId(), "/children?filters=kind:(FOLDER OR "
			+ "FILE)");
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
		}
		NodeInfoList nodeInfoList = new NodeInfoList(responseObject.get("count").getAsLong(),
			(responseObject.has("nextToken") ? responseObject.get("nextToken").getAsString() : ""), data);
		return nodeInfoList;
	}

	/**
	 * add property to <code>node</code> in <code>owner</code> group.
	 *
	 * @param acdSession
	 * @param node
	 * @param property   require owner, key and value.
	 * @return
	 */
	public static Property addProperty(ACDSession acdSession, NodeInfo node, Property property) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, node.getId(), "/properties/", property.getOwner(), "/",
			property.getKey());
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("PUT")
			.build());
		Property rtnProperty = Utils.getGson().fromJson(Utils.getResponseBody(response), Property.class);
		return rtnProperty;
	}

	/**
	 * Get all properties of <code>node</code> with target <code>owner</code> Group.
	 *
	 * @param acdSession
	 * @param node
	 * @param owner      can't be null.
	 * @return
	 */
	public static Properties getProperties(ACDSession acdSession, NodeInfo node, String owner) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, node.getId(), "/properties/", owner);
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		Properties rtnProperties = Utils.getGson().fromJson(Utils.getResponseBody(response), Properties.class);
		return rtnProperties;
	}

	/**
	 * Get Property value of <code>node</code> by <code>property</code>.
	 *
	 * @param acdSession
	 * @param node
	 * @param property   require owner and key.
	 * @return
	 */
	public static Property getProperty(ACDSession acdSession, NodeInfo node, Property property) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, node.getId(), "/properties/", property.getOwner(), "/",
			property.getKey());
		Response response = acdSession.execute(new RequestBuilder()
			.setUrl(acdSession.getMetadataUrl(resourceEndpoint))
			.setMethod("GET")
			.build());
		Property rtnProperty = Utils.getGson().fromJson(Utils.getResponseBody(response), Property.class);
		rtnProperty.setOwner(property.getOwner());
		return rtnProperty;
	}

	/**
	 * Delete target <code>property</code> in <code>node</code>.
	 *
	 * @param acdSession
	 * @param node
	 * @param property   require owner, key
	 */
	public static void deleteProperty(ACDSession acdSession, NodeInfo node, Property property) {
		Log.d(Utils.getCurrentMethodName());
		String resourceEndpoint = Utils.stringAppender(root, node.getId(), "/properties/", property.getOwner(), "/",
			property.getKey());
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
