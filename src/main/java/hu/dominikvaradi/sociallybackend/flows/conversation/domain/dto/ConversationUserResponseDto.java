package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@Getter
public class ConversationUserResponseDto {
	private UUID conversationId;
	private UUID userId;
	private String userName;
	private UserConversationRole userConversationRole;
}
