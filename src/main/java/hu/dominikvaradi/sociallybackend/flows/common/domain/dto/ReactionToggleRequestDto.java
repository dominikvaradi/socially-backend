package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ReactionToggleRequestDto {
	@NotNull
	private Reaction reaction;
}
