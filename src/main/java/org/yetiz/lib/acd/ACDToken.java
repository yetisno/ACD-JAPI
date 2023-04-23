package org.yetiz.lib.acd;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yeti on 2015/4/13.
 */
public class ACDToken {
	private String token_type;
	private String access_token;
	private String refresh_token;
	private Date expireTime;

	public ACDToken(String token_type, Date expireTime, String refresh_token, String access_token) {
		this.token_type = token_type;
		this.expireTime = expireTime;
		this.refresh_token = refresh_token;
		this.access_token = access_token;
	}

	public String getTokenType() {
		return token_type;
	}

	public String getAccessToken() {
		return access_token;
	}

	public String getRefreshToken() {
		return refresh_token;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public boolean isExpire() {
		return expireTime.before(Calendar.getInstance().getTime());
	}

	public String getAuthorizationString() {
		return Character.toUpperCase(token_type.charAt(0)) + token_type.substring(1) + " " + access_token;
	}
}
