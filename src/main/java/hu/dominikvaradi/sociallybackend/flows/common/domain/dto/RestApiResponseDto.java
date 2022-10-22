package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.OK;

@Getter
@Setter
@NoArgsConstructor
public class RestApiResponseDto<T> extends EmptyRestApiResponseDto {
	private T data;

	@Builder(builderMethodName = "childBuilder")
	public RestApiResponseDto(int httpStatusCode, List<String> messages, T data) {
		super(httpStatusCode, messages);

		this.data = data;
	}

	public static <T> RestApiResponseDto<T> buildFromDataWithoutMessages(T data) {
		return RestApiResponseDto.<T>childBuilder()
				.data(data)
				.httpStatusCode(OK.value())
				.messages(emptyList())
				.build();
	}
}
