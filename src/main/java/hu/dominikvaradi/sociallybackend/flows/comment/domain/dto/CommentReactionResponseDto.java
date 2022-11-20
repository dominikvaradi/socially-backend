package hu.dominikvaradi.sociallybackend.flows.comment.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CommentReactionResponseDto {
	@NotNull
	private UUID commentId;

	@NotNull
	private UUID userId;

	@NotNull
	private String userFirstName;

	@NotNull
	private String userLastName;

	@NotNull
	private Reaction reaction;
}