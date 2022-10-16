package hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@Setter
public class FriendRequestIncomingResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private UUID requesterUserId;

	@NotNull
	private String requesterUserName;
}
