package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class InternalServerErrorException extends ACDResponseException {

	public InternalServerErrorException(Response response) {
		super(response);
	}

	@Override
	public String getAmazonDescription() {
		return "Servers are not working as expected. The request is probably valid but needs to be requested again later.";
	}
}