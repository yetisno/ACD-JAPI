package org.yetiz.lib.acd.exception;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class RequestRateLimitedException extends ACDResponseException {

	public RequestRateLimitedException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.REQUEST_RATE_LIMITED;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}