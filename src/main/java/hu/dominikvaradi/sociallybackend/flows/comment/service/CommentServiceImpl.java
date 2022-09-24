package hu.dominikvaradi.sociallybackend.flows.comment.service;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateDto;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentRepository;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.service.PostService;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final CommentReactionRepository commentReactionRepository;
	private final PostService postService;
	private final UserService userService;

	@Override
	public Comment createNewComment(UUID postPublicId, UUID userPublicId, CommentCreateDto commentCreateDto) {
		Post post = postService.findPostByPublicId(postPublicId).orElseThrow(); // TODO REST Exception 404
		User user = userService.findUserByPublicId(userPublicId).orElseThrow(); // TODO REST Exception 404

		Comment newComment = Comment.builder()
				.content(commentCreateDto.getContent())
				.post(post)
				.user(user)
				.build();

		return commentRepository.save(newComment);
	}

	@Override
	public Set<Comment> getCommentsByPost(UUID postPublicId) {
		return commentRepository.findByPostPublicIdOrderByCreatedDesc(postPublicId);
	}

	@Override
	public Comment updateComment(UUID commentPublicId, CommentUpdateDto commentUpdateDto) {
		Comment comment = commentRepository.findByPublicId(commentPublicId).orElseThrow(); // TODO REST Exception 404

		if (!Objects.equals(comment.getPublicId(), commentUpdateDto.getId())) {
			throw new RuntimeException(); // TODO REST Exception - bad request, rossz id-t rakott a request bodyba.
		}

		comment.setContent(commentUpdateDto.getContent());

		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(UUID commentPublicId) {
		Comment comment = commentRepository.findByPublicId(commentPublicId).orElseThrow(); // TODO REST Exception 404

		commentRepository.delete(comment);
	}

	@Override
	public CommentReaction addReactionToComment(UUID commentPublicId, UUID userPublicId, Reaction reaction) {
		Comment comment = commentRepository.findByPublicId(commentPublicId).orElseThrow(); // TODO REST Exception 404
		User user = userService.findUserByPublicId(userPublicId).orElseThrow(); // TODO REST Exception 404

		if (commentReactionRepository.findByUserPublicIdAndCommentPublicIdAndReaction(userPublicId, commentPublicId, reaction).isPresent()) {
			throw new RuntimeException();
			// TODO REST Exception - már létezik az entity, conflict lenne.
		}

		CommentReaction newCommentReaction = CommentReaction.builder()
				.reaction(reaction)
				.user(user)
				.comment(comment)
				.build();

		return commentReactionRepository.save(newCommentReaction);
	}

	@Override
	public void deleteReactionFromComment(UUID commentPublicId, UUID userPublicId, Reaction reaction) {
		CommentReaction commentReaction = commentReactionRepository.findByUserPublicIdAndCommentPublicIdAndReaction(userPublicId, commentPublicId, reaction)
				.orElseThrow(); // TODO REST Exception 404

		commentReactionRepository.delete(commentReaction);
	}

	@Override
	public Set<CommentReaction> getReactionsByComment(UUID commentPublicId) {
		return commentReactionRepository.findByCommentPublicIdOrderByUserLastNameAsc(commentPublicId);
	}

	@Override
	public Map<Reaction, Long> getReactionsCountByComment(UUID commentPublicId) {
		Map<Reaction, Long> reactionCount = new EnumMap<>(Reaction.class);

		for (Reaction reaction : Reaction.values()) {
			reactionCount.put(reaction, commentReactionRepository.countByCommentPublicIdAndReaction(commentPublicId, reaction));
		}

		return reactionCount;
	}
}
