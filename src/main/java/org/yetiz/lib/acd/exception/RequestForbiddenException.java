package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class RequestForbiddenException extends ACDResponseException {

	public RequestForbiddenException(Response response) {
		super(response);
	}

	@Override
	public String getAmazonDescription() {
		return "\t1. Customer doesn’t exist.\n" +
			"\t2. Application not registered.\n" +
			"\t3. Application try to access to properties not belong to an App.\n" +
			"\t4. Application try to trash/purge root node.\n" +
			"\t5. Application try to update contentProperties.\n" +
			"\t6. Operation is blocked (for third-party apps).";
	}
}