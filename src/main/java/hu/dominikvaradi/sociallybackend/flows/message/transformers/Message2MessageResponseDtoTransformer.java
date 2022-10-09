package hu.dominikvaradi.sociallybackend.flows.message.transformers;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageResponseDto;

import java.util.EnumMap;

public class Message2MessageResponseDtoTransformer {
	private Message2MessageResponseDtoTransformer() {
		/* Static Class */
	}

	public static MessageResponseDto transform(Message message) {
		EnumMap<Reaction, Long> emptyReactionCounts = new EnumMap<>(Reaction.class);
		for (Reaction reaction : Reaction.values()) {
			emptyReactionCounts.put(reaction, 0L);
		}

		return MessageResponseDto.builder()
				.id(message.getPublicId())
				.userId(message.getUser().getPublicId())
				.userName(message.getUser().getName())
				.conversationId(message.getConversation().getPublicId())
				.content(message.getContent())
				.created(message.getCreated())
				.reactionsCount(emptyReactionCounts)
				.build();
	}
}