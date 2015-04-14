package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class RequestConflictException extends ACDResponseException {

	public RequestConflictException(Response response) {
		super(response);
	}

	@Override
	public String getAmazonDescription() {
		return "Conflict.";
	}
}