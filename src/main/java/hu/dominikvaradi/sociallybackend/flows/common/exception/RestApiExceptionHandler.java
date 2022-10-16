package hu.dominikvaradi.sociallybackend.flows.common.exception;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.EmptyRestApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class RestApiExceptionHandler {
	@ExceptionHandler(value = {RestApiException.class})
	public ResponseEntity<EmptyRestApiResponseDto> handleRestApiRequestException(RestApiException e) {
		return createResponseEntity(e.getHttpStatusCode(), singletonList(e.getMessage()));
	}

	@ExceptionHandler(value = {UsernameNotFoundException.class})
	public ResponseEntity<EmptyRestApiResponseDto> handleUsernameNotFoundException(UsernameNotFoundException e) {
		return createResponseEntity(UNAUTHORIZED.value(), singletonList(e.getMessage()));
	}

	@ExceptionHandler(value = {BadCredentialsException.class})
	public ResponseEntity<EmptyRestApiResponseDto> handleBadCredentialsException(BadCredentialsException e) {
		return createResponseEntity(UNAUTHORIZED.value(), singletonList("WRONG_PASSWORD"));
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<EmptyRestApiResponseDto> handleException(Exception e) {
		log.error("Internal server error happened: " + e.getMessage(), e);

		return createResponseEntity(INTERNAL_SERVER_ERROR.value(), singletonList(e.getMessage()));
	}

	private ResponseEntity<EmptyRestApiResponseDto> createResponseEntity(int httpStatusCode, List<String> messages) {
		EmptyRestApiResponseDto responseData = EmptyRestApiResponseDto.builder()
				.httpStatusCode(httpStatusCode)
				.messages(messages)
				.build();

		return ResponseEntity
				.status(responseData.getHttpStatusCode())
				.body(responseData);
	}
}
