package org.yetiz.lib.acd;

import org.yetiz.lib.acd.Entity.*;
import org.yetiz.lib.acd.api.v1.Account;
import org.yetiz.lib.acd.api.v1.Nodes;
import org.yetiz.lib.acd.api.v1.Trash;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeti on 2015/4/13.
 */
public class ACD {

	private ACDSession acdSession = null;

	public ACD(ACDSession acdSession) {
		this.acdSession = acdSession;
	}

	public AccountInfo getUserInfo() {
		return Account.getAccountInfo(acdSession);
	}

	public AccountUsage getUserUsage() {
		return Account.getAccountUsage(acdSession);
	}

	public AccountQuota getUserQuota() {
		return Account.getAccountQuota(acdSession);
	}

	/**
	 * Upload file
	 *
	 * @param parentID
	 * @param name     filename
	 * @param file
	 * @return
	 */
	public FileInfo uploadFile(String parentID, String name, File file) {
		FileInfo fileInfo = new FileInfo();
		if (parentID != null && !parentID.equals(""))
			fileInfo.setParents(new String[]{parentID});
		fileInfo.setName(name);
		return Nodes.uploadFile(acdSession, fileInfo, file);
	}

	/**
	 * update file content
	 *
	 * @param id
	 * @param file
	 * @return
	 */
	public FileInfo updateFile(String id, File file) {
		return Nodes.overwriteFile(acdSession, new FileInfo(id), file);
	}

	/**
	 * rename file
	 *
	 * @param id
	 * @param name
	 * @return
	 */
	public FileInfo renameFile(String id, String name) {
		FileInfo fileInfo = new FileInfo(id);
		fileInfo.setName(name);
		return Nodes.updateFileMetadata(acdSession, fileInfo);
	}

	/**
	 * download file
	 *
	 * @param id
	 * @return
	 */
	public InputStream getFile(String id) {
		return Nodes.downloadFile(acdSession, new FileInfo(id));
	}

	/**
	 * delete file
	 *
	 * @param id
	 * @return
	 */
	public FileInfo removeFile(String id) {
		return ((FileInfo) Trash.moveNodeToTrash(acdSession, new FileInfo(id)));
	}

	/**
	 * restore file
	 *
	 * @param id
	 * @return
	 */
	public FileInfo restoreFile(String id) {
		return ((FileInfo) Trash.restore(acdSession, new FileInfo(id)));
	}

	/**
	 * create folder
	 *
	 * @param parentID
	 * @param name
	 * @return
	 */
	public FolderInfo createFolder(String parentID, String name) {
		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setName(name);
		if (parentID != null && !parentID.equals(""))
			folderInfo.setParents(new String[]{parentID});
		return Nodes.createFolder(acdSession, folderInfo);
	}

	/**
	 * rename folder
	 *
	 * @param id
	 * @param name
	 * @return
	 */
	public FolderInfo renameFolder(String id, String name) {
		FolderInfo folderInfo = new FolderInfo(id);
		folderInfo.setName(name);
		return Nodes.updateFolderMetadata(acdSession, folderInfo);
	}

	/**
	 * remove folder
	 *
	 * @param id
	 * @return
	 */
	public FolderInfo removeFolder(String id) {
		return ((FolderInfo) Trash.moveNodeToTrash(acdSession, new FolderInfo(id)));
	}

	/**
	 * restore folder
	 *
	 * @param id
	 * @return
	 */
	public FolderInfo restoreFolder(String id) {
		return ((FolderInfo) Trash.restore(acdSession, new FolderInfo(id)));
	}

	private List<NodeInfo> getList(String id, String startToken) {
		List<NodeInfo> rtnList = new ArrayList<NodeInfo>();
		NodeInfoList nodeInfoList = Nodes.getChildList(acdSession, new FolderInfo(id), startToken);
		rtnList.addAll(nodeInfoList.getList());
		if (nodeInfoList.hasNext()) {
			rtnList.addAll(getList(id, nodeInfoList.getNextToken()));
		}
		return rtnList;
	}

	/**
	 * get list below target id
	 *
	 * @param id
	 * @return
	 */
	public List<NodeInfo> getList(String id) {
		return getList(id, null);
	}

	/**
	 * add Property
	 *
	 * @param id
	 * @param key
	 * @param value
	 */
	public void addProperty(String id, String key, String value) {
		Nodes.addProperty(acdSession, new FileInfo(id), new Property(key, value));
	}

	/**
	 * remove Property
	 *
	 * @param id
	 * @param key
	 */
	public void removeProperty(String id, String key) {
		Nodes.deleteProperty(acdSession, new FileInfo(id), key);
	}

	/**
	 * get properties of target id
	 *
	 * @param id
	 * @return
	 */
	public Properties getProperties(String id) {
		return Nodes.getProperties(acdSession, new FileInfo(id), acdSession.getConfigure().getOwner());
	}

	/**
	 * destroy ACD and close acdSession
	 */
	public void destroy() {
		acdSession.destroy();
	}
}
