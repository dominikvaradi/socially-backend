package hu.dominikvaradi.sociallybackend.flows.post.repository;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
	Set<PostReaction> findByPostPublicIdOrderByUserLastNameAsc(UUID postPublicId);

	Optional<PostReaction> findByUserPublicIdAndPostPublicIdAndReaction(UUID userPublicId, UUID postPublicId, Reaction reaction);

	long countByPostPublicIdAndReaction(UUID publicId, Reaction reaction);
}