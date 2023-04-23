package org.yetiz.lib.acd;

import org.asynchttpclient.Response;
import org.yetiz.lib.acd.exception.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeti on 4/17/15.
 */
public class ResponseCode {

	private static Map<Integer, Class<?>> errorList = new HashMap<>();

	static {
		errorList.put(ResponseCode.Error.BAD_PARAMETER.getValue(), BadParameterException.class);
		errorList.put(ResponseCode.Error.INVALID_AUTH_TOKEN.getValue(), InvalidAuthTokenException.class);
		errorList.put(ResponseCode.Error.REQUEST_FORBIDDEN.getValue(), RequestForbiddenException.class);
		errorList.put(ResponseCode.Error.RESOURCE_NOT_FOUND.getValue(), ResourceNotFoundException.class);
		errorList.put(ResponseCode.Error.METHOD_NOT_ALLOWED.getValue(), MethodNotAllowedException.class);
		errorList.put(ResponseCode.Error.REQUEST_CONFLICT.getValue(), RequestConflictException.class);
		errorList.put(ResponseCode.Error.CONTENT_LENGTH_NOT_EXIST.getValue(), ContentLengthNotExistedException.class);
		errorList.put(ResponseCode.Error.PRECONDITION_FAILED.getValue(), PreconditionFailedException.class);
		errorList.put(ResponseCode.Error.REQUEST_RATE_LIMITED.getValue(), RequestRateLimitedException.class);
		errorList.put(ResponseCode.Error.INTERNAL_SERVER_ERROR.getValue(), InternalServerErrorException.class);
		errorList.put(ResponseCode.Error.SERVICE_UNAVAILABLE.getValue(), ServiceUnavailableException.class);
	}

	public static void check(Response response) {
		if (errorList.containsKey(response.getStatusCode())) {
			ACDResponseException exception = null;
			try {
				exception = ((ACDResponseException) errorList.get(response.getStatusCode())
					.getConstructor(Response.class).newInstance(response));
			} catch (Throwable t) {
			}
			throw exception;
		}
	}

	public enum Error {
		BAD_PARAMETER(400, "Bad input parameter. Error message should indicate which one and why."),
		INVALID_AUTH_TOKEN(401, "The client passed in the invalid Auth token. Client should refresh the token and " +
			"then try again."),
		REQUEST_FORBIDDEN(403, "\t1. Customer doesn’t exist.\n" +
			"\t2. Application not registered.\n" +
			"\t3. Application try to access to properties not belong to an App.\n" +
			"\t4. Application try to trash/purge root node.\n" +
			"\t5. Application try to update contentProperties.\n" +
			"\t6. Operation is blocked (for third-party apps)."),
		RESOURCE_NOT_FOUND(404, "Resource not found."),
		METHOD_NOT_ALLOWED(405, "The resource doesn't support the specified HTTP verb."),
		REQUEST_CONFLICT(409, "Conflict."),
		CONTENT_LENGTH_NOT_EXIST(411, "The Content-Length header was not specified."),
		PRECONDITION_FAILED(412, "Precondition failed."),
		REQUEST_RATE_LIMITED(429, "Too many request for rate limiting."),
		INTERNAL_SERVER_ERROR(500, "Servers are not working as expected. The request is probably valid but needs to " +
			"be requested again later."),
		SERVICE_UNAVAILABLE(503, "Service Unavailable.");


		private final int value;
		private final String description;

		Error(int value, String description) {
			this.value = value;
			this.description = description;
		}

		public int getValue() {
			return value;
		}

		public String getDescription() {
			return description;
		}
	}
}
