package hu.dominikvaradi.sociallybackend.flows.comment.rest;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.service.CommentService;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import liquibase.repackaged.org.apache.commons.lang3.NotImplementedException;
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
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PutMapping("/api/comments/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable(name = "commentId") UUID commentPublicId, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/api/comments/{commentId}")
	public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable(name = "commentId") UUID commentPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/comments/{commentId}/reactions")
	public ResponseEntity<Page<CommentReactionResponseDto>> findAllReactionsByComment(@PathVariable(name = "commentId") UUID commentPublicId, @ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/comments/{commentId}/reactions")
	public ResponseEntity<CommentReactionResponseDto> createReactionOnComment(@PathVariable(name = "commentId") UUID commentPublicId, @RequestBody ReactionCreateRequestDto reactionCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/api/comments/{commentId}/reactions/{reaction}")
	public ResponseEntity<CommentReactionResponseDto> deleteReactionFromComment(@PathVariable(name = "commentId") UUID commentPublicId, @PathVariable(name = "reaction") Reaction reaction) {
		throw new NotImplementedException("REST method not implemented yet.");
	}
}
