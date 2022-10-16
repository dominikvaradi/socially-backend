package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableRequestDto {
	@Parameter(description = "Zero-based page index (0..N)", schema = @Schema(defaultValue = "20", type = "integer"))
	private int page = 0;

	@Parameter(description = "The size of the page to be returned", schema = @Schema(defaultValue = "20", type = "integer"))
	private int size = 20;
}
