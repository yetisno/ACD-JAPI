package org.yetiz.lib.acd.exception;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class PreconditionFailedException extends ACDResponseException {

	public PreconditionFailedException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.PRECONDITION_FAILED;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}