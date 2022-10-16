package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
public class ReactionCountResponseDto {
	@NotNull
	private Reaction reaction;

	@NotNull
	private long count;
}
