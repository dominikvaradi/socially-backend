package hu.dominikvaradi.sociallybackend.flows.friendship.transformers;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestOutgoingResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;

import java.util.Objects;

public class Friendship2FriendRequestOutgoingResponseDtoTransformer {
	private Friendship2FriendRequestOutgoingResponseDtoTransformer() {
		/* Static class */
	}

	public static FriendRequestOutgoingResponseDto transform(Friendship friendship, User currentUser) {
		User addresseeUser = Objects.equals(friendship.getRequester(), currentUser) ? friendship.getAddressee() : friendship.getRequester();

		return FriendRequestOutgoingResponseDto.builder()
				.id(friendship.getPublicId())
				.addresseeUserId(addresseeUser.getPublicId())
				.addresseeUserName(addresseeUser.getName())
				.build();
	}
}