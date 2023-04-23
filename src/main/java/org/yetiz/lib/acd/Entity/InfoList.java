package org.yetiz.lib.acd.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeti on 2015/4/16.
 */
public abstract class InfoList<T> {
	protected Long count = Long.MIN_VALUE;
	protected String nextToken = "";
	protected List<T> data = new ArrayList<>();

	public InfoList() {
	}

	public InfoList(Long count, String nextToken, List<T> data) {
		this.count = count;
		this.nextToken = nextToken;
		this.data = data;
	}

	public Boolean hasNext() {
		return !nextToken.equals("");
	}

	public Long getCount() {
		return count;
	}

	public String getNextToken() {
		return nextToken;
	}

	public List<T> getList() {
		return data;
	}
}
