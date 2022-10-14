package hu.dominikvaradi.sociallybackend.flows.common.exception;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.RestApiExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestApiExceptionHandler {
	@ExceptionHandler(value = {RestApiException.class})
	public ResponseEntity<RestApiExceptionResponseDto> handleRestApiRequestException(RestApiException e) {
		RestApiExceptionResponseDto responseData = RestApiExceptionResponseDto.builder()
				.httpStatusCode(e.getHttpStatusCode())
				.message(e.getMessage())
				.build();

		return ResponseEntity
				.status(responseData.getHttpStatusCode())
				.body(responseData);
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<RestApiExceptionResponseDto> handleRestApiRequestException(Exception e) {
		RestApiExceptionResponseDto responseData = RestApiExceptionResponseDto.builder()
				.httpStatusCode((short) 500)
				.message(e.getMessage())
				.build();

		log.error("Internal server error happened: " + e.getMessage(), e);

		return ResponseEntity
				.status(responseData.getHttpStatusCode())
				.body(responseData);
	}
}
