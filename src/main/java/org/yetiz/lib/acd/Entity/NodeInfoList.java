package org.yetiz.lib.acd.Entity;

import java.util.List;

/**
 * Created by yeti on 2015/4/16.
 */
public class NodeInfoList extends InfoList<NodeInfo> {
	public NodeInfoList() {
	}

	public NodeInfoList(Long count, String nextToken, List<NodeInfo> data) {
		super(count, nextToken, data);
	}
}
