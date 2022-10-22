package hu.dominikvaradi.sociallybackend.flows.message.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.dto.MessageCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;
import java.util.UUID;

public interface MessageService {
	@PostAuthorize("isAuthenticationUserMemberOfConversation(returnObject.conversation)")
	Message findMessageByPublicId(UUID messagePublicId);

	@PreAuthorize("authentication.principal.user == #user && isUserMemberOfConversation(#user, #conversation)")
	Message createMessage(Conversation conversation, User user, MessageCreateRequestDto messageCreateRequestDto);

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#conversation)")
	Page<Message> findAllMessagesByConversation(Conversation conversation, Pageable pageable);

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#message.conversation) && authentication.principal.user == #message.user")
	void deleteMessage(Message message);

	@PreAuthorize("authentication.principal.user == #user && isUserMemberOfConversation(#user, #message.conversation)")
	MessageReaction addReactionToMessage(Message message, User user, Reaction reaction);

	@PreAuthorize("authentication.principal.user == #user && isUserMemberOfConversation(#user, #message.conversation)")
	void deleteReactionFromMessage(Message message, User user, Reaction reaction);

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#message.conversation)")
	Page<MessageReaction> findAllReactionsByMessage(Message message, Pageable pageable);

	@PreAuthorize("isAuthenticationUserMemberOfConversation(#message.conversation)")
	Set<ReactionCountResponseDto> findAllReactionCountsByMessage(Message message);
}
