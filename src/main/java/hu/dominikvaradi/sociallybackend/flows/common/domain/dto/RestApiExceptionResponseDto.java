package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RestApiExceptionResponseDto {
	private short httpStatusCode;
	private String message;
}
