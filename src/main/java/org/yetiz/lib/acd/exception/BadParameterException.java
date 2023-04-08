package org.yetiz.lib.acd.exception;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class BadParameterException extends ACDResponseException {

	public BadParameterException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.BAD_PARAMETER;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}