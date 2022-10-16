package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
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
public class ConversationResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private ZonedDateTime lastMessageSent;

	@NotNull
	private ConversationType type;

	@NotNull
	private Set<ConversationUserResponseDto> members;
}
