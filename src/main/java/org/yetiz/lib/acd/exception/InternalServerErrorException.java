package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class InternalServerErrorException extends ACDResponseException {

	public InternalServerErrorException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}