package hu.dominikvaradi.sociallybackend.flows.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InternalServerErrorException extends RestApiException {
	public InternalServerErrorException(String message) {
		super(message, (short) 500);
	}

	public InternalServerErrorException(String message, Throwable cause) {
		super(message, (short) 500, cause);

		log.error("Internal server error: " + message, cause);
	}
}
