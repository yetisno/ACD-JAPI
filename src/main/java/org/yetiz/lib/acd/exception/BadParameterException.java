package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class BadParameterException extends ACDResponseException {

	public BadParameterException(Response response) {
		super(response);
	}

	@Override
	public String getAmazonDescription() {
		return "Bad input parameter. Error message should indicate which one and why.";
	}
}