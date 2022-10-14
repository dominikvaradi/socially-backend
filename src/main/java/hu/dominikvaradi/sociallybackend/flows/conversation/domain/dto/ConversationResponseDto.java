package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ConversationResponseDto {
	private UUID id;
	private ZonedDateTime lastMessageSent;
	private ConversationType type;
	private Set<ConversationUserResponseDto> members;
}
