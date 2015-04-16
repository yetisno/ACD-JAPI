package org.yetiz.lib.acd.api;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.Entity.*;
import org.yetiz.lib.acd.Utils;
import org.yetiz.lib.utils.Log;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.InputStream;

/**
 * Created by yeti on 2015/4/13.
 */
public class Nodes {
	public static String root = "/nodes/";

	public static FileInfo uploadFile(ACDSession acdSession, FileInfo uploadedFileInfo, File uploadFile) {

		throw new NotImplementedException();
	}

	public static FileInfo overwriteFile(ACDSession acdSession, FileInfo overwritedFileInfo, File uploadFile) {

		throw new NotImplementedException();
	}

	public static InputStream downloadFile(ACDSession acdSession, FileInfo downloadedFileInfo) {

		throw new NotImplementedException();
	}

	/**
	 * Get List of all FileInfo.
	 *
	 * @param acdSession
	 * @param startToken   <code>nextToken</code> from previous request for access more content. Default: ""
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
		Log.d("getFileInfoLists");
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
	 * @param startToken
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
		Log.d("getFileMetadata");
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
		Log.d("updateFileMetadata");
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
		Log.d("getRootFolder");
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
	 * @param acdSession
	 * @param folderInfo create folder according to <code>folderInfo</code>.
	 * @return the created <code>FolderInfo</code>
	 */
	public static FolderInfo createFolder(ACDSession acdSession, FolderInfo folderInfo) {
		Log.d("createFolder");
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
		Log.i("createFolder", request.toString());
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
		Log.d("getFolderMetadata");
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
		Log.d("updateFolderMetadata");
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

	public static void addChildToNode(ACDSession acdSession, NodeInfo parent, NodeInfo child) {
		throw new NotImplementedException();
	}

	public static void removeChildFromNode(ACDSession acdSession, NodeInfo parent, NodeInfo child) {
		throw new NotImplementedException();
	}

	public static void getChildsListOfNode(ACDSession acdSession, NodeInfo parent, String startToken) {
		throw new NotImplementedException();
	}

	public static Property addProperty(ACDSession acdSession, NodeInfo node, Property property) {
		throw new NotImplementedException();
	}

	public static Properties getProperties(ACDSession acdSession, NodeInfo node, String owner) {
		throw new NotImplementedException();
	}

	public static Property getProperty(ACDSession acdSession, NodeInfo node, Property property) {
		throw new NotImplementedException();
	}

	public static void deleteProperty(ACDSession acdSession, NodeInfo node, Property property) {
		throw new NotImplementedException();
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
