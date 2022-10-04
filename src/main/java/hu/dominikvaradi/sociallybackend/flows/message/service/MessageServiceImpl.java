package hu.dominikvaradi.sociallybackend.flows.message.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageServiceImpl implements MessageService {
	private final MessageRepository messageRepository;
	private final MessageReactionRepository messageReactionRepository;
	private final ConversationRepository conversationRepository;

	@Override
	public Message createMessage(Conversation conversation, User user, MessageCreateRequestDto messageCreateRequestDto) {
		Message message = Message.builder()
				.user(user)
				.conversation(conversation)
				.content(messageCreateRequestDto.getContent())
				.build();

		message = messageRepository.save(message);

		conversation.setLastMessageSent(LocalDateTime.now());
		conversationRepository.save(conversation);

		return message;
	}

	@Override
	public Page<Message> findAllMessagesByConversation(Conversation conversation, Pageable pageable) {
		return messageRepository.findByConversationOrderByCreatedDesc(conversation, pageable);
	}

	@Override
	public void deleteMessage(Message message) {
		messageRepository.delete(message);
	}

	@Override
	public MessageReaction addReactionToMessage(Message message, User user, Reaction reaction) {
		if (messageReactionRepository.findByUserAndMessageAndReaction(user, message, reaction).isPresent()) {
			throw new RuntimeException(); // TODO REST Exception - már létezik az entity, conflict lenne.
		}

		MessageReaction newMessageReaction = MessageReaction.builder()
				.reaction(reaction)
				.user(user)
				.message(message)
				.build();

		return messageReactionRepository.save(newMessageReaction);
	}

	@Override
	public void deleteReactionFromMessage(Message message, User user, Reaction reaction) {
		MessageReaction messageReaction = messageReactionRepository.findByUserAndMessageAndReaction(user, message, reaction)
				.orElseThrow(); // TODO REST Exception 404

		messageReactionRepository.delete(messageReaction);
	}

	@Override
	public Page<MessageReaction> findAllReactionsByMessage(Message message, Pageable pageable) {
		return messageReactionRepository.findAllByMessageOrderByUserNameAsc(message, pageable);
	}

	@Override
	public EnumMap<Reaction, Long> findAllReactionCountsByMessage(Message message) {
		EnumMap<Reaction, Long> reactionCount = new EnumMap<>(Reaction.class);

		for (Reaction reaction : Reaction.values()) {
			reactionCount.put(reaction, messageReactionRepository.countByMessageAndReaction(message, reaction));
		}

		return reactionCount;
	}
}
