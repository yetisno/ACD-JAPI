package org.yetiz.lib.acd.Entity;

import java.util.Date;
import java.util.Map;

/**
 * Created by yeti on 2015/4/16.
 */
public class Nodes {
	protected String id;
	protected String name;
	protected String kind;
	protected Long version;
	protected Date modifiedDate;
	protected Date creationDate;
	protected String[] labels;
	protected String description;
	protected String createdBy;
	protected String[] parents;
	protected String status;
	protected Boolean restricted;
	protected Boolean isShared;
	protected Map<String, Map<String, String>> properties;
}
