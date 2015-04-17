package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class RequestConflictException extends ACDResponseException {

	public RequestConflictException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.REQUEST_CONFLICT;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}