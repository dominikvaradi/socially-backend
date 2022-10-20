package hu.dominikvaradi.sociallybackend.flows.message.transformers;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageResponseDto;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class Message2MessageResponseDtoTransformer {
	private Message2MessageResponseDtoTransformer() {
		/* Static Class */
	}

	public static MessageResponseDto transform(Message message) {
		Set<ReactionCountResponseDto> emptyReactionCountResponseDtoList = new HashSet<>();
		for (Reaction reaction : Reaction.values()) {
			emptyReactionCountResponseDtoList.add(ReactionCountResponseDto.builder()
					.reaction(reaction)
					.count(0L)
					.build());
		}

		return MessageResponseDto.builder()
				.id(message.getPublicId())
				.userId(message.getUser().getPublicId())
				.userName(message.getUser().getName())
				.conversationId(message.getConversation().getPublicId())
				.content(message.getContent())
				.created(message.getCreated().atZone(ZoneId.systemDefault()))
				.reactionsCount(emptyReactionCountResponseDtoList)
				.build();
	}
}