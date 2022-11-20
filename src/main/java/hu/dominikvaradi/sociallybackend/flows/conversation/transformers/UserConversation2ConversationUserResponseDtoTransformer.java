package hu.dominikvaradi.sociallybackend.flows.conversation.transformers;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationUserResponseDto;

public class UserConversation2ConversationUserResponseDtoTransformer {
	private UserConversation2ConversationUserResponseDtoTransformer() {
		/* Static Class */
	}

	public static ConversationUserResponseDto transform(UserConversation userConversation) {
		return ConversationUserResponseDto.builder()
				.conversationId(userConversation.getConversation().getPublicId())
				.userId(userConversation.getUser().getPublicId())
				.userFirstName(userConversation.getUser().getFirstName())
				.userLastName(userConversation.getUser().getLastName())
				.userConversationRole(userConversation.getUserRole())
				.build();
	}
}