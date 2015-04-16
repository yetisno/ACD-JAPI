package org.yetiz.lib.acd.Entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeti on 2015/4/16.
 */
public abstract class NodeInfo {
	protected String id = "";
	protected String name = "";
	protected String kind = "";
	protected Long version = Long.MIN_VALUE;
	protected Date modifiedDate = new Date(0);
	protected Date creationDate = new Date(0);
	protected String eTagResponse = "";
	protected String[] labels = new String[]{};
	protected String description = "";
	protected String createdBy = "";
	protected String[] parents = new String[]{};
	protected String status = "";
	protected Boolean restricted = false;
	protected Boolean isShared = false;
	protected Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getKind() {
		return kind;
	}

	public Long getVersion() {
		return version;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String geteTagResponse() {
		return eTagResponse;
	}

	public String[] getLabels() {
		return labels;
	}

	public String getDescription() {
		return description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String[] getParents() {
		return parents;
	}

	public String getStatus() {
		return status;
	}

	public Boolean isRestricted() {
		return restricted;
	}

	public Boolean isShared() {
		return isShared;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParents(String[] parents) {
		this.parents = parents;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isFile() {
		return this instanceof FileInfo;
	}

	public Boolean isFolder() {
		return this instanceof FolderInfo;
	}

	public Boolean isAsset() {
		return this instanceof AssetInfo;
	}

	public Boolean isImage() {
		return this instanceof FileInfo && ((FileInfo) this).getContentProperties().getImage() != null;
	}

	public Boolean isVideo() {
		return this instanceof FileInfo && ((FileInfo) this).getContentProperties().getVideo() != null;
	}
}