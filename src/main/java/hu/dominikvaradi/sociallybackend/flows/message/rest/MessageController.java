package hu.dominikvaradi.sociallybackend.flows.message.rest;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.message.service.MessageService;
import hu.dominikvaradi.sociallybackend.flows.message.transformers.Message2MessageResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.message.transformers.MessageReaction2MessageReactionResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
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
		Message message = messageService.findMessageByPublicId(messagePublicId);

		MessageResponseDto responseData = Message2MessageResponseDtoTransformer.transform(message);
		responseData.setReactionsCount(messageService.findAllReactionCountsByMessage(message));

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/api/messages/{messageId}")
	public ResponseEntity<MessageResponseDto> deleteMessage(@PathVariable(name = "messageId") UUID messagePublicId) {
		Message message = messageService.findMessageByPublicId(messagePublicId);

		messageService.deleteMessage(message);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/api/messages/{messageId}/reactions")
	public ResponseEntity<Page<MessageReactionResponseDto>> findAllReactionsByMessage(@PathVariable(name = "messageId") UUID messagePublicId, @ParameterObject Pageable pageable) {
		Message message = messageService.findMessageByPublicId(messagePublicId);

		Page<MessageReactionResponseDto> responseData = messageService.findAllReactionsByMessage(message, pageable)
				.map(MessageReaction2MessageReactionResponseDtoTransformer::transform);

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/messages/{messageId}/reactions")
	public ResponseEntity<MessageReactionResponseDto> createReactionOnMessage(@PathVariable(name = "messageId") UUID messagePublicId, @RequestBody ReactionCreateRequestDto reactionCreateRequestDto) {
		Message message = messageService.findMessageByPublicId(messagePublicId);
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.

		MessageReaction createdMessageReaction = messageService.addReactionToMessage(message, currentUser, reactionCreateRequestDto.getReaction());

		MessageReactionResponseDto responseData = MessageReaction2MessageReactionResponseDtoTransformer.transform(createdMessageReaction);

		return ResponseEntity.ok(responseData);
	}

	@DeleteMapping("/api/messages/{messageId}/reactions/{reaction}")
	public ResponseEntity<MessageReactionResponseDto> deleteReactionFromMessage(@PathVariable(name = "messageId") UUID messagePublicId, @PathVariable(name = "reaction") Reaction reaction) {
		Message message = messageService.findMessageByPublicId(messagePublicId);
		User currentUser = null; // TODO RequestContext-ből kivesszük a current user-t.

		messageService.deleteReactionFromMessage(message, currentUser, reaction);

		return ResponseEntity.noContent().build();
	}
}