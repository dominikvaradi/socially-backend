package hu.dominikvaradi.sociallybackend.flows.comment.service;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentRepository;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityConflictException;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final CommentReactionRepository commentReactionRepository;

	@Override
	public Comment findCommentByPublicId(UUID commentPublicId) {
		return commentRepository.findByPublicId(commentPublicId)
				.orElseThrow(() -> new EntityNotFoundException("COMMENT_NOT_FOUND"));
	}

	@Override
	public Comment createComment(Post post, User user, CommentCreateRequestDto commentCreateRequestDto) {
		Comment newComment = Comment.builder()
				.content(commentCreateRequestDto.getContent())
				.post(post)
				.user(user)
				.build();

		return commentRepository.save(newComment);
	}

	@Override
	public Page<Comment> findAllCommentsByPost(Post post, Pageable pageable) {
		return commentRepository.findByPostOrderByCreatedDesc(post, pageable);
	}

	@Override
	public Comment updateComment(Comment comment, CommentUpdateRequestDto commentUpdateRequestDto) {
		comment.setContent(commentUpdateRequestDto.getContent());

		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(Comment comment) {
		commentRepository.delete(comment);
	}

	@Override
	public CommentReaction addReactionToComment(Comment comment, User user, Reaction reaction) {
		if (commentReactionRepository.findByUserAndCommentAndReaction(user, comment, reaction).isPresent()) {
			throw new EntityConflictException("REACTION_ALREADY_EXISTS_ON_COMMENT_MADE_BY_USER");
		}

		CommentReaction newCommentReaction = CommentReaction.builder()
				.reaction(reaction)
				.user(user)
				.comment(comment)
				.build();

		return commentReactionRepository.save(newCommentReaction);
	}

	@Override
	public void deleteReactionFromComment(Comment comment, User user, Reaction reaction) {
		CommentReaction commentReaction = commentReactionRepository.findByUserAndCommentAndReaction(user, comment, reaction)
				.orElseThrow(() -> new EntityNotFoundException("REACTION_NOT_FOUND"));

		commentReactionRepository.delete(commentReaction);
	}

	@Override
	public Page<CommentReaction> findAllReactionsByComment(Comment comment, Pageable pageable) {
		return commentReactionRepository.findAllByCommentOrderByUserNameAsc(comment, pageable);
	}

	@Override
	public EnumMap<Reaction, Long> findAllReactionCountsByComment(Comment comment) {
		EnumMap<Reaction, Long> reactionCount = new EnumMap<>(Reaction.class);

		for (Reaction reaction : Reaction.values()) {
			reactionCount.put(reaction, commentReactionRepository.countByCommentAndReaction(comment, reaction));
		}

		return reactionCount;
	}
}
