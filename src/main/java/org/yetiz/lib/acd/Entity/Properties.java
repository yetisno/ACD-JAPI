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
	protected Property[] properties = new Property[]{};

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Property[] get() {
		if (data.size() > 0) {
			List<Property> properties = new ArrayList<Property>();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				Property property = new Property();
				property.setOwner(owner);
				property.setKey(entry.getKey());
				property.setValue(entry.getValue());
				properties.add(property);
			}
			this.properties = properties.toArray(new Property[]{});
			data.clear();
		}
		return this.properties;
	}
}