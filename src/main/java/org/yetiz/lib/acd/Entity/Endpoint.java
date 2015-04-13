package org.yetiz.lib.acd.Entity;

/**
 * Created by yeti on 2015/4/13.
 */
public class Endpoint {
	private boolean customerExists;
	private String contentUrl;
	private String metadataUrl;

	public boolean isCustomerExists() {
		return customerExists;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public String getMetadataUrl() {
		return metadataUrl;
	}
}