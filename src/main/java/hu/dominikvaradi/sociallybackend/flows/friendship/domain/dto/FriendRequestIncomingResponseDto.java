package hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class FriendRequestIncomingResponseDto {
	private UUID id;
	private UUID requesterUserId;
	private String requesterUserName;
}
