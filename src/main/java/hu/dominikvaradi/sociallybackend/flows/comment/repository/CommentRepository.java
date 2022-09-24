package hu.dominikvaradi.sociallybackend.flows.comment.repository;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Optional<Comment> findByPublicId(UUID publicId);

	Set<Comment> findByPostPublicIdOrderByCreatedDesc(UUID publicId);
}