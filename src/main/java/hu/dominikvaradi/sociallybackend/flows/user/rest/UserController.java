package hu.dominikvaradi.sociallybackend.flows.user.rest;

import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;
import hu.dominikvaradi.sociallybackend.flows.post.service.PostService;
import hu.dominikvaradi.sociallybackend.flows.post.transformers.Post2PostResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserProfileResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserSearchResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
import hu.dominikvaradi.sociallybackend.flows.user.transformers.User2UserCreateResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.user.transformers.User2UserProfileResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.user.transformers.User2UserSearchResponseDtoTransformer;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserService userService;
	private final PostService postService;

	@GetMapping("/api/users")
	public ResponseEntity<Page<UserSearchResponseDto>> findAllUsersByName(@RequestParam(name = "name", defaultValue = "") String name, @ParameterObject Pageable pageable) {
		Page<UserSearchResponseDto> responseData = userService.findAllUsersByName(name, pageable)
				.map(User2UserSearchResponseDtoTransformer::transform);

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/users")
	public ResponseEntity<UserCreateResponseDto> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
		UserCreateResponseDto responseData = User2UserCreateResponseDtoTransformer.transform(userService.createUser(userCreateRequestDto));

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/api/users/{userId}")
	public ResponseEntity<UserProfileResponseDto> findUserByPublicId(@PathVariable(name = "userId") UUID userPublicId) {
		User user = userService.findUserByPublicId(userPublicId);

		UserProfileResponseDto responseData = User2UserProfileResponseDtoTransformer.transform(user);

		return ResponseEntity.ok(responseData);
	}

	@PutMapping("/api/users/{userId}")
	public ResponseEntity<UserProfileResponseDto> updateUser(@PathVariable(name = "userId") UUID userPublicId, @RequestBody UserUpdateRequestDto userUpdateDto) {
		User user = userService.findUserByPublicId(userPublicId);

		User updatedUser = userService.updateUser(user, userUpdateDto);

		UserProfileResponseDto responseData = User2UserProfileResponseDtoTransformer.transform(updatedUser);

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/api/users/{userId}/friends")
	public ResponseEntity<Page<UserSearchResponseDto>> findAllFriendsOfUser(@PathVariable(name = "userId") UUID userPublicId, @ParameterObject Pageable pageable) {
		User user = userService.findUserByPublicId(userPublicId);

		Page<UserSearchResponseDto> responseData = userService.findAllFriendsByUser(user, pageable)
				.map(User2UserSearchResponseDtoTransformer::transform);

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/api/users/{userId}/posts")
	public ResponseEntity<Page<PostResponseDto>> findAllPostsOfUser(@PathVariable(name = "userId") UUID userPublicId, @ParameterObject Pageable pageable) {
		User user = userService.findUserByPublicId(userPublicId);

		Page<PostResponseDto> responseData = postService.findAllPostsOnUsersTimeline(user, pageable)
				.map(p -> {
					PostResponseDto transformed = Post2PostResponseDtoTransformer.transform(p);
					transformed.setReactionsCount(postService.findAllReactionCountsByPost(p));
					transformed.setCommentsCount(postService.findCommentCountByPost(p));

					return transformed;
				});

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/users/{userId}/posts")
	public ResponseEntity<PostResponseDto> createPostOnUser(@PathVariable(name = "userId") UUID userPublicId, @RequestBody PostCreateRequestDto postCreateRequestDto) {
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.
		User addresseeUser = userService.findUserByPublicId(userPublicId);

		Post createdPost = postService.createPost(currentUser, addresseeUser, postCreateRequestDto);

		PostResponseDto responseData = Post2PostResponseDtoTransformer.transform(createdPost);

		return ResponseEntity.ok(responseData);
	}
}
