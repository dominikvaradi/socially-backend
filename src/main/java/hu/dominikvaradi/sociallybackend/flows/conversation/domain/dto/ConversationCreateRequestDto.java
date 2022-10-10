package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ConversationCreateRequestDto {
	private ConversationType type;
	private Set<UUID> memberUserIds;
}
