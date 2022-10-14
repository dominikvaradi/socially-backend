package hu.dominikvaradi.sociallybackend.flows.friendship.service;

import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_ENDED;
import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_REQUEST_ACCEPTED;
import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_REQUEST_SENT;

@RequiredArgsConstructor
@Transactional
@Service
public class FriendshipServiceImpl implements FriendshipService {
	private final FriendshipRepository friendshipRepository;

	@Override
	public Friendship findByPublicId(UUID friendshipPublicId) {
		return friendshipRepository.findByPublicId(friendshipPublicId)
				.orElseThrow(() -> new EntityNotFoundException("Friendship not found."));
	}

	@Override
	public Page<Friendship> findAllIncomingFriendRequestsOfUser(User user, Pageable pageable) {
		return friendshipRepository.findAllIncomingByUserOrderByStatusLastModifiedDesc(user, pageable);
	}

	@Override
	public Page<Friendship> findAllOutgoingFriendRequestsOfUser(User user, Pageable pageable) {
		return friendshipRepository.findAllOutgoingByUserOrderByStatusLastModifiedDesc(user, pageable);
	}

	@Override
	public Friendship createFriendRequest(User requesterUser, User addresseeUser) {
		Friendship friendship = friendshipRepository.findAllByRequesterAndAddressee(requesterUser, addresseeUser)
				.orElse(Friendship.builder()
						.requester(requesterUser)
						.addressee(addresseeUser)
						.lastStatusModifier(requesterUser)
						.status(FRIENDSHIP_REQUEST_SENT)
						.statusLastModified(Instant.now())
						.build());

		if (friendship.getStatus() == FRIENDSHIP_ENDED) {
			friendship.setStatus(FRIENDSHIP_REQUEST_SENT);
			friendship.setLastStatusModifier(addresseeUser);
			friendship.setStatusLastModified(Instant.now());
		}

		return friendshipRepository.save(friendship);
	}

	@Override
	public Friendship acceptFriendRequest(Friendship friendship, User user) {
		if (friendship.getStatus() == FRIENDSHIP_REQUEST_SENT) {
			friendship.setStatus(FRIENDSHIP_REQUEST_ACCEPTED);
			friendship.setLastStatusModifier(user);
			friendship.setStatusLastModified(Instant.now());
		}

		return friendshipRepository.save(friendship);
	}

	@Override
	public Friendship declineFriendRequest(Friendship friendship, User user) {
		if (friendship.getStatus() == FRIENDSHIP_REQUEST_SENT) {
			friendship.setStatus(FRIENDSHIP_ENDED);
			friendship.setLastStatusModifier(user);
			friendship.setStatusLastModified(Instant.now());
		}

		return friendshipRepository.save(friendship);
	}
}
