package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Getter;

@Getter
public class ReactionCreateRequestDto {
	private Reaction reaction;
}
