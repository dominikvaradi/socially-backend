package hu.dominikvaradi.sociallybackend.flows.post.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@Setter
public class PostReactionResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private UUID postId;

	@NotNull
	private UUID userId;

	@NotNull
	private String userFirstName;

	@NotNull
	private String userLastName;

	@NotNull
	private Reaction reaction;
}
