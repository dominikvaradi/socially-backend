package hu.dominikvaradi.sociallybackend.flows.post.transformers;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class Post2PostResponseDtoTransformer {
	private Post2PostResponseDtoTransformer() {
		/* Static class */
	}

	public static PostResponseDto transform(Post post) {
		Set<ReactionCountResponseDto> emptyReactionCountResponseDtoList = new HashSet<>();
		for (Reaction reaction : Reaction.values()) {
			emptyReactionCountResponseDtoList.add(ReactionCountResponseDto.builder()
					.reaction(reaction)
					.count(0L)
					.build());
		}

		return PostResponseDto.builder()
				.id(post.getPublicId())
				.header(post.getHeader())
				.content(post.getContent())
				.authorId(post.getAuthor().getPublicId())
				.authorName(post.getAuthor().getName())
				.addresseeId(post.getAddressee().getPublicId())
				.addresseeName(post.getAddressee().getName())
				.created(post.getCreated().atZone(ZoneId.systemDefault()))
				.reactionsCount(emptyReactionCountResponseDtoList)
				.commentsCount(0)
				.build();
	}
}