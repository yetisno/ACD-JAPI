package org.yetiz.lib.acd.Entity;

/**
 * Created by yeti on 2015/4/16.
 */
public class Property {
	protected Integer statusCode = Integer.MIN_VALUE;
	protected String owner = null;
	protected String location = "";
	protected String key = "";
	protected String value = "";

	public Property() {
	}

	public Property(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
