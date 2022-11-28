package hu.dominikvaradi.sociallybackend.flows.friendship.service;

import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityUnprocessableException;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_ENDED;
import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_REQUEST_ACCEPTED;
import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_REQUEST_SENT;

@RequiredArgsConstructor
@Transactional
@Service
public class FriendshipService {
	private final FriendshipRepository friendshipRepository;

	@PostAuthorize("returnObject.requester == authentication.principal.user || returnObject.addressee == authentication.principal.user")
	public Friendship findByPublicId(UUID friendshipPublicId) {
		return friendshipRepository.findByPublicId(friendshipPublicId)
				.orElseThrow(() -> new EntityNotFoundException("FRIENDSHIP_NOT_FOUND"));
	}

	@PreAuthorize("#user == authentication.principal.user")
	public Page<Friendship> findAllIncomingFriendRequestsOfUser(User user, Pageable pageable) {
		return friendshipRepository.findAllIncomingByUserOrderByStatusLastModifiedDesc(user, pageable);
	}

	@PreAuthorize("#user == authentication.principal.user")
	public Page<Friendship> findAllOutgoingFriendRequestsOfUser(User user, Pageable pageable) {
		return friendshipRepository.findAllOutgoingByUserOrderByStatusLastModifiedDesc(user, pageable);
	}

	@PreAuthorize("#requesterUser == authentication.principal.user && !isUserFriendOf(#requesterUser, #addresseeUser)")
	public Friendship createFriendRequest(User requesterUser, User addresseeUser) {
		if (Objects.equals(requesterUser, addresseeUser)) {
			throw new EntityUnprocessableException("CANNOT_SEND_FRIEND_REQUEST_TO_SELF");
		}

		Friendship friendship = friendshipRepository.findByRequesterAndAddressee(requesterUser, addresseeUser)
				.orElse(Friendship.builder()
						.requester(requesterUser)
						.addressee(addresseeUser)
						.lastStatusModifier(requesterUser)
						.status(FRIENDSHIP_REQUEST_SENT)
						.statusLastModified(Instant.now())
						.build());

		if (friendship.getStatus() == FRIENDSHIP_ENDED) {
			friendship.setStatus(FRIENDSHIP_REQUEST_SENT);
			friendship.setLastStatusModifier(requesterUser);
			friendship.setStatusLastModified(Instant.now());
		}

		return friendshipRepository.save(friendship);
	}

	@PreAuthorize("#user == authentication.principal.user && (#friendship.requester == #user || #friendship.addressee == #user) && #friendship.lastStatusModifier != #user")
	public Friendship acceptFriendRequest(Friendship friendship, User user) {
		if (friendship.getStatus() == FRIENDSHIP_REQUEST_SENT) {
			friendship.setStatus(FRIENDSHIP_REQUEST_ACCEPTED);
			friendship.setLastStatusModifier(user);
			friendship.setStatusLastModified(Instant.now());
		}

		return friendshipRepository.save(friendship);
	}

	@PreAuthorize("#user == authentication.principal.user && (#friendship.addressee == #user || #friendship.requester == #user)")
	public Friendship deleteFriendship(Friendship friendship, User user) {
		if (friendship.getStatus() == FRIENDSHIP_REQUEST_ACCEPTED) {
			friendship.setStatus(FRIENDSHIP_ENDED);
			friendship.setLastStatusModifier(user);
			friendship.setStatusLastModified(Instant.now());
		}

		return friendshipRepository.save(friendship);
	}

	@PreAuthorize("#user == authentication.principal.user && (#friendship.requester == #user || #friendship.addressee == #user)")
	public Friendship declineFriendRequest(Friendship friendship, User user) {
		if (friendship.getStatus() == FRIENDSHIP_REQUEST_SENT) {
			friendship.setStatus(FRIENDSHIP_ENDED);
			friendship.setLastStatusModifier(user);
			friendship.setStatusLastModified(Instant.now());
		}

		return friendshipRepository.save(friendship);
	}

	@PreAuthorize("authentication.principal.user == #user1 || authentication.principal.user == #user2")
	public Optional<Friendship> findFriendshipBetweenUsers(User user1, User user2) {
		return friendshipRepository.findByRequesterAndAddressee(user1, user2);
	}
}
