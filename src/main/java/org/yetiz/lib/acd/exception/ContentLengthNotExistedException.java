package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class ContentLengthNotExistedException extends ACDResponseException {

	public ContentLengthNotExistedException(Response response) {
		super(response);
	}

	@Override
	public String getAmazonDescription() {
		return "The Content-Length header was not specified.";
	}
}