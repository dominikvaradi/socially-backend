package hu.dominikvaradi.sociallybackend.flows.comment.repository;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
	Set<CommentReaction> findByCommentPublicIdOrderByUserLastNameAsc(UUID publicId);

	Optional<CommentReaction> findByUserPublicIdAndCommentPublicIdAndReaction(UUID publicId, UUID publicId1, Reaction reaction);

	long countByCommentPublicIdAndReaction(UUID publicId, Reaction reaction);
}