package hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FriendRequestCreateRequestDto {
	UUID addresseeUserId;
}
