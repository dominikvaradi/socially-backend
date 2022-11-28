package hu.dominikvaradi.sociallybackend.flows.conversation.transformers;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationResponseDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationUserResponseDto;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class Conversation2ConversationResponseDtoTransformer {
	private Conversation2ConversationResponseDtoTransformer() {
		/* Static Class */
	}

	public static ConversationResponseDto transform(Conversation conversation) {
		List<ConversationUserResponseDto> members = conversation.getUserConversations()
				.stream()
				.map(UserConversation2ConversationUserResponseDtoTransformer::transform)
				.collect(Collectors.toList());

		return ConversationResponseDto.builder()
				.id(conversation.getPublicId())
				.lastMessageSent(conversation.getLastMessageSent().atZone(ZoneId.systemDefault()))
				.type(conversation.getType())
				.members(members)
				.build();
	}
}