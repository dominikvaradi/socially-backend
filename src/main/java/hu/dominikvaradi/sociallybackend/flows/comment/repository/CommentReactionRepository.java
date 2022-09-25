package hu.dominikvaradi.sociallybackend.flows.comment.repository;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
	Page<CommentReaction> findAllByCommentOrderByUserNameAsc(Comment comment, Pageable pageable);

	Optional<CommentReaction> findByUserAndCommentAndReaction(User user, Comment comment, Reaction reaction);

	long countByCommentAndReaction(Comment comment, Reaction reaction);
}