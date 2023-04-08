package org.yetiz.lib.acd.exception;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class MethodNotAllowedException extends ACDResponseException {

	public MethodNotAllowedException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.METHOD_NOT_ALLOWED;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}