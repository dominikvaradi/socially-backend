package hu.dominikvaradi.sociallybackend.flows.comment.service;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CommentService {
	Comment createNewComment(UUID postPublicId, UUID userPublicId, CommentCreateDto commentCreateDto);

	Set<Comment> getCommentsByPost(UUID postPublicId);

	Comment updateComment(UUID commentPublicId, CommentUpdateDto commentUpdateDto);

	void deleteComment(UUID commentPublicId);

	CommentReaction addReactionToComment(UUID commentPublicId, UUID userPublicId, Reaction reaction);

	void deleteReactionFromComment(UUID commentPublicId, UUID userPublicId, Reaction reaction);

	Set<CommentReaction> getReactionsByComment(UUID commentPublicId);

	Map<Reaction, Long> getReactionsCountByComment(UUID commentPublicId);
}
