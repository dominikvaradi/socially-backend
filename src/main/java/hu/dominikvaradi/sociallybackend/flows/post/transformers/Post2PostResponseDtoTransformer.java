package hu.dominikvaradi.sociallybackend.flows.post.transformers;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Post2PostResponseDtoTransformer {
	private Post2PostResponseDtoTransformer() {
		/* Static class */
	}

	public static PostResponseDto transform(Post post) {
		List<ReactionCountResponseDto> emptyReactionCountResponseDtoList = new ArrayList<>();
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
				.authorFirstName(post.getAuthor().getFirstName())
				.authorLastName(post.getAuthor().getLastName())
				.addresseeId(post.getAddressee().getPublicId())
				.addresseeFirstName(post.getAddressee().getFirstName())
				.addresseeLastName(post.getAddressee().getLastName())
				.created(post.getCreated().atZone(ZoneId.systemDefault()))
				.reactionsCount(emptyReactionCountResponseDtoList)
				.commentsCount(0)
				.build();
	}
}