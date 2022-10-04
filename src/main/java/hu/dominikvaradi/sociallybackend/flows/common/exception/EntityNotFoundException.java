package hu.dominikvaradi.sociallybackend.flows.common.exception;

public class EntityNotFoundException extends RestApiException {
	public EntityNotFoundException(String message) {
		super(message, (short) 404);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, (short) 404, cause);
	}
}
