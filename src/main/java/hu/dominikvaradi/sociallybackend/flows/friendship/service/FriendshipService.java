package hu.dominikvaradi.sociallybackend.flows.friendship.service;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

public interface FriendshipService {
	@PostAuthorize("returnObject.requester == authentication.principal.user || returnObject.addressee == authentication.principal.user")
	Friendship findByPublicId(UUID friendshipPublicId);

	@PreAuthorize("#user == authentication.principal.user")
	Page<Friendship> findAllIncomingFriendRequestsOfUser(User user, Pageable pageable);

	@PreAuthorize("#user == authentication.principal.user")
	Page<Friendship> findAllOutgoingFriendRequestsOfUser(User user, Pageable pageable);

	@PreAuthorize("#requesterUser == authentication.principal.user && isUserFriendOf(#requesterUser, #addresseeUser)")
	Friendship createFriendRequest(User requesterUser, User addresseeUser);

	@PreAuthorize("#user == authentication.principal.user && #friendship.addressee == #user")
	Friendship acceptFriendRequest(Friendship friendship, User user);

	@PreAuthorize("#user == authentication.principal.user && (#friendship.requester == #user || #friendship.addressee == #user)")
	Friendship declineFriendRequest(Friendship friendship, User user);
}