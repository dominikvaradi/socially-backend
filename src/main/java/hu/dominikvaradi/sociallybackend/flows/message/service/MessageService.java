package hu.dominikvaradi.sociallybackend.flows.message.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.ConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.message.repository.MessageReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.message.repository.MessageRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {
	private final MessageRepository messageRepository;
	private final MessageReactionRepository messageReactionRepository;
	private final ConversationRepository conversationRepository;

	@PostAuthorize("isAuthenticationUserMemberOfConversation(returnObject.conversation)")
	public Message findMessageByPublicId(UUID messagePublicId) {
		return messageRepository.findByPublicId(messagePublicId)
				.orElseThrow(() -> new EntityNotFoundException("MESSAGE_NOT_FOUND"));
	}

	@PreAuthorize("authentication.principal.user == #user && isUserMemberOfConversation(#user, #conversation)")
	public Message createMessage(Conversation conversation, User user, MessageCreateRequestDto messageCreateRequestDto) {
		Message message = Message.builder()
				.user(user)
				.conversation(conversation)
				.content(messageCreateRequestDto.getContent())
				.build();

		message = messageRepository.save(message);

		conversation.setLastMessageSent(Instant.now());
		conversationRepository.save(conversation);

		return message;
	}

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#conversation)")
	public Page<Message> findAllMessagesByConversation(Conversation conversation, Pageable pageable) {
		return messageRepository.findByConversationOrderByCreatedDesc(conversation, pageable);
	}

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#message.conversation) && authentication.principal.user == #message.user")
	public void deleteMessage(Message message) {
		messageRepository.delete(message);
	}

	@PreAuthorize("authentication.principal.user == #user && isUserMemberOfConversation(#user, #message.conversation)")
	public MessageReaction toggleReactionOnMessage(Message message, User user, Reaction reaction) {
		Optional<MessageReaction> existingMessageReaction = messageReactionRepository.findByUserAndMessage(user, message);
		if (existingMessageReaction.isPresent()) {
			messageReactionRepository.delete(existingMessageReaction.get());

			if (existingMessageReaction.get().getReaction() == reaction) {
				return null;
			}
		}

		MessageReaction newMessageReaction = MessageReaction.builder()
				.reaction(reaction)
				.user(user)
				.message(message)
				.build();

		return messageReactionRepository.save(newMessageReaction);
	}

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#message.conversation)")
	public Page<MessageReaction> findAllReactionsByMessage(Message message, Pageable pageable) {
		return messageReactionRepository.findAllByMessageOrderByUserLastNameAsc(message, pageable);
	}

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#message.conversation)")
	public Set<ReactionCountResponseDto> findAllReactionCountsByMessage(Message message) {
		Set<ReactionCountResponseDto> reactionCounts = new HashSet<>();

		for (Reaction reaction : Reaction.values()) {
			reactionCounts.add(ReactionCountResponseDto.builder()
					.reaction(reaction)
					.count(messageReactionRepository.countByMessageAndReaction(message, reaction))
					.build());
		}

		return reactionCounts;
	}

	@PreAuthorize("authentication.principal.user == #user && isAuthenticationUserMemberOfConversation(#message.conversation)")
	public Reaction getUsersReactionByMessage(User user, Message message) {
		Optional<MessageReaction> messageReaction = messageReactionRepository.findByUserAndMessage(user, message);
		if (messageReaction.isEmpty()) {
			return null;
		}

		return messageReaction.get().getReaction();
	}
}
