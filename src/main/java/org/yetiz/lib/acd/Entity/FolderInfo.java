package org.yetiz.lib.acd.Entity;

/**
 * Created by yeti on 2015/4/16.
 */
public class FolderInfo extends NodeInfo {
	protected Boolean isRoot = false;

	public FolderInfo() {
		kind = "FOLDER";
	}

	public FolderInfo(String id) {
		super();
		this.id = id;
	}

	public Boolean isRoot() {
		return isRoot;
	}
}
