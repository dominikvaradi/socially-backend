package hu.dominikvaradi.sociallybackend.flows.conversation.service;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ConversationService {
	@PreAuthorize("authentication.principal.requesterUser == #firstUser && isUserFriendOfOtherUsers(#requesterUser, #otherUsers)")
	Conversation createConversation(User requesterUser, ConversationType type, Set<User> otherUsers);

	@PostAuthorize("isAuthenticationUserMemberOfConversation(returnObject)")
	Conversation findConversationByPublicId(UUID conversationPublicId);

	@PreAuthorize("isAuthenticationUserAdminOfConversation(#conversation) && isAuthenticationUserFriendOfOtherUsers(#otherUsers)")
	List<UserConversation> addUsersToConversation(Conversation conversation, Set<User> users);

	@PreAuthorize("isAuthenticationUserAdminOfConversation(#conversation)")
	void removeUserFromConversation(Conversation conversation, User user);

	@PreAuthorize("isAuthenticationUserAdminOfConversation(#conversation)")
	UserConversation changeUserRoleInConversation(Conversation conversation, User user, UserConversationRole role);

	@PreAuthorize("authentication.principal.user == #user")
	Page<Conversation> findAllConversationsByUser(User user, Pageable pageable);
}
