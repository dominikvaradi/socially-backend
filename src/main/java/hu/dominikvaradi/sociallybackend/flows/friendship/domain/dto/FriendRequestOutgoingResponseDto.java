package hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class FriendRequestOutgoingResponseDto {
	private UUID id;
	private UUID addresseeUserId;
	private String addresseeUserName;
}
