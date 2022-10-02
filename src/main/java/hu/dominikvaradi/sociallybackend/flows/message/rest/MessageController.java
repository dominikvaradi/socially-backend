package hu.dominikvaradi.sociallybackend.flows.message.rest;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageReactionResponseDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class MessageController {
	private final MessageService messageService;

	@GetMapping("/api/messages/{messageId}")
	public ResponseEntity<MessageResponseDto> findMessageByPublicId(@PathVariable(name = "messageId") UUID messagePublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/api/messages/{messageId}")
	public ResponseEntity<MessageResponseDto> deleteMessage(@PathVariable(name = "messageId") UUID messagePublicId) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@GetMapping("/api/messages/{messageId}/reactions")
	public ResponseEntity<Page<MessageReactionResponseDto>> findAllReactionsByMessage(@PathVariable(name = "messageId") UUID messagePublicId, @ParameterObject Pageable pageable) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@PostMapping("/api/messages/{messageId}/reactions")
	public ResponseEntity<MessageReactionResponseDto> createReactionOnMessage(@PathVariable(name = "messageId") UUID messagePublicId, @RequestBody ReactionCreateRequestDto reactionCreateRequestDto) {
		throw new NotImplementedException("REST method not implemented yet.");
	}

	@DeleteMapping("/api/messages/{messageId}/reactions/{reaction}")
	public ResponseEntity<MessageReactionResponseDto> deleteReactionFromMessage(@PathVariable(name = "messageId") UUID messagePublicId, @PathVariable(name = "reaction") Reaction reaction) {
		throw new NotImplementedException("REST method not implemented yet.");
	}
}
