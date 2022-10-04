package hu.dominikvaradi.sociallybackend.flows.friendship.repository;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
	Optional<Friendship> findByPublicId(UUID friendshipPublicId);

	@Query("select f from Friendship f where (f.requester = ?1 and f.addressee = ?2) or (f.requester = ?2 and f.addressee = ?1)")
	Optional<Friendship> findAllByRequesterAndAddressee(User requesterUser, User addresseeUser);

	@Query("select f from Friendship f where (f.requester = ?1 or f.addressee = ?1) and f.status = 'FRIENDSHIP_REQUEST_SENT' and f.lastStatusModifier = ?1 order by f.statusLastModified DESC")
	Page<Friendship> findAllOutgoingByUserOrderByStatusLastModifiedDesc(User user, Pageable pageable);

	@Query("select f from Friendship f where (f.requester = ?1 or f.addressee = ?1) and f.status = 'FRIENDSHIP_REQUEST_SENT' and f.lastStatusModifier <> ?1 order by f.statusLastModified DESC")
	Page<Friendship> findAllIncomingByUserOrderByStatusLastModifiedDesc(User user, Pageable pageable);

	@Query("select f from Friendship f where (f.requester = ?1 or f.addressee = ?1) and f.status = 'FRIENDSHIP_REQUEST_ACCEPTED'")
	Set<Friendship> findAllAcceptedByUser(User user);

	@Query("select f from Friendship f where (f.requester = ?1 or f.addressee = ?1) and f.status = 'FRIENDSHIP_REQUEST_ACCEPTED'")
	Page<Friendship> findAllAcceptedByUser(User user, Pageable pageable);
}