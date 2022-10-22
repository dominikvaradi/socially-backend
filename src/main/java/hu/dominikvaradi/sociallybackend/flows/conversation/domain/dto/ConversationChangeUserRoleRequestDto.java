package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
public class ConversationChangeUserRoleRequestDto {
	@NotNull
	private UserConversationRole role;
}
