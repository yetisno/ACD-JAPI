package org.yetiz.lib.acd.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeti on 4/17/15.
 */
public class ChangesInfo {
	protected Boolean end = false;
	protected String checkpoint = "";
	protected Boolean reset = false;
	protected List<NodeInfo> nodes = new ArrayList<NodeInfo>();

	public Boolean hasNext() {
		return !isEnd();
	}

	public Boolean isEnd() {
		return end;
	}

	public String getCheckpoint() {
		return checkpoint;
	}

	public Boolean isReset() {
		return reset;
	}

	public List<NodeInfo> getNodes() {
		return nodes;
	}
}
