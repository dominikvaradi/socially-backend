package hu.dominikvaradi.sociallybackend.flows.user.rest;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageableRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.RestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.service.PostService;
import hu.dominikvaradi.sociallybackend.flows.post.transformers.Post2PostResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.security.domain.JwtUserDetails;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserProfileResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserSearchResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
import hu.dominikvaradi.sociallybackend.flows.user.transformers.User2UserProfileResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.user.transformers.User2UserSearchResponseDtoTransformer;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SecurityRequirement(name = "BearerToken")
@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserService userService;
	private final PostService postService;

	@GetMapping("/api/users")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<UserSearchResponseDto>>> findAllUsersByName(@RequestParam(name = "name", defaultValue = "") String name, @ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		Page<UserSearchResponseDto> page = userService.findAllUsersByName(name, pageable)
				.map(User2UserSearchResponseDtoTransformer::transform);

		PageResponseDto<UserSearchResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@GetMapping("/api/users/{userId}")
	public ResponseEntity<RestApiResponseDto<UserProfileResponseDto>> findUserByPublicId(@PathVariable(name = "userId") UUID userPublicId) {
		User user = userService.findUserByPublicId(userPublicId);

		UserProfileResponseDto responseData = User2UserProfileResponseDtoTransformer.transform(user);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PutMapping("/api/users/{userId}")
	public ResponseEntity<RestApiResponseDto<UserProfileResponseDto>> updateUser(@PathVariable(name = "userId") UUID userPublicId, @RequestBody UserUpdateRequestDto userUpdateDto) {
		User user = userService.findUserByPublicId(userPublicId);

		User updatedUser = userService.updateUser(user, userUpdateDto);

		UserProfileResponseDto responseData = User2UserProfileResponseDtoTransformer.transform(updatedUser);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@GetMapping("/api/users/{userId}/friends")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<UserSearchResponseDto>>> findAllFriendsOfUser(@PathVariable(name = "userId") UUID userPublicId, @ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		User user = userService.findUserByPublicId(userPublicId);

		Page<UserSearchResponseDto> page = userService.findAllFriendsByUser(user, pageable)
				.map(User2UserSearchResponseDtoTransformer::transform);

		PageResponseDto<UserSearchResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@GetMapping("/api/users/{userId}/posts")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<PostResponseDto>>> findAllPostsOfUser(@PathVariable(name = "userId") UUID userPublicId, @ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		User user = userService.findUserByPublicId(userPublicId);

		Page<PostResponseDto> page = postService.findAllPostsOnUsersTimeline(user, pageable)
				.map(p -> {
					PostResponseDto transformed = Post2PostResponseDtoTransformer.transform(p);
					transformed.setReactionsCount(postService.findAllReactionCountsByPost(p));
					transformed.setCommentsCount(postService.findCommentCountByPost(p));

					return transformed;
				});

		PageResponseDto<PostResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PostMapping("/api/users/{userId}/posts")
	public ResponseEntity<RestApiResponseDto<PostResponseDto>> createPostOnUser(@PathVariable(name = "userId") UUID userPublicId, @RequestBody PostCreateRequestDto postCreateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		User addresseeUser = userService.findUserByPublicId(userPublicId);

		Post createdPost = postService.createPost(currentUser, addresseeUser, postCreateRequestDto);

		PostResponseDto responseData = Post2PostResponseDtoTransformer.transform(createdPost);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}
}
