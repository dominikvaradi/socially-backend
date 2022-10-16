package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ConversationAddUsersRequestDto {
	@NotEmpty
	private Set<UUID> memberUserIds;
}
