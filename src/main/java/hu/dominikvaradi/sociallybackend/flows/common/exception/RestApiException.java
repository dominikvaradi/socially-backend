package hu.dominikvaradi.sociallybackend.flows.common.exception;

import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {
	private final short httpStatusCode;

	public RestApiException(String message, short httpStatusCode) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}

	public RestApiException(String message, short httpStatusCode, Throwable cause) {
		super(message, cause);
		this.httpStatusCode = httpStatusCode;
	}
}
