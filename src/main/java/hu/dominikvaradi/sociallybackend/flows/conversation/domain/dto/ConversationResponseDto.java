package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
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
public class ConversationResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private ZonedDateTime lastMessageSent;

	@NotNull
	private ConversationType type;

	@NotNull
	private List<ConversationUserResponseDto> members;
}
