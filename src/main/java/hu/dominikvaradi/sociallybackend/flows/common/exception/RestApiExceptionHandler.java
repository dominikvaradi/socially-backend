package hu.dominikvaradi.sociallybackend.flows.common.exception;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.RestApiExceptionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {
	@ExceptionHandler(value = {RestApiException.class})
	public ResponseEntity<RestApiExceptionResponseDto> handleRestApiRequestException(RestApiException e) {
		RestApiExceptionResponseDto responseData = RestApiExceptionResponseDto.builder()
				.httpStatusCode(e.getHttpStatusCode())
				.message(e.getMessage())
				.build();

		return ResponseEntity
				.status(e.getHttpStatusCode())
				.body(responseData);
	}
}
