package org.yetiz.lib.acd.exception;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class ServiceUnavailableException extends ACDResponseException {

	public ServiceUnavailableException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.SERVICE_UNAVAILABLE;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}