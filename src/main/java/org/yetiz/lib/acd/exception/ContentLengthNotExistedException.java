package org.yetiz.lib.acd.exception;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class ContentLengthNotExistedException extends ACDResponseException {

	public ContentLengthNotExistedException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.CONTENT_LENGTH_NOT_EXIST;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}