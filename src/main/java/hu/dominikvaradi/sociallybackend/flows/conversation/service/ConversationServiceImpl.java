package hu.dominikvaradi.sociallybackend.flows.conversation.service;

import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityConflictException;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityUnprocessableException;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.ConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.UserConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType.DIRECT;
import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole.ADMIN;
import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole.NORMAL;

@RequiredArgsConstructor
@Transactional
@Service
public class ConversationServiceImpl implements ConversationService {
	private final ConversationRepository conversationRepository;
	private final UserConversationRepository userConversationRepository;

	@Override
	public Conversation createConversation(User requesterUser, ConversationType type, Set<User> otherUsers) {
		Conversation conversation = Conversation.builder()
				.type(type)
				.build();

		if (otherUsers.contains(requesterUser)) {
			throw new EntityUnprocessableException("The requester user cannot be in the other user members array.");
		}

		if (type == DIRECT) {
			if (otherUsers.size() != 1) {
				throw new EntityUnprocessableException("Direct conversations must contain only one user in the other user members array.");
			}

			User otherUser = otherUsers.stream().iterator().next();
			Optional<Conversation> conversationBetweenTheUsers = findDirectConversationByUsers(requesterUser, otherUser);
			if (conversationBetweenTheUsers.isPresent()) {
				throw new EntityConflictException("The requester user and the given user already have an existing conversation.");
			}
		}

		Set<UserConversation> userConversations = otherUsers.stream()
				.map(u -> createUserConversation(u, conversation, NORMAL))
				.collect(Collectors.toSet());

		userConversations.add(createUserConversation(requesterUser, conversation, type == DIRECT ? NORMAL : ADMIN));

		conversation.setUserConversations(userConversations);

		return conversationRepository.save(conversation);
	}

	@Override
	public Conversation findConversationByPublicId(UUID conversationPublicId) {
		return conversationRepository.findByPublicId(conversationPublicId)
				.orElseThrow(() -> new EntityNotFoundException("Conversation not found."));
	}

	@Override
	public List<UserConversation> addUsersToConversation(Conversation conversation, Set<User> users) {
		Set<UserConversation> userConversations = users.stream()
				.map(u -> createUserConversation(u, conversation, NORMAL))
				.collect(Collectors.toSet());

		conversation.getUserConversations().addAll(userConversations);

		return userConversationRepository.saveAll(userConversations);
	}

	@Override
	public void removeUserFromConversation(Conversation conversation, User user) {
		Set<UserConversation> userConversations = conversation.getUserConversations();

		UserConversation userConversation = userConversations.stream()
				.filter(uc -> uc.getUser().equals(user))
				.findFirst()
				.orElseThrow(() -> new EntityNotFoundException("User not found in conversation."));

		userConversations.remove(userConversation);

		userConversationRepository.save(userConversation);
	}

	@Override
	public UserConversation changeUserRoleInConversation(Conversation conversation, User user, UserConversationRole role) {
		UserConversation userConversation = conversation.getUserConversations().stream()
				.filter(uc -> uc.getUser().equals(user))
				.findFirst()
				.orElseThrow(() -> new EntityNotFoundException("User not found in conversation."));

		userConversation.setUserRole(role);

		return userConversationRepository.save(userConversation);
	}

	@Override
	public Page<Conversation> findAllConversationsByUser(User user, Pageable pageable) {
		return conversationRepository.findByUserConversationsUserOrderByLastMessageSentDesc(user, pageable);
	}

	@Override
	public Conversation findDirectConversationBetweenTwoUsers(User firstUser, User secondUser) {
		return findDirectConversationByUsers(firstUser, secondUser)
				.orElseThrow(() -> new EntityNotFoundException("Conversation not found for given users."));
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
}
