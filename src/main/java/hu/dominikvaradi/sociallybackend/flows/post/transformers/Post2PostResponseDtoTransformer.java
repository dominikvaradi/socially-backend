package hu.dominikvaradi.sociallybackend.flows.post.transformers;

import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;

public class Post2PostResponseDtoTransformer {
	private Post2PostResponseDtoTransformer() {
		/* Static class */
	}

	public static PostResponseDto transform(Post post) {
		return PostResponseDto.builder()
				.id(post.getPublicId())
				.header(post.getHeader())
				.content(post.getContent())
				.authorId(post.getAuthor().getPublicId())
				.authorName(post.getAuthor().getName())
				.addresseeId(post.getAddressee().getPublicId())
				.addresseeName(post.getAddressee().getName())
				.created(post.getCreated())
				.build();
	}
}