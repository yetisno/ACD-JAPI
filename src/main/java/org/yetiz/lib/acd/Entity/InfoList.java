package org.yetiz.lib.acd.Entity;

/**
 * Created by yeti on 2015/4/16.
 */
public abstract class InfoList<T> {
	protected Long count = Long.MIN_VALUE;
	protected String nextToken = "";
	protected T[] data;

	public Boolean hasNext() {
		return !nextToken.equals("");
	}

	public Long getCount() {
		return count;
	}

	public String getNextToken() {
		return nextToken;
	}

	public T[] getList() {
		return data;
	}
}
