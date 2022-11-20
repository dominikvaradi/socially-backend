package hu.dominikvaradi.sociallybackend.flows.message.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class MessageResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private UUID userId;

	@NotNull
	private String userFirstName;

	@NotNull
	private String userLastName;

	@NotNull
	private UUID conversationId;

	@NotNull
	private String content;

	@NotNull
	private ZonedDateTime created;

	@NotNull
	private Set<ReactionCountResponseDto> reactionsCount;
}
