package hu.dominikvaradi.sociallybackend.flows.comment.rest;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.service.CommentService;
import hu.dominikvaradi.sociallybackend.flows.comment.transformers.Comment2CommentResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.comment.transformers.CommentReaction2CommentReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CommentController {
	private final CommentService commentService;

	@GetMapping("/api/comments/{commentId}")
	public ResponseEntity<CommentResponseDto> findCommentByPublicId(@PathVariable(name = "commentId") UUID commentPublicId) {
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		CommentResponseDto responseData = Comment2CommentResponseDtoTransformer.transform(comment);
		responseData.setReactionsCount(commentService.findAllReactionCountsByComment(comment));

		return ResponseEntity.ok(responseData);
	}

	@PutMapping("/api/comments/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable(name = "commentId") UUID commentPublicId, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		Comment updatedComment = commentService.updateComment(comment, commentUpdateRequestDto);

		CommentResponseDto responseData = Comment2CommentResponseDtoTransformer.transform(updatedComment);
		responseData.setReactionsCount(commentService.findAllReactionCountsByComment(comment));

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/api/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable(name = "commentId") UUID commentPublicId) {
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		commentService.deleteComment(comment);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/api/comments/{commentId}/reactions")
	public ResponseEntity<Page<CommentReactionResponseDto>> findAllReactionsByComment(@PathVariable(name = "commentId") UUID commentPublicId, @ParameterObject Pageable pageable) {
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		Page<CommentReactionResponseDto> responseData = commentService.findAllReactionsByComment(comment, pageable)
				.map(CommentReaction2CommentReactionResponseDto::transform);

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/comments/{commentId}/reactions")
	public ResponseEntity<CommentReactionResponseDto> createReactionOnComment(@PathVariable(name = "commentId") UUID commentPublicId, @RequestBody ReactionCreateRequestDto reactionCreateRequestDto) {
		Comment comment = commentService.findCommentByPublicId(commentPublicId);
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.

		CommentReaction createdCommentReaction = commentService.addReactionToComment(comment, currentUser, reactionCreateRequestDto.getReaction());

		CommentReactionResponseDto responseData = CommentReaction2CommentReactionResponseDto.transform(createdCommentReaction);

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/api/comments/{commentId}/reactions/{reaction}")
	public ResponseEntity<Void> deleteReactionFromComment(@PathVariable(name = "commentId") UUID commentPublicId, @PathVariable(name = "reaction") Reaction reaction) {
		Comment comment = commentService.findCommentByPublicId(commentPublicId);
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.

		commentService.deleteReactionFromComment(comment, currentUser, reaction);

		return ResponseEntity.noContent().build();
	}
}
