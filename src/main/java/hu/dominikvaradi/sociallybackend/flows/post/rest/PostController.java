package hu.dominikvaradi.sociallybackend.flows.post.rest;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.service.CommentService;
import hu.dominikvaradi.sociallybackend.flows.comment.transformers.Comment2CommentResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.EmptyRestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageableRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionToggleRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.RestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.service.PostService;
import hu.dominikvaradi.sociallybackend.flows.post.transformers.Post2PostResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.post.transformers.PostReaction2PostReactionResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.security.domain.JwtUserDetails;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

@SecurityRequirement(name = "BearerToken")
@RequiredArgsConstructor
@RestController
public class PostController {
	private final PostService postService;
	private final CommentService commentService;

	@GetMapping("/posts")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<PostResponseDto>>> findAllPostsOnCurrentUsersFeed(@ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();

		Page<PostResponseDto> page = postService.findAllPostsForUsersFeed(currentUser, pageable)
				.map(p -> {
					PostResponseDto transformed = Post2PostResponseDtoTransformer.transform(p);

					transformed.setReactionsCount(new ArrayList<>(postService.findAllReactionCountsByPost(p)));

					transformed.setCommentsCount(postService.findCommentCountByPost(p));
					transformed.setCurrentUsersReaction(postService.getUsersReactionByPost(currentUser, p));

					return transformed;
				});

		PageResponseDto<PostResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<RestApiResponseDto<PostResponseDto>> findPostByPublicId(@PathVariable(name = "postId") UUID postPublicId) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Post post = postService.findPostByPublicId(postPublicId);

		PostResponseDto responseData = createPostResponseDtoFromPost(post, currentUser);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PutMapping("/posts/{postId}")
	public ResponseEntity<RestApiResponseDto<PostResponseDto>> updatePost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Post post = postService.findPostByPublicId(postPublicId);

		Post updatedPost = postService.updatePost(post, postUpdateRequestDto);

		PostResponseDto responseData = createPostResponseDtoFromPost(updatedPost, currentUser);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<EmptyRestApiResponseDto> deletePost(@PathVariable(name = "postId") UUID postPublicId) {
		Post post = postService.findPostByPublicId(postPublicId);

		postService.deletePost(post);

		return ResponseEntity.ok(new EmptyRestApiResponseDto());
	}

	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<CommentResponseDto>>> findAllCommentsByPost(@PathVariable(name = "postId") UUID postPublicId, @ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Post post = postService.findPostByPublicId(postPublicId);

		Page<CommentResponseDto> page = commentService.findAllCommentsByPost(post, pageable)
				.map(c -> {
					CommentResponseDto transformed = Comment2CommentResponseDtoTransformer.transform(c);
					transformed.setReactionsCount(new ArrayList<>(commentService.findAllReactionCountsByComment(c)));
					transformed.setCurrentUsersReaction(commentService.getUsersReactionByComment(currentUser, c));

					return transformed;
				});

		PageResponseDto<CommentResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<RestApiResponseDto<CommentResponseDto>> createCommentOnPost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Post post = postService.findPostByPublicId(postPublicId);

		Comment createdComment = commentService.createComment(post, currentUser, commentCreateRequestDto);

		CommentResponseDto responseData = Comment2CommentResponseDtoTransformer.transform(createdComment);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@GetMapping("/posts/{postId}/reactions")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<PostReactionResponseDto>>> findAllReactionsByPost(@PathVariable(name = "postId") UUID postPublicId, @RequestParam(name = "reaction", required = false) Reaction reaction, @ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		Post post = postService.findPostByPublicId(postPublicId);

		Page<PostReactionResponseDto> page = postService.findAllReactionsByPost(post, reaction, pageable)
				.map(PostReaction2PostReactionResponseDtoTransformer::transform);

		PageResponseDto<PostReactionResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PutMapping("/posts/{postId}/reactions")
	public ResponseEntity<RestApiResponseDto<PostResponseDto>> toggleReactionOnPost(@PathVariable(name = "postId") UUID postPublicId, @RequestBody ReactionToggleRequestDto reactionToggleRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Post post = postService.findPostByPublicId(postPublicId);

		postService.toggleReactionOnPost(post, currentUser, reactionToggleRequestDto.getReaction());

		PostResponseDto responseData = createPostResponseDtoFromPost(post, currentUser);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	private PostResponseDto createPostResponseDtoFromPost(Post post, User currentUser) {
		PostResponseDto postResponseDto = Post2PostResponseDtoTransformer.transform(post);
		postResponseDto.setReactionsCount(new ArrayList<>(postService.findAllReactionCountsByPost(post)));
		postResponseDto.setCommentsCount(postService.findCommentCountByPost(post));
		postResponseDto.setCurrentUsersReaction(postService.getUsersReactionByPost(currentUser, post));

		return postResponseDto;
	}
}
