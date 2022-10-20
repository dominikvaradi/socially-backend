package hu.dominikvaradi.sociallybackend.flows.security.exception;

public class JwtAuthenticationFailedException extends RuntimeException {
	public JwtAuthenticationFailedException(String message) {
		super(message);
	}

	public JwtAuthenticationFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
