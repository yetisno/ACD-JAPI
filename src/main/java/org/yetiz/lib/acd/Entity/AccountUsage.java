package org.yetiz.lib.acd.Entity;

import java.util.Date;

/**
 * Created by yeti on 4/15/15.
 */
public class AccountUsage {
	private Date lastCalculated;
	private ResourceType other;
	private ResourceType doc;
	private ResourceType photo;
	private ResourceType video;


	public class ResourceType {
		private Meta total;
		private Meta billable;
	}

	public class Meta {
		private Long bytes;
		private Long count;
	}
}
