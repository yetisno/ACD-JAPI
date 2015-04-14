package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class MethodNotAllowedException extends ACDResponseException {

	public MethodNotAllowedException(Response response) {
		super(response);
	}

	@Override
	public String getAmazonDescription() {
		return "The resource doesn't support the specified HTTP verb.";
	}
}