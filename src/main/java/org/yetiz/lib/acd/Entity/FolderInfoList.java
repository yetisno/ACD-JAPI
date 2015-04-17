package org.yetiz.lib.acd.Entity;

import java.util.List;

/**
 * Created by yeti on 2015/4/16.
 */
public class FolderInfoList extends InfoList<FolderInfo> {
	public FolderInfoList() {
	}

	public FolderInfoList(Long count, String nextToken, List<FolderInfo> data) {
		super(count, nextToken, data);
	}
}
