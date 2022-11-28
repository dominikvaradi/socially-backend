package hu.dominikvaradi.sociallybackend.flows.comment.rest;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentResponseDto;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.dto.CommentUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.comment.service.CommentService;
import hu.dominikvaradi.sociallybackend.flows.comment.transformers.Comment2CommentResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.comment.transformers.CommentReaction2CommentReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.EmptyRestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageableRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionToggleRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.RestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

@SecurityRequirement(name = "BearerToken")
@RequiredArgsConstructor
@RestController
public class CommentController {
	private final CommentService commentService;

	@GetMapping("/comments/{commentId}")
	public ResponseEntity<RestApiResponseDto<CommentResponseDto>> findCommentByPublicId(@PathVariable(name = "commentId") UUID commentPublicId) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		CommentResponseDto responseData = createCommentResponseDtoFromComment(comment, currentUser);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<RestApiResponseDto<CommentResponseDto>> updateComment(@PathVariable(name = "commentId") UUID commentPublicId, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		Comment updatedComment = commentService.updateComment(comment, commentUpdateRequestDto);

		CommentResponseDto responseData = createCommentResponseDtoFromComment(updatedComment, currentUser);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<EmptyRestApiResponseDto> deleteComment(@PathVariable(name = "commentId") UUID commentPublicId) {
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		commentService.deleteComment(comment);

		return ResponseEntity.ok(new EmptyRestApiResponseDto());
	}

	@GetMapping("/comments/{commentId}/reactions")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<CommentReactionResponseDto>>> findAllReactionsByComment(@PathVariable(name = "commentId") UUID commentPublicId, @RequestParam(name = "reaction", required = false) Reaction reaction, @ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		Page<CommentReactionResponseDto> page = commentService.findAllReactionsByComment(comment, reaction, pageable)
				.map(CommentReaction2CommentReactionResponseDto::transform);

		PageResponseDto<CommentReactionResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PutMapping("/comments/{commentId}/reactions")
	public ResponseEntity<RestApiResponseDto<CommentResponseDto>> toggleReactionOnComment(@PathVariable(name = "commentId") UUID commentPublicId, @RequestBody ReactionToggleRequestDto reactionToggleRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Comment comment = commentService.findCommentByPublicId(commentPublicId);

		commentService.toggleReactionOnComment(comment, currentUser, reactionToggleRequestDto.getReaction());

		CommentResponseDto responseData = createCommentResponseDtoFromComment(comment, currentUser);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	private CommentResponseDto createCommentResponseDtoFromComment(Comment comment, User currentUser) {
		CommentResponseDto commentResponseDto = Comment2CommentResponseDtoTransformer.transform(comment);
		commentResponseDto.setReactionsCount(new ArrayList<>(commentService.findAllReactionCountsByComment(comment)));
		commentResponseDto.setCurrentUsersReaction(commentService.getUsersReactionByComment(currentUser, comment));

		return commentResponseDto;
	}
}
