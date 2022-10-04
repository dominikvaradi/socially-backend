package hu.dominikvaradi.sociallybackend.flows.friendship.service;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FriendshipService {
	Friendship findByPublicId(UUID friendshipPublicId);

	Page<Friendship> findAllIncomingFriendRequestsOfUser(User user, Pageable pageable);

	Page<Friendship> findAllOutgoingFriendRequestsOfUser(User user, Pageable pageable);

	Friendship createFriendRequest(User requesterUser, User addresseeUser);

	Friendship acceptFriendRequest(Friendship friendship, User user);

	Friendship declineFriendRequest(Friendship friendship, User user);
}