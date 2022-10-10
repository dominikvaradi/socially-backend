package hu.dominikvaradi.sociallybackend.flows.post.rest;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.service.CommentService;
import hu.dominikvaradi.sociallybackend.flows.comment.transformers.Comment2CommentResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.service.PostService;
import hu.dominikvaradi.sociallybackend.flows.post.transformers.Post2PostResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.post.transformers.PostReaction2PostReactionResponseDtoTransformer;
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
public class PostController {
	private final PostService postService;
	private final CommentService commentService;

	@GetMapping("/api/posts")
	public ResponseEntity<Page<PostResponseDto>> findAllPostsOnCurrentUsersFeed(@ParameterObject Pageable pageable) {
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.

		Page<PostResponseDto> responseData = postService.findAllPostsForUsersFeed(currentUser, pageable)
				.map(Post2PostResponseDtoTransformer::transform);

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/api/posts/{postId}")
	public ResponseEntity<PostResponseDto> findPostByPublicId(@PathVariable(name = "postId") UUID postPublicId) {
		Post post = postService.findPostByPublicId(postPublicId);

		PostResponseDto responseData = Post2PostResponseDtoTransformer.transform(post);
		responseData.setReactionsCount(postService.findAllReactionCountsByPost(post));
		responseData.setCommentsCount(postService.findCommentCountByPost(post));

		return ResponseEntity.ok(responseData);
	}

	@PutMapping("/api/posts/{postId}")
	public ResponseEntity<PostResponseDto> updatePost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
		Post post = postService.findPostByPublicId(postPublicId);

		Post updatedPost = postService.updatePost(post, postUpdateRequestDto);

		PostResponseDto responseData = Post2PostResponseDtoTransformer.transform(updatedPost);
		responseData.setReactionsCount(postService.findAllReactionCountsByPost(updatedPost));
		responseData.setCommentsCount(postService.findCommentCountByPost(updatedPost));

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/api/posts/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable(name = "postId") UUID postPublicId) {
		Post post = postService.findPostByPublicId(postPublicId);

		postService.deletePost(post);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/api/posts/{postId}/comments")
	public ResponseEntity<Page<CommentResponseDto>> findAllCommentsByPost(@PathVariable(name = "postId") UUID postPublicId, @ParameterObject Pageable pageable) {
		Post post = postService.findPostByPublicId(postPublicId);

		Page<CommentResponseDto> responseData = commentService.findAllCommentsByPost(post, pageable)
				.map(c -> {
					CommentResponseDto transformed = Comment2CommentResponseDtoTransformer.transform(c);
					transformed.setReactionsCount(commentService.findAllReactionCountsByComment(c));

					return transformed;
				});

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/posts/{postId}/comments")
	public ResponseEntity<CommentResponseDto> createCommentOnPost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.
		Post post = postService.findPostByPublicId(postPublicId);

		Comment createdComment = commentService.createComment(post, currentUser, commentCreateRequestDto);

		CommentResponseDto responseData = Comment2CommentResponseDtoTransformer.transform(createdComment);

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/api/posts/{postId}/reactions")
	public ResponseEntity<Page<PostReactionResponseDto>> findAllReactionsByPost(@PathVariable(name = "postId") UUID postPublicId, @ParameterObject Pageable pageable) {
		Post post = postService.findPostByPublicId(postPublicId);

		Page<PostReactionResponseDto> responseData = postService.findAllReactionsByPost(post, pageable)
				.map(PostReaction2PostReactionResponseDtoTransformer::transform);

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/posts/{postId}/reactions")
	public ResponseEntity<PostReactionResponseDto> createReactionOnPost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody ReactionCreateRequestDto reactionCreateRequestDto) {
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.
		Post post = postService.findPostByPublicId(postPublicId);

		PostReaction createdPostReaction = postService.addReactionToPost(post, currentUser, reactionCreateRequestDto.getReaction());

		PostReactionResponseDto responseData = PostReaction2PostReactionResponseDtoTransformer.transform(createdPostReaction);

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/api/posts/{postId}/reactions/{reaction}")
	public ResponseEntity<Void> deleteReactionFromPost(@PathVariable(name = "postId") UUID postPublicId, @PathVariable(name = "reaction") Reaction reaction) {
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.
		Post post = postService.findPostByPublicId(postPublicId);
		
		postService.deleteReactionFromPost(post, currentUser, reaction);

		return ResponseEntity.noContent().build();
	}
}
