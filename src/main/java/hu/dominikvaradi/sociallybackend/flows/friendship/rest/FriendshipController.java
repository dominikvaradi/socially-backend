package hu.dominikvaradi.sociallybackend.flows.friendship.rest;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestIncomingResponseDto;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto.FriendRequestOutgoingResponseDto;
import hu.dominikvaradi.sociallybackend.flows.friendship.service.FriendshipService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class FriendshipController {
	private final FriendshipService friendshipService;

	@GetMapping("/friendships/incoming")
	public ResponseEntity<Page<FriendRequestIncomingResponseDto>> findAllIncomingFriendRequestsForCurrentUser(@ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/friendships/incoming/{friendshipId}/accept")
	public ResponseEntity<Void> acceptIncomingFriendRequest(@PathVariable(name = "friendshipId") UUID friendshipPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/friendships/incoming/{friendshipId}")
	public ResponseEntity<Void> declineIncomingFriendRequest(@PathVariable(name = "friendshipId") UUID friendshipPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/friendships/outgoing")
	public ResponseEntity<Page<FriendRequestOutgoingResponseDto>> findAllOutgoingFriendRequestsForCurrentUser(@ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/friendships/outgoing")
	public ResponseEntity<FriendRequestOutgoingResponseDto> createNewFriendRequest(@RequestBody FriendRequestCreateRequestDto friendRequestCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/friendships/outgoing/{friendshipId}")
	public ResponseEntity<Void> revokeOutgoingFriendRequest(@PathVariable(name = "friendshipId") UUID friendshipPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}
}
