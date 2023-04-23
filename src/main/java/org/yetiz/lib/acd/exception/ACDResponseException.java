package org.yetiz.lib.acd.exception;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.ResponseCode;
import org.yetiz.lib.utils.Log;

/**
 * Created by yeti on 4/14/15.
 */
public class ACDResponseException extends RuntimeException {
	protected ResponseCode.Error statusCode;
	private String responseBody;

	public ACDResponseException(Response response) {
		responseBody = response.hasResponseBody() ? response.getResponseBody() : response.getStatusText();
		try {
			Log.e(Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()), getMessage());
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public String getMessage() {
		return responseBody;
	}

	public int getStatusCode() {
		return statusCode.getValue();
	}

	public String getResponseBody() {
		return responseBody;
	}

	public String getAmazonDescription() {
		return "ACDResponseException Description";
	}
}
