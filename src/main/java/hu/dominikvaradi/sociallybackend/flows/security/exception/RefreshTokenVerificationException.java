package hu.dominikvaradi.sociallybackend.flows.security.exception;

import hu.dominikvaradi.sociallybackend.flows.common.exception.RestApiException;

public class RefreshTokenVerificationException extends RestApiException {
	public RefreshTokenVerificationException(String message) {
		super(message, (short) 401);
	}

	public RefreshTokenVerificationException(String message, Throwable cause) {
		super(message, (short) 401, cause);
	}
}
