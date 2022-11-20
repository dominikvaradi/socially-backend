package hu.dominikvaradi.sociallybackend.flows.comment.transformers;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class Comment2CommentResponseDtoTransformer {
	private Comment2CommentResponseDtoTransformer() {
		/* Static class */
	}

	public static CommentResponseDto transform(Comment comment) {
		Set<ReactionCountResponseDto> emptyReactionCountResponseDtoList = new HashSet<>();
		for (Reaction reaction : Reaction.values()) {
			emptyReactionCountResponseDtoList.add(ReactionCountResponseDto.builder()
					.reaction(reaction)
					.count(0L)
					.build());
		}

		return CommentResponseDto.builder()
				.id(comment.getPublicId())
				.content(comment.getContent())
				.postId(comment.getPost().getPublicId())
				.authorId(comment.getUser().getPublicId())
				.authorFirstName(comment.getUser().getFirstName())
				.authorLastName(comment.getUser().getLastName())
				.created(comment.getCreated().atZone(ZoneId.systemDefault()))
				.reactionsCount(emptyReactionCountResponseDtoList)
				.build();
	}
}