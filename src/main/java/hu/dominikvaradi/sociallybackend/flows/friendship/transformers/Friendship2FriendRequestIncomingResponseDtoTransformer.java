package hu.dominikvaradi.sociallybackend.flows.friendship.transformers;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestIncomingResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;

import java.util.Objects;

public class Friendship2FriendRequestIncomingResponseDtoTransformer {
	private Friendship2FriendRequestIncomingResponseDtoTransformer() {
		/* Static class */
	}

	public static FriendRequestIncomingResponseDto transform(Friendship friendship, User currentUser) {
		User requesterUser = Objects.equals(friendship.getRequester(), currentUser) ? friendship.getAddressee() : friendship.getRequester();

		return FriendRequestIncomingResponseDto.builder()
				.id(friendship.getPublicId())
				.requesterUserId(requesterUser.getPublicId())
				.requesterUserFirstName(requesterUser.getFirstName())
				.requesterUserLastName(requesterUser.getLastName())
				.build();
	}
}