package hu.dominikvaradi.sociallybackend.flows.common.exception;

public class EntityConflictException extends RestApiException {
	public EntityConflictException(String message) {
		super(message, (short) 409);
	}

	public EntityConflictException(String message, Throwable cause) {
		super(message, (short) 409, cause);
	}
}
