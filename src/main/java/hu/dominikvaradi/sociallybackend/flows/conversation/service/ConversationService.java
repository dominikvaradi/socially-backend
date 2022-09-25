package hu.dominikvaradi.sociallybackend.flows.conversation.service;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ConversationService {
	Conversation createDirectConversation(User requesterUser, User addresseeUser);

	Conversation createGroupConversation(User ownerUser, Set<User> otherUsers);

	Optional<Conversation> findConversationByPublicId(UUID conversationPublicId);

	Conversation addUsersToConversation(Conversation conversation, Set<User> users);

	Conversation removeUserFromConversation(Conversation conversation, User user);

	Conversation changeUserRoleInConversation(Conversation conversation, User user, UserConversationRole role);

	Page<Conversation> findAllConversationsByUser(User user, Pageable pageable);
}
