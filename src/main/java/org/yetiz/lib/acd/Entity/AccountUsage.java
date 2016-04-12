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

	public Date getLastCalculated() {
		return lastCalculated;
	}

	public ResourceType getOther() {
		return other;
	}

	public ResourceType getDoc() {
		return doc;
	}

	public ResourceType getPhoto() {
		return photo;
	}

	public ResourceType getVideo() {
		return video;
	}

	public class ResourceType {
		private Meta total;
		private Meta billable;

		public Meta getTotal() {
			return total;
		}

		public Meta getBillable() {
			return billable;
		}
	}

	public class Meta {
		private Long bytes;
		private Long count;

		public Long getBytes() {
			return bytes;
		}

		public Long getCount() {
			return count;
		}
	}
}
