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

	public class Benefit {
		private Date expiration;
		private String benefit;
	}
}