package hu.dominikvaradi.sociallybackend.flows.common.exception;

public class EntityUnprocessableException extends RestApiException {
	public EntityUnprocessableException(String message) {
		super(message, (short) 422);
	}

	public EntityUnprocessableException(String message, Throwable cause) {
		super(message, (short) 422, cause);
	}
}
