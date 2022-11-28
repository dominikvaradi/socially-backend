package hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Getter
public class ConversationCreateRequestDto {
	@NotEmpty
	private List<UUID> memberUserIds;
}
