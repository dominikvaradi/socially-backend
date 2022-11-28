package hu.dominikvaradi.sociallybackend.flows.post.transformers;

import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostReactionResponseDto;

public class PostReaction2PostReactionResponseDtoTransformer {
	private PostReaction2PostReactionResponseDtoTransformer() {
		/* Static class */
	}

	public static PostReactionResponseDto transform(PostReaction postReaction) {
		return PostReactionResponseDto.builder()
				.id(postReaction.getPublicId())
				.postId(postReaction.getPost().getPublicId())
				.userId(postReaction.getUser().getPublicId())
				.userFirstName(postReaction.getUser().getFirstName())
				.userLastName(postReaction.getUser().getLastName())
				.reaction(postReaction.getReaction())
				.build();
	}
}