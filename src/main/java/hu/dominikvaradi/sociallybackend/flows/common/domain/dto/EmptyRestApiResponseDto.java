package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmptyRestApiResponseDto {
	@Builder.Default
	@NotNull
	private int httpStatusCode = OK.value();

	@Builder.Default
	@NotNull
	private List<String> messages = new ArrayList<>();
}
