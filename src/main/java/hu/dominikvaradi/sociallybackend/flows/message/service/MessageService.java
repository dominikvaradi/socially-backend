package hu.dominikvaradi.sociallybackend.flows.message.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageCreateDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface MessageService {
	Message createMessage(Conversation conversation, User user, MessageCreateDto messageCreateDto);

	Page<Message> findAllMessagesByConversation(Conversation conversation, Pageable pageable);

	void deleteMessage(Message message);

	MessageReaction addReactionToMessage(Message message, User user, Reaction reaction);

	void deleteReactionFromMessage(Message message, User user, Reaction reaction);

	Page<MessageReaction> findAllReactionsByMessage(Message message, Pageable pageable);

	Map<Reaction, Long> findAllReactionCountsByMessage(Message message);
}
