package hu.dominikvaradi.sociallybackend.flows.comment.transformers;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;

import java.util.EnumMap;

public class Comment2CommentResponseDtoTransformer {
	private Comment2CommentResponseDtoTransformer() {
		/* Static class */
	}

	public static CommentResponseDto transform(Comment comment) {
		EnumMap<Reaction, Long> emptyReactionCounts = new EnumMap<>(Reaction.class);
		for (Reaction reaction : Reaction.values()) {
			emptyReactionCounts.put(reaction, 0L);
		}

		return CommentResponseDto.builder()
				.id(comment.getPublicId())
				.content(comment.getContent())
				.postId(comment.getPost().getPublicId())
				.authorId(comment.getUser().getPublicId())
				.authorName(comment.getUser().getName())
				.created(comment.getCreated())
				.reactionsCount(emptyReactionCounts)
				.build();
	}
}