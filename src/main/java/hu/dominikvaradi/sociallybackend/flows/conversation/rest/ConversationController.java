package hu.dominikvaradi.sociallybackend.flows.conversation.rest;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationAddUsersRequestDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationChangeUserRoleRequestDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationResponseDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.dto.ConversationUserResponseDto;
import hu.dominikvaradi.sociallybackend.flows.conversation.service.ConversationService;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.message.service.MessageService;
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

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ConversationController {
	private final ConversationService conversationService;
	private final MessageService messageService;

	@GetMapping("/api/conversations")
	public ResponseEntity<Page<ConversationResponseDto>> findAllConversationsByCurrentUser(@ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/conversations")
	public ResponseEntity<ConversationResponseDto> createConversation(@RequestBody ConversationCreateRequestDto conversationCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/conversations/{conversationId}")
	public ResponseEntity<ConversationResponseDto> findConversationByPublicId(@PathVariable(name = "conversationId") UUID conversationPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PutMapping("/api/conversations/{conversationId}/users")
	public ResponseEntity<Set<ConversationUserResponseDto>> addUsersToConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @RequestBody ConversationAddUsersRequestDto conversationAddUsersRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/api/conversations/{conversationId}/users/{userId}")
	public ResponseEntity<Void> removeUserFromConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @PathVariable(name = "userId") UUID userPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PutMapping("/api/conversations/{conversationId}/users/{userId}/role")
	public ResponseEntity<ConversationUserResponseDto> updateUsersRoleInConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @PathVariable(name = "userId") UUID userPublicId, ConversationChangeUserRoleRequestDto conversationChangeUserRoleRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/conversations/{conversationId}/messages")
	public ResponseEntity<Page<MessageResponseDto>> findMessagesByConversation(@PathVariable(name = "conversationId") UUID conversationPublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/conversations/{conversationId}/messages")
	public ResponseEntity<Page<MessageResponseDto>> createMessageInConversation(@PathVariable(name = "conversationId") UUID conversationPublicId, @RequestBody MessageCreateRequestDto messageCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}
}
