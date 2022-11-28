package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ConversationChangeUserRoleRequestDto {
	@NotNull
	private UserConversationRole role;
}
