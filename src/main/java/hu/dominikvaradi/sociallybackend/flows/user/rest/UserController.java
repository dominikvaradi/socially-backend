package hu.dominikvaradi.sociallybackend.flows.user.rest;

import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserProfileResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserSearchResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
import liquibase.repackaged.org.apache.commons.lang3.NotImplementedException;
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

	@GetMapping("/api/users")
	public ResponseEntity<Page<UserSearchResponseDto>> findAllUsersByName(@RequestParam(name = "name", defaultValue = "") String name, @ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/users")
	public ResponseEntity<UserCreateResponseDto> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/users/{userId}")
	public ResponseEntity<UserProfileResponseDto> findUserByPublicId(@PathVariable(name = "userId") UUID userPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PutMapping("/api/users/{userId}")
	public ResponseEntity<UserProfileResponseDto> updateUser(@PathVariable(name = "userId") UUID userPublicId, @RequestBody UserUpdateRequestDto userUpdateDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/users/{userId}/friends")
	public ResponseEntity<Page<UserSearchResponseDto>> findAllFriendsOfUser(@PathVariable(name = "userId") UUID userPublicId, @ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/users/{userId}/posts")
	public ResponseEntity<Page<PostResponseDto>> findAllPostsOfUser(@PathVariable(name = "userId") UUID userPublicId, @ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/users/{userId}/posts")
	public ResponseEntity<Page<PostResponseDto>> createPostOnUser(@PathVariable(name = "userId") UUID userPublicId, @RequestBody PostCreateRequestDto postCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}
}
