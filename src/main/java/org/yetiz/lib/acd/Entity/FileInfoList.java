package org.yetiz.lib.acd.Entity;

import java.util.List;

/**
 * Created by yeti on 2015/4/16.
 */
public class FileInfoList extends InfoList<FileInfo> {
	public FileInfoList() {
	}

	public FileInfoList(Long count, String nextToken, List<FileInfo> data) {
		super(count, nextToken, data);
	}
}
