package hu.dominikvaradi.sociallybackend.flows.message.transformers;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageResponseDto;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Message2MessageResponseDtoTransformer {
	private Message2MessageResponseDtoTransformer() {
		/* Static Class */
	}

	public static MessageResponseDto transform(Message message) {
		List<ReactionCountResponseDto> emptyReactionCountResponseDtoList = new ArrayList<>();
		for (Reaction reaction : Reaction.values()) {
			emptyReactionCountResponseDtoList.add(ReactionCountResponseDto.builder()
					.reaction(reaction)
					.count(0L)
					.build());
		}

		return MessageResponseDto.builder()
				.id(message.getPublicId())
				.userId(message.getUser().getPublicId())
				.userFirstName(message.getUser().getFirstName())
				.userLastName(message.getUser().getLastName())
				.conversationId(message.getConversation().getPublicId())
				.content(message.getContent())
				.created(message.getCreated().atZone(ZoneId.systemDefault()))
				.reactionsCount(emptyReactionCountResponseDtoList)
				.build();
	}
}