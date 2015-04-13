package org.yetiz.lib.acd.exception;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by yeti on 2015/4/13.
 */
public class AuthorizationRequiredException extends RuntimeException {
	/**
	 * Constructs a new runtime exception with {@code null} as its
	 * detail message.  The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	private String client_id;
	private String redirectUrl;
	private boolean writable;

	public AuthorizationRequiredException(String client_id, String redirectUrl, boolean writable) {
		this.client_id = client_id;
		this.redirectUrl = redirectUrl;
		this.writable = writable;
	}

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this {@code Throwable} instance
	 * (which may be {@code null}).
	 */
	@Override
	public String getMessage() {
		String rtn = "";
		try {
			rtn = "Parse " +
				"https://www.amazon.com/ap/oa?client_id=" + URLEncoder.encode(client_id, "UTF-8") +
				"&scope=clouddrive%3Aread" + (writable ? "%20clouddrive%3Awrite" : "") +
				"&response_type=code" +
				"&redirect_uri=" + redirectUrl +
				"\n to Web Browser, login and do Authorization. Then paste parameter [code] back.\n" +
				"Example: \n" +
				redirectUrl + "/?code=ASdkwelad2321SDsdEG&scope=clouddrive%3Aread+clouddrive%3Awrite \n" +
				", then code is ASdkwelad2321SDsdEG";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rtn;
	}
}
