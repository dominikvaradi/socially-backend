package hu.dominikvaradi.sociallybackend.flows.comment.service;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;
import java.util.UUID;

public interface CommentService {
	@PostAuthorize("isAuthenticationUserEqualsOrFriendOf(returnObject.post.addressee)")
	Comment findCommentByPublicId(UUID commentPublicId);

	@PreAuthorize("authentication.principal.user == user && isUserEqualsOrFriendOf(#user, #post.addressee) ")
	Comment createComment(Post post, User user, CommentCreateRequestDto commentCreateRequestDto);
	
	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	Page<Comment> findAllCommentsByPost(Post post, Pageable pageable);

	@PreAuthorize("authentication.principal.user == #comment.user")
	Comment updateComment(Comment comment, CommentUpdateRequestDto commentUpdateRequestDto);

	@PreAuthorize("authentication.principal.user == #comment.user || authentication.principal.user == #comment.post.addressee")
	void deleteComment(Comment comment);

	@PreAuthorize("authentication.principal.user == user && isUserEqualsOrFriendOf(#user, #comment.post.addressee)")
	CommentReaction addReactionToComment(Comment comment, User user, Reaction reaction);

	@PreAuthorize("authentication.principal.user == user && isUserEqualsOrFriendOf(#user, #comment.post.addressee)")
	void deleteReactionFromComment(Comment comment, User user, Reaction reaction);

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#comment.post.addressee)")
	Page<CommentReaction> findAllReactionsByComment(Comment comment, Pageable pageable);

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#comment.post.addressee)")
	Set<ReactionCountResponseDto> findAllReactionCountsByComment(Comment comment);
}
