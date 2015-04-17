package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;
import org.yetiz.lib.acd.ResponseCode;
import org.yetiz.lib.utils.Log;

import java.io.IOException;

/**
 * Created by yeti on 4/14/15.
 */
public class ACDResponseException extends RuntimeException {
	protected ResponseCode.Error statusCode;
	private String responseBody = "";

	public ACDResponseException(Response response) {
		try {
			responseBody = response.hasResponseBody() ? response.getResponseBody() : response.getStatusText();
		} catch (IOException e) {
			throw new BadContentException(e.getMessage());
		}
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
