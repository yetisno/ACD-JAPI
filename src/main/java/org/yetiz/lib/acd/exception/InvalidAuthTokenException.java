package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class InvalidAuthTokenException extends ACDResponseException {

	public InvalidAuthTokenException(Response response) {
		super(response);
	}

	@Override
	public String getAmazonDescription() {
		return "The client passed in the invalid Auth token. Client should refresh the token and then try again.";
	}
}