package hu.dominikvaradi.sociallybackend.flows.post.transformers;

import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostReactionResponseDto;

public class PostReaction2PostReactionResponseDtoTransformer {
	private PostReaction2PostReactionResponseDtoTransformer() {
		/* Static class */
	}

	public static PostReactionResponseDto transform(PostReaction postReaction) {
		return PostReactionResponseDto.builder()
				.postId(postReaction.getPost().getPublicId())
				.userId(postReaction.getUser().getPublicId())
				.reaction(postReaction.getReaction())
				.build();
	}
}