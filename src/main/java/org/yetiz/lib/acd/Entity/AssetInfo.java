package org.yetiz.lib.acd.Entity;

/**
 * Created by yeti on 2015/4/16.
 */
public class AssetInfo extends FileInfo {
	public AssetInfo() {
		kind = "ASSET";
	}

	public AssetInfo(String id) {
		super();
		this.id = id;
	}
}
