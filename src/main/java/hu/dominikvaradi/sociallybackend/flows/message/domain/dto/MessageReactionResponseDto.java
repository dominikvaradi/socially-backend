package hu.dominikvaradi.sociallybackend.flows.message.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class MessageReactionResponseDto {
	private UUID messageId;
	private UUID userId;
	private String userName;
	private Reaction reaction;
}
