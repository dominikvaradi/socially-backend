package hu.dominikvaradi.sociallybackend.flows.conversation.transformers;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationResponseDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationUserResponseDto;

import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

public class Conversation2ConversationResponseDtoTransformer {
	private Conversation2ConversationResponseDtoTransformer() {
		/* Static Class */
	}

	public static ConversationResponseDto transform(Conversation conversation) {
		Set<ConversationUserResponseDto> members = conversation.getUserConversations()
				.stream()
				.map(UserConversation2ConversationUserResponseDtoTransformer::transform)
				.collect(Collectors.toSet());

		return ConversationResponseDto.builder()
				.id(conversation.getPublicId())
				.lastMessageSent(conversation.getLastMessageSent().atZone(ZoneId.systemDefault()))
				.type(conversation.getType())
				.members(members)
				.build();
	}
}