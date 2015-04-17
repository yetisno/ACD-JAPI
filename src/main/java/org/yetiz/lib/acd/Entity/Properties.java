package org.yetiz.lib.acd.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yeti on 2015/4/16.
 */
public class Properties {
	protected Integer statusCode = Integer.MIN_VALUE;
	protected String owner = "CloudDrive";
	protected Map<String, String> data = new HashMap<String, String>();
	protected List<Property> properties;

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Property> get() {
		properties = new ArrayList<Property>();
		if (data.size() > 0) {
			Property property;
			for (Map.Entry<String, String> entry : data.entrySet()) {
				property = new Property(entry.getKey(), entry.getValue());
				property.setOwner(owner);
				properties.add(property);
			}
			data.clear();
		}
		return properties;
	}
}