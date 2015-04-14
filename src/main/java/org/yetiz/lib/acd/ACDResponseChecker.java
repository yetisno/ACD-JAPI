package org.yetiz.lib.acd;

import com.ning.http.client.Response;
import org.yetiz.lib.acd.exception.*;

/**
 * Created by yeti on 2015/4/14.
 */
public class ACDResponseChecker {
	public static void check(Response response) {
		switch (response.getStatusCode()) {
			case 400:
				throw new BadParameterException(response);
			case 401:
				throw new InvalidAuthTokenException(response);
			case 403:
				throw new RequestForbiddenException(response);
			case 404:
				throw new ResourceNotFoundException(response);
			case 405:
				throw new MethodNotAllowedException(response);
			case 409:
				throw new RequestConflictException(response);
			case 411:
				throw new ContentLengthNotExistedException(response);
			case 412:
				throw new PreconditionFailedException(response);
			case 429:
				throw new RequestRateLimitedException(response);
			case 500:
				throw new InternalServerErrorException(response);
			case 503:
				throw new ServiceUnavailableException(response);
		}
	}
}