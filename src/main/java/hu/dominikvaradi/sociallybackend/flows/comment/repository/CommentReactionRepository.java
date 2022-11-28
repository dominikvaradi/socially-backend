package hu.dominikvaradi.sociallybackend.flows.comment.repository;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
	Page<CommentReaction> findAllByCommentOrderByUserLastNameAsc(Comment comment, Pageable pageable);

	Page<CommentReaction> findAllByCommentAndReactionOrderByUserLastNameAsc(Comment comment, Reaction reaction, Pageable pageable);

	Optional<CommentReaction> findByUserAndComment(User user, Comment comment);

	long countByCommentAndReaction(Comment comment, Reaction reaction);

	@Modifying
	@Query(value = "truncate table comment_reactions restart identity cascade", nativeQuery = true)
	void truncate();
}