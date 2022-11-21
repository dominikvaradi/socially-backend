package hu.dominikvaradi.sociallybackend.flows.comment.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CommentResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private String content;

	@NotNull
	private UUID postId;

	@NotNull
	private UUID authorId;

	@NotNull
	private String authorFirstName;

	private String authorLastName;

	@NotNull
	private ZonedDateTime created;

	@NotNull
	private List<ReactionCountResponseDto> reactionsCount;
}
