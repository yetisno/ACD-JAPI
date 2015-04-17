package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;
import org.yetiz.lib.acd.ResponseCode;

/**
 * Created by yeti on 2015/4/13.
 */
public class ResourceNotFoundException extends ACDResponseException {

	public ResourceNotFoundException(Response response) {
		super(response);
		statusCode = ResponseCode.Error.RESOURCE_NOT_FOUND;
	}

	@Override
	public String getAmazonDescription() {
		return statusCode.getDescription();
	}
}