package hu.dominikvaradi.sociallybackend.flows.conversation.service;

import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityUnprocessableException;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.ConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.UserConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType.DIRECT;
import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType.GROUP;
import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole.ADMIN;
import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole.NORMAL;

@RequiredArgsConstructor
@Transactional
@Service
public class ConversationService {
	private final ConversationRepository conversationRepository;
	private final UserConversationRepository userConversationRepository;

	@PreAuthorize("authentication.principal.user == #requesterUser && isAuthenticationUserFriendOfOtherUsers(#otherUsers)")
	public Conversation createConversation(User requesterUser, Set<User> otherUsers) {
		Conversation conversation = new Conversation();

		if (otherUsers.contains(requesterUser)) {
			throw new EntityUnprocessableException("REQUESTER_USER_CANNOT_BE_IN_MEMBERS_ARRAY");
		}

		if (otherUsers.size() == 1) {
			User otherUser = otherUsers.stream().iterator().next();
			Optional<Conversation> conversationBetweenTheUsers = findDirectConversationByUsers(requesterUser, otherUser);
			if (conversationBetweenTheUsers.isPresent()) {
				return conversationBetweenTheUsers.get();
			}

			conversation.setType(DIRECT);
		} else {
			conversation.setType(GROUP);
		}

		conversation.setLastMessageSent(Instant.now());

		Conversation createdConversation = conversationRepository.save(conversation);

		Set<UserConversation> userConversations = otherUsers.stream()
				.map(u -> createUserConversation(u, createdConversation, NORMAL))
				.collect(Collectors.toSet());

		userConversations.add(createUserConversation(requesterUser, createdConversation, conversation.getType() == DIRECT ? NORMAL : ADMIN));

		createdConversation.setUserConversations(userConversations);

		return conversationRepository.save(createdConversation);
	}

	@PostAuthorize("isAuthenticationUserMemberOfConversation(returnObject)")
	public Conversation findConversationByPublicId(UUID conversationPublicId) {
		return conversationRepository.findByPublicId(conversationPublicId)
				.orElseThrow(() -> new EntityNotFoundException("CONVERSATION_NOT_FOUND"));
	}

	@PreAuthorize("isAuthenticationUserAdminOfConversation(#conversation) && isAuthenticationUserFriendOfOtherUsers(#users)")
	public Conversation addUsersToConversation(Conversation conversation, Set<User> users) {
		if (conversation.getType() == DIRECT) {
			throw new EntityUnprocessableException("DIRECT_CONVERSATIONS_CANNOT_BE_EXPANDED");
		}

		Set<UserConversation> userConversations = users.stream()
				.filter(u -> findUserConversationInConversationByUser(u, conversation).isEmpty())
				.map(u -> createUserConversation(u, conversation, NORMAL))
				.collect(Collectors.toSet());

		conversation.getUserConversations().addAll(userConversations);

		return conversationRepository.save(conversation);
	}

	@PreAuthorize("authentication.principal.user == #currentUser && isAuthenticationUserAdminOfConversation(#conversation)")
	public void removeUserFromConversation(Conversation conversation, User currentUser, User user) {
		UserConversation userConversation = findUserConversationInConversationByUser(user, conversation)
				.orElseThrow(() -> new EntityNotFoundException("USER_NOT_FOUND_IN_CONVERSATION"));

		long adminCount = conversation.getUserConversations().stream()
				.filter(uc -> uc.getUserRole() == ADMIN)
				.count();

		if (adminCount == 1 && Objects.equals(currentUser, user)) {
			throw new EntityUnprocessableException("CONVERSATIONS_MUST_HAVE_AN_ADMIN");
		}

		conversation.getUserConversations().remove(userConversation);

		conversationRepository.save(conversation);
	}

	@PreAuthorize("authentication.principal.user == #currentUser && isAuthenticationUserAdminOfConversation(#conversation)")
	public UserConversation changeUserRoleInConversation(Conversation conversation, User currentUser, User user, UserConversationRole role) {
		UserConversation userConversation = findUserConversationInConversationByUser(user, conversation)
				.orElseThrow(() -> new EntityNotFoundException("USER_NOT_FOUND_IN_CONVERSATION"));

		long adminCount = conversation.getUserConversations().stream()
				.filter(uc -> uc.getUserRole() == ADMIN)
				.count();

		if (adminCount == 1 && Objects.equals(currentUser, user)) {
			throw new EntityUnprocessableException("CONVERSATIONS_MUST_HAVE_AN_ADMIN");
		}

		userConversation.setUserRole(role);

		return userConversationRepository.save(userConversation);
	}

	@PreAuthorize("authentication.principal.user == #user")
	public Page<Conversation> findAllConversationsByUser(User user, Pageable pageable) {
		return conversationRepository.findByUserConversationsUserOrderByLastMessageSentDesc(user, pageable);
	}

	private UserConversation createUserConversation(User user, Conversation conversation, UserConversationRole role) {
		return UserConversation.builder()
				.user(user)
				.conversation(conversation)
				.userRole(role)
				.build();
	}

	private Optional<Conversation> findDirectConversationByUsers(User firstUser, User secondUser) {
		List<Conversation> directConversationsOfFirstUser = conversationRepository.findAllDirectConversationsByUser(firstUser);

		return directConversationsOfFirstUser
				.stream()
				.filter(c -> c.getUserConversations()
						.stream()
						.anyMatch(uc -> Objects.equals(uc.getUser(), secondUser)))
				.findFirst();
	}

	private Optional<UserConversation> findUserConversationInConversationByUser(User user, Conversation conversation) {
		for (UserConversation uc : conversation.getUserConversations()) {
			if (Objects.equals(uc.getUser(), user)) {
				return Optional.of(uc);
			}
		}

		return Optional.empty();
	}
}
