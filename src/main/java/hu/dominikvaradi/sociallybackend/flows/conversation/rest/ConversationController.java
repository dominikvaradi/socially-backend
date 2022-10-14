package hu.dominikvaradi.sociallybackend.flows.conversation.rest;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationAddUsersRequestDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationChangeUserRoleRequestDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationResponseDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationUserResponseDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.service.ConversationService;
import hu.dominikvaradi.sociallybackend.flows.conversation.transformers.Conversation2ConversationResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.conversation.transformers.UserConversation2ConversationUserResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.message.service.MessageService;
import hu.dominikvaradi.sociallybackend.flows.message.transformers.Message2MessageResponseDtoTransformer;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ConversationController {
	private final ConversationService conversationService;
	private final MessageService messageService;
	private final UserService userService;

	@GetMapping("/api/conversations")
	public ResponseEntity<Page<ConversationResponseDto>> findAllConversationsByCurrentUser(@ParameterObject Pageable pageable) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();

		Page<ConversationResponseDto> responseData = conversationService.findAllConversationsByUser(currentUser, pageable)
				.map(Conversation2ConversationResponseDtoTransformer::transform);

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/conversations")
	public ResponseEntity<ConversationResponseDto> createConversation(@RequestBody ConversationCreateRequestDto conversationCreateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();

		Set<User> otherUsers = userService.findAllUsersByPublicIds(conversationCreateRequestDto.getMemberUserIds());

		Conversation createdConversation = conversationService.createConversation(currentUser, conversationCreateRequestDto.getType(), otherUsers);

		ConversationResponseDto responseData = Conversation2ConversationResponseDtoTransformer.transform(createdConversation);

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/api/conversations/{conversationId}")
	public ResponseEntity<ConversationResponseDto> findConversationByPublicId(@PathVariable(name = "conversationId") UUID conversationPublicId) {
		Conversation conversation = conversationService.findConversationByPublicId(conversationPublicId);

		ConversationResponseDto responseData = Conversation2ConversationResponseDtoTransformer.transform(conversation);

		return ResponseEntity.ok(responseData);
	}

	@PutMapping("/api/conversations/{conversationId}/users")
	public ResponseEntity<Set<ConversationUserResponseDto>> addUsersToConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @RequestBody ConversationAddUsersRequestDto conversationAddUsersRequestDto) {
		Conversation conversation = conversationService.findConversationByPublicId(conversationPublicId);

		Set<User> otherUsers = userService.findAllUsersByPublicIds(conversationAddUsersRequestDto.getMemberUserIds());

		Set<ConversationUserResponseDto> responseData = conversationService.addUsersToConversation(conversation, otherUsers)
				.stream()
				.map(UserConversation2ConversationUserResponseDtoTransformer::transform)
				.collect(Collectors.toSet());

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/api/conversations/{conversationId}/users/{userId}")
	public ResponseEntity<Void> removeUserFromConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @PathVariable(name = "userId") UUID userPublicId) {
		Conversation conversation = conversationService.findConversationByPublicId(conversationPublicId);
		User user = userService.findUserByPublicId(userPublicId);

		conversationService.removeUserFromConversation(conversation, user);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/api/conversations/{conversationId}/users/{userId}/role")
	public ResponseEntity<ConversationUserResponseDto> updateUsersRoleInConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @PathVariable(name = "userId") UUID userPublicId, ConversationChangeUserRoleRequestDto conversationChangeUserRoleRequestDto) {
		Conversation conversation = conversationService.findConversationByPublicId(conversationPublicId);
		User user = userService.findUserByPublicId(userPublicId);

		UserConversation updatedUserConversation = conversationService.changeUserRoleInConversation(conversation, user, conversationChangeUserRoleRequestDto.getRole());

		ConversationUserResponseDto responseData = UserConversation2ConversationUserResponseDtoTransformer.transform(updatedUserConversation);

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/api/conversations/{conversationId}/messages")
	public ResponseEntity<Page<MessageResponseDto>> findMessagesByConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @ParameterObject Pageable pageable) {
		Conversation conversation = conversationService.findConversationByPublicId(conversationPublicId);

		Page<MessageResponseDto> responseData = messageService.findAllMessagesByConversation(conversation, pageable)
				.map(m -> {
					MessageResponseDto transformed = Message2MessageResponseDtoTransformer.transform(m);
					transformed.setReactionsCount(messageService.findAllReactionCountsByMessage(m));

					return transformed;
				});

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/conversations/{conversationId}/messages")
	public ResponseEntity<MessageResponseDto> createMessageInConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @RequestBody MessageCreateRequestDto messageCreateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Conversation conversation = conversationService.findConversationByPublicId(conversationPublicId);

		Message createdMessage = messageService.createMessage(conversation, currentUser, messageCreateRequestDto);

		MessageResponseDto responseData = Message2MessageResponseDtoTransformer.transform(createdMessage);

		return ResponseEntity.ok(responseData);
	}
}
