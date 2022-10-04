package hu.dominikvaradi.sociallybackend.flows.comment.service;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.EnumMap;
import java.util.UUID;

public interface CommentService {
	Comment findCommentByPublicId(UUID commentPublicId);

	Comment createComment(Post post, User user, CommentCreateRequestDto commentCreateRequestDto);

	Page<Comment> findAllCommentsByPost(Post post, Pageable pageable);

	Comment updateComment(Comment comment, CommentUpdateRequestDto commentUpdateRequestDto);

	void deleteComment(Comment comment);

	CommentReaction addReactionToComment(Comment comment, User user, Reaction reaction);

	void deleteReactionFromComment(Comment comment, User user, Reaction reaction);

	Page<CommentReaction> findAllReactionsByComment(Comment comment, Pageable pageable);

	EnumMap<Reaction, Long> findAllReactionCountsByComment(Comment comment);
}
