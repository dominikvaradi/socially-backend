package hu.dominikvaradi.sociallybackend.flows.message.rest;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.EmptyRestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.PageableRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.RestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageReactionResponseDto;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageResponseDto;
import hu.dominikvaradi.sociallybackend.flows.message.service.MessageService;
import hu.dominikvaradi.sociallybackend.flows.message.transformers.Message2MessageResponseDtoTransformer;
import hu.dominikvaradi.sociallybackend.flows.message.transformers.MessageReaction2MessageReactionResponseDtoTransformer;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

@SecurityRequirement(name = "BearerToken")
@RequiredArgsConstructor
@RestController
public class MessageController {
	private final MessageService messageService;

	@GetMapping("/messages/{messageId}")
	public ResponseEntity<RestApiResponseDto<MessageResponseDto>> findMessageByPublicId(@PathVariable(name = "messageId") UUID messagePublicId) {
		Message message = messageService.findMessageByPublicId(messagePublicId);

		MessageResponseDto responseData = Message2MessageResponseDtoTransformer.transform(message);
		responseData.setReactionsCount(new ArrayList<>(messageService.findAllReactionCountsByMessage(message)));

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@DeleteMapping("/messages/{messageId}")
	public ResponseEntity<EmptyRestApiResponseDto> deleteMessage(@PathVariable(name = "messageId") UUID messagePublicId) {
		Message message = messageService.findMessageByPublicId(messagePublicId);

		messageService.deleteMessage(message);

		return ResponseEntity.ok(new EmptyRestApiResponseDto());
	}

	@GetMapping("/messages/{messageId}/reactions")
	public ResponseEntity<RestApiResponseDto<PageResponseDto<MessageReactionResponseDto>>> findAllReactionsByMessage(@PathVariable(name = "messageId") UUID messagePublicId, @ParameterObject PageableRequestDto pageableRequestDto) {
		Pageable pageable = PageRequest.of(pageableRequestDto.getPage(), pageableRequestDto.getSize());

		Message message = messageService.findMessageByPublicId(messagePublicId);

		Page<MessageReactionResponseDto> page = messageService.findAllReactionsByMessage(message, pageable)
				.map(MessageReaction2MessageReactionResponseDtoTransformer::transform);

		PageResponseDto<MessageReactionResponseDto> responseData = PageResponseDto.buildFromPage(page);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PostMapping("/messages/{messageId}/reactions")
	public ResponseEntity<RestApiResponseDto<MessageReactionResponseDto>> createReactionOnMessage(@PathVariable(name = "messageId") UUID messagePublicId, @RequestBody ReactionCreateRequestDto reactionCreateRequestDto) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Message message = messageService.findMessageByPublicId(messagePublicId);

		MessageReaction createdMessageReaction = messageService.addReactionToMessage(message, currentUser, reactionCreateRequestDto.getReaction());

		MessageReactionResponseDto responseData = MessageReaction2MessageReactionResponseDtoTransformer.transform(createdMessageReaction);

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@DeleteMapping("/messages/{messageId}/reactions/{reaction}")
	public ResponseEntity<EmptyRestApiResponseDto> deleteReactionFromMessage(@PathVariable(name = "messageId") UUID messagePublicId, @PathVariable(name = "reaction") Reaction reaction) {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDetails.getUser();
		Message message = messageService.findMessageByPublicId(messagePublicId);

		messageService.deleteReactionFromMessage(message, currentUser, reaction);

		return ResponseEntity.ok(new EmptyRestApiResponseDto());
	}
}
