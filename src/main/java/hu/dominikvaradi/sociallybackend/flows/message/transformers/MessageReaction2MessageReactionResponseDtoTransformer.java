package hu.dominikvaradi.sociallybackend.flows.message.transformers;

import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageReactionResponseDto;

public class MessageReaction2MessageReactionResponseDtoTransformer {
	private MessageReaction2MessageReactionResponseDtoTransformer() {
		/* Static Class */
	}

	public static MessageReactionResponseDto transform(MessageReaction messageReaction) {
		return MessageReactionResponseDto.builder()
				.id(messageReaction.getPublicId())
				.messageId(messageReaction.getMessage().getPublicId())
				.userId(messageReaction.getUser().getPublicId())
				.userFirstName(messageReaction.getUser().getFirstName())
				.userLastName(messageReaction.getUser().getLastName())
				.reaction(messageReaction.getReaction())
				.build();
	}
}