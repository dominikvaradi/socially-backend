package hu.dominikvaradi.sociallybackend.flows.friendship.rest;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestIncomingResponseDto;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestOutgoingResponseDto;
import hu.dominikvaradi.sociallybackend.flows.friendship.service.FriendshipService;
import hu.dominikvaradi.sociallybackend.flows.friendship.transformers.Friendship2FriendRequestIncomingResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.friendship.transformers.Friendship2FriendRequestOutgoingResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.security.domain.JwtUserDetails;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class FriendshipController {
	private final FriendshipService friendshipService;
	private final UserService userService;

	@GetMapping("/friendships/incoming")
	public ResponseEntity<Page<FriendRequestIncomingResponseDto>> findAllIncomingFriendRequestsForCurrentUser(@ParameterObject Pageable pageable) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();

		Page<FriendRequestIncomingResponseDto> responseData = friendshipService.findAllIncomingFriendRequestsOfUser(currentUser, pageable)
				.map(fs -> Friendship2FriendRequestIncomingResponseDtoTransformer.transform(fs, currentUser));

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/friendships/incoming/{friendshipId}/accept")
	public ResponseEntity<FriendRequestIncomingResponseDto> acceptIncomingFriendRequest(@PathVariable(name = "friendshipId") UUID friendshipPublicId) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Friendship friendship = friendshipService.findByPublicId(friendshipPublicId);

		Friendship acceptedFriendship = friendshipService.acceptFriendRequest(friendship, currentUser);

		FriendRequestIncomingResponseDto responseData = Friendship2FriendRequestIncomingResponseDtoTransformer.transform(acceptedFriendship, currentUser);

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/friendships/incoming/{friendshipId}")
	public ResponseEntity<FriendRequestIncomingResponseDto> declineIncomingFriendRequest(@PathVariable(name = "friendshipId") UUID friendshipPublicId) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Friendship friendship = friendshipService.findByPublicId(friendshipPublicId);

		Friendship declinedFriendship = friendshipService.declineFriendRequest(friendship, currentUser);

		FriendRequestIncomingResponseDto responseData = Friendship2FriendRequestIncomingResponseDtoTransformer.transform(declinedFriendship, currentUser);

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/friendships/outgoing")
	public ResponseEntity<Page<FriendRequestOutgoingResponseDto>> findAllOutgoingFriendRequestsForCurrentUser(@ParameterObject Pageable pageable) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();

		Page<FriendRequestOutgoingResponseDto> responseData = friendshipService.findAllOutgoingFriendRequestsOfUser(currentUser, pageable)
				.map((fs -> Friendship2FriendRequestOutgoingResponseDtoTransformer.transform(fs, currentUser)));

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/friendships/outgoing")
	public ResponseEntity<FriendRequestOutgoingResponseDto> createNewFriendRequest(@RequestBody FriendRequestCreateRequestDto friendRequestCreateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		User addresseeUser = userService.findUserByPublicId(friendRequestCreateRequestDto.getAddresseeUserId());

		Friendship createdFriendship = friendshipService.createFriendRequest(currentUser, addresseeUser);

		FriendRequestOutgoingResponseDto responseData = Friendship2FriendRequestOutgoingResponseDtoTransformer.transform(createdFriendship, currentUser);

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/friendships/outgoing/{friendshipId}")
	public ResponseEntity<FriendRequestOutgoingResponseDto> revokeOutgoingFriendRequest(@PathVariable(name = "friendshipId") UUID friendshipPublicId) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Friendship friendship = friendshipService.findByPublicId(friendshipPublicId);

		Friendship revokedFriendship = friendshipService.declineFriendRequest(friendship, currentUser);

		FriendRequestOutgoingResponseDto responseData = Friendship2FriendRequestOutgoingResponseDtoTransformer.transform(revokedFriendship, currentUser);

		return ResponseEntity.ok(responseData);
	}
}
