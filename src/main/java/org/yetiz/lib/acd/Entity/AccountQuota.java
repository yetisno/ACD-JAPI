package org.yetiz.lib.acd.Entity;

import java.util.Date;

/**
 * Created by yeti on 4/15/15.
 */
public class AccountQuota {
	private Long quota;
	private Date lastCalculated;
	private Long available;
	private String[] plans;
	private String[] grants;
	private Benefit[] benefits;

	public Long getQuota() {
		return quota;
	}

	public Date getLastCalculated() {
		return lastCalculated;
	}

	public Long getAvailable() {
		return available;
	}

	public String[] getPlans() {
		return plans;
	}

	public String[] getGrants() {
		return grants;
	}

	public Benefit[] getBenefits() {
		return benefits;
	}

	public static class Benefit {
		private Date expiration;
		private String benefit;

		public Date getExpiration() {
			return expiration;
		}

		public String getBenefit() {
			return benefit;
		}
	}
}