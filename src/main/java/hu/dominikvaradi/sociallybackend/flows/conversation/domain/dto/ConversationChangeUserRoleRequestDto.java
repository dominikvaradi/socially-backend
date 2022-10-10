package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ConversationChangeUserRoleRequestDto {
	private UserConversationRole role;
}
