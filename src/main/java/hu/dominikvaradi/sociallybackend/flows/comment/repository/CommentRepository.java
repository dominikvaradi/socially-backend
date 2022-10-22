package hu.dominikvaradi.sociallybackend.flows.comment.repository;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Optional<Comment> findByPublicId(UUID publicId);

	Page<Comment> findByPostOrderByCreatedDesc(Post post, Pageable pageable);

	long countByPost(Post post);

	@Modifying
	@Query(value = "truncate table comments restart identity cascade", nativeQuery = true)
	void truncate();
}