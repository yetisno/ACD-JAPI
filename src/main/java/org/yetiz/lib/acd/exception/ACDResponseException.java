package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;
import org.yetiz.lib.utils.Log;

import java.io.IOException;

/**
 * Created by yeti on 4/14/15.
 */
public class ACDResponseException extends RuntimeException {
	private int statusCode;
	private String responseBody = "";

	public ACDResponseException(Response response) {
		statusCode = response.getStatusCode();
		try {
			responseBody = response.hasResponseBody() ? response.getResponseBody() : response.getStatusText();
		} catch (IOException e) {
			throw new BadContentException(e.getMessage());
		}
		Log.e(getMessage());
	}

	@Override
	public String getMessage() {
		return responseBody;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public String getAmazonDescription(){
		return "ACDResponseException Description";
	}
}
