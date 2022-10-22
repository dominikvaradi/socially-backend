package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ConversationCreateRequestDto {
	@NotNull
	private ConversationType type;

	@NotEmpty
	private Set<UUID> memberUserIds;
}
