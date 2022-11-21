package hu.dominikvaradi.sociallybackend.flows.comment.service;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentRepository;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final CommentReactionRepository commentReactionRepository;

	@PostAuthorize("isAuthenticationUserEqualsOrFriendOf(returnObject.post.addressee)")
	public Comment findCommentByPublicId(UUID commentPublicId) {
		return commentRepository.findByPublicId(commentPublicId)
				.orElseThrow(() -> new EntityNotFoundException("COMMENT_NOT_FOUND"));
	}

	@PreAuthorize("authentication.principal.user == user && isUserEqualsOrFriendOf(#user, #post.addressee)")
	public Comment createComment(Post post, User user, CommentCreateRequestDto commentCreateRequestDto) {
		Comment newComment = Comment.builder()
				.content(commentCreateRequestDto.getContent())
				.post(post)
				.user(user)
				.build();

		return commentRepository.save(newComment);
	}

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	public Page<Comment> findAllCommentsByPost(Post post, Pageable pageable) {
		return commentRepository.findByPostOrderByCreatedDesc(post, pageable);
	}

	@PreAuthorize("authentication.principal.user == #comment.user")
	public Comment updateComment(Comment comment, CommentUpdateRequestDto commentUpdateRequestDto) {
		comment.setContent(commentUpdateRequestDto.getContent());

		return commentRepository.save(comment);
	}

	@PreAuthorize("authentication.principal.user == #comment.user || authentication.principal.user == #comment.post.addressee")
	public void deleteComment(Comment comment) {
		commentRepository.delete(comment);
	}

	@PreAuthorize("authentication.principal.user == user && isUserEqualsOrFriendOf(#user, #comment.post.addressee)")
	public CommentReaction toggleReactionOnComment(Comment comment, User user, Reaction reaction) {
		Optional<CommentReaction> existingCommentReaction = commentReactionRepository.findByUserAndComment(user, comment);
		if (existingCommentReaction.isPresent()) {
			commentReactionRepository.delete(existingCommentReaction.get());

			if (existingCommentReaction.get().getReaction() == reaction) {
				return null;
			}
		}

		CommentReaction newCommentReaction = CommentReaction.builder()
				.reaction(reaction)
				.user(user)
				.comment(comment)
				.build();

		return commentReactionRepository.save(newCommentReaction);
	}

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#comment.post.addressee)")
	public Page<CommentReaction> findAllReactionsByComment(Comment comment, Pageable pageable) {
		return commentReactionRepository.findAllByCommentOrderByUserLastNameAsc(comment, pageable);
	}

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#comment.post.addressee)")
	public Set<ReactionCountResponseDto> findAllReactionCountsByComment(Comment comment) {
		Set<ReactionCountResponseDto> reactionCounts = new HashSet<>();

		for (Reaction reaction : Reaction.values()) {
			reactionCounts.add(ReactionCountResponseDto.builder()
					.reaction(reaction)
					.count(commentReactionRepository.countByCommentAndReaction(comment, reaction))
					.build());
		}

		return reactionCounts;
	}

	@PreAuthorize("authentication.principal.user == user && isAuthenticationUserEqualsOrFriendOf(#comment.post.addressee)")
	public Reaction getUsersReactionByComment(User user, Comment comment) {
		Optional<CommentReaction> commentReaction = commentReactionRepository.findByUserAndComment(user, comment);
		if (commentReaction.isEmpty()) {
			return null;
		}

		return commentReaction.get().getReaction();
	}
}
