package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class InvalidAuthTokenException extends ACDResponseException {

	public InvalidAuthTokenException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.INVALID_AUTH_TOKEN;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}

}