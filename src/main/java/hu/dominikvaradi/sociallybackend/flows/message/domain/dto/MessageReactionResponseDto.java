package hu.dominikvaradi.sociallybackend.flows.message.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@Setter
public class MessageReactionResponseDto {
	@NotNull
	private UUID messageId;

	@NotNull
	private UUID userId;

	@NotNull
	private String userName;

	@NotNull
	private Reaction reaction;
}
