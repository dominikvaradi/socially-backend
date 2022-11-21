package hu.dominikvaradi.sociallybackend.flows.post.domain.dto;

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
public class PostResponseDto {
	@NotNull
	private UUID id;

	private String header;

	@NotNull
	private String content;

	@NotNull
	private UUID authorId;

	@NotNull
	private String authorFirstName;

	@NotNull
	private String authorLastName;

	@NotNull
	private UUID addresseeId;

	@NotNull
	private String addresseeFirstName;

	@NotNull
	private String addresseeLastName;

	@NotNull
	private ZonedDateTime created;

	@NotNull
	private List<ReactionCountResponseDto> reactionsCount;

	@NotNull
	private long commentsCount;
}
