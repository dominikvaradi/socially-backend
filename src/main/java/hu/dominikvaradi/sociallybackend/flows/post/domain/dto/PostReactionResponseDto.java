package hu.dominikvaradi.sociallybackend.flows.post.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class PostReactionResponseDto {
	private UUID postId;
	private UUID userId;
	private String userName;
	private Reaction reaction;
}
