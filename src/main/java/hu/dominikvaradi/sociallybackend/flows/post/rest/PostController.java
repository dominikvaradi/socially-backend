package hu.dominikvaradi.sociallybackend.flows.post.rest;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.service.PostService;
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
public class PostController {
	private final PostService postService;

	@GetMapping("/api/posts")
	public ResponseEntity<Page<PostResponseDto>> findAllPostsOnCurrentUsersFeed(@ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/posts/{postId}")
	public ResponseEntity<PostResponseDto> findPostByPublicId(@PathVariable(name = "postId") UUID postPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PutMapping("/api/posts/{postId}")
	public ResponseEntity<PostResponseDto> updatePost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/api/posts/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable(name = "postId") UUID postPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/posts/{postId}/comments")
	public ResponseEntity<Page<CommentResponseDto>> findAllCommentsByPost(@PathVariable(name = "postId") UUID postPublicId, @ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/posts/{postId}/comments")
	public ResponseEntity<CommentResponseDto> createCommentOnPost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/posts/{postId}/reactions")
	public ResponseEntity<Page<PostReactionResponseDto>> findAllReactionsByPost(@PathVariable(name = "postId") UUID postPublicId, @ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/posts/{postId}/reactions")
	public ResponseEntity<PostReactionResponseDto> createReactionOnPost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody ReactionCreateRequestDto reactionCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/api/posts/{postId}/reactions/{reaction}")
	public ResponseEntity<PostReactionResponseDto> deleteReactionFromPost(@PathVariable(name = "postId") UUID postPublicId, @PathVariable(name = "reaction") Reaction reaction) {
		throw new NotImplementedException("REST method not implemented yet.");
	}
}
