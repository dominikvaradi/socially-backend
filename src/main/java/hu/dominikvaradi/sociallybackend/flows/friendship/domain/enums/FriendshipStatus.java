package hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum FriendshipStatus {
	FRIENDSHIP_REQUEST_SENT,
	FRIENDSHIP_REQUEST_ACCEPTED,
	FRIENDSHIP_ENDED
}
