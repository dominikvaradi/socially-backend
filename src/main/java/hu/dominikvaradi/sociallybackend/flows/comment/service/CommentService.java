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
	@PostAuthorize("authentication.principal.user == returnObject.post.addressee || isUserFriendOf(returnObject.post.addressee)")
	Comment findCommentByPublicId(UUID commentPublicId);

	@PreAuthorize("user == #post.addressee || areUsersFriends(#user, #post.addressee)")
	Comment createComment(Post post, User user, CommentCreateRequestDto commentCreateRequestDto);

	@PreAuthorize("authentication.principal.user == #post.addressee || isUserFriendOf(#post.addressee)")
	Page<Comment> findAllCommentsByPost(Post post, Pageable pageable);

	@PreAuthorize("authentication.principal.user == #comment.user")
	Comment updateComment(Comment comment, CommentUpdateRequestDto commentUpdateRequestDto);

	@PreAuthorize("authentication.principal.user == #comment.user || authentication.principal.user == #comment.post.addressee")
	void deleteComment(Comment comment);

	@PreAuthorize("#user == #comment.post.addressee || areUsersFriends(#user, #comment.post.addressee)")
	CommentReaction addReactionToComment(Comment comment, User user, Reaction reaction);

	@PreAuthorize("#user == #comment.post.addressee || areUsersFriends(#user, #comment.post.addressee)")
	void deleteReactionFromComment(Comment comment, User user, Reaction reaction);

	@PreAuthorize("authentication.principal.user == #comment.post.addressee || isUserFriendOf(#comment.post.addressee)")
	Page<CommentReaction> findAllReactionsByComment(Comment comment, Pageable pageable);

	@PreAuthorize("authentication.principal.user == #comment.post.addressee || isUserFriendOf(#comment.post.addressee)")
	Set<ReactionCountResponseDto> findAllReactionCountsByComment(Comment comment);
}
