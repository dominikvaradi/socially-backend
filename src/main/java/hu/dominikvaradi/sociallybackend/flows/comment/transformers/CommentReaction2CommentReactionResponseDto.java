package hu.dominikvaradi.sociallybackend.flows.comment.transformers;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentReactionResponseDto;

public class CommentReaction2CommentReactionResponseDto {
	private CommentReaction2CommentReactionResponseDto() {
		/* Static class */
	}

	public static CommentReactionResponseDto transform(CommentReaction commentReaction) {
		return CommentReactionResponseDto.builder()
				.commentId(commentReaction.getComment().getPublicId())
				.userId(commentReaction.getUser().getPublicId())
				.userFirstName(commentReaction.getUser().getFirstName())
				.userLastName(commentReaction.getUser().getLastName())
				.reaction(commentReaction.getReaction())
				.build();
	}
}