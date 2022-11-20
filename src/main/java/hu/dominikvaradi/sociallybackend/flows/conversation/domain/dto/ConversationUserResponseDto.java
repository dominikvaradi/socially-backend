package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Setter
@Getter
public class ConversationUserResponseDto {
	@NotNull
	private UUID conversationId;

	@NotNull
	private UUID userId;

	@NotNull
	private String userFirstName;

	@NotNull
	private String userLastName;

	@NotNull
	private UserConversationRole userConversationRole;
}
