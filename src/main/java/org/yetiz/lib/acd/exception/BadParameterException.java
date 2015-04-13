package org.yetiz.lib.acd.exception;

import com.ning.http.client.Response;

import java.io.IOException;

/**
 * Created by yeti on 2015/4/13.
 */
public class BadParameterException extends RuntimeException {
	/**
	 * Constructs a new runtime exception with {@code null} as its
	 * detail message.  The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	private Response response;

	public BadParameterException(Response response) {
		this.response = response;
	}

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this {@code Throwable} instance
	 * (which may be {@code null}).
	 */
	@Override
	public String getMessage() {
		try {
			return response.hasResponseBody() ? response.getResponseBody() : response.getStatusText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.getStatusText();
	}

	public int getStatusCode() {
		return response.getStatusCode();
	}
}
