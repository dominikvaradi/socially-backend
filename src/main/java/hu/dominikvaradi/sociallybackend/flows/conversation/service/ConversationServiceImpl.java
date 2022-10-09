package hu.dominikvaradi.sociallybackend.flows.conversation.service;

import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.ConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.UserConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
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
public class ConversationServiceImpl implements ConversationService {
	private final ConversationRepository conversationRepository;
	private final UserConversationRepository userConversationRepository;

	@Override
	public Conversation createDirectConversation(User requesterUser, User addresseeUser) {
		Conversation conversation = Conversation.builder()
				.type(DIRECT)
				.build();

		Set<UserConversation> userConversations = new HashSet<>();
		userConversations.add(createUserConversation(requesterUser, conversation, NORMAL));
		userConversations.add(createUserConversation(addresseeUser, conversation, NORMAL));

		conversation.setUserConversations(userConversations);

		return conversationRepository.save(conversation);
	}

	@Override
	public Conversation createGroupConversation(User ownerUser, Set<User> otherUsers) {
		Conversation conversation = Conversation.builder()
				.type(GROUP)
				.build();

		Set<UserConversation> userConversations = otherUsers.stream()
				.map(u -> createUserConversation(u, conversation, NORMAL))
				.collect(Collectors.toSet());

		userConversations.add(createUserConversation(ownerUser, conversation, ADMIN));

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

	private static UserConversation createUserConversation(User user, Conversation conversation, UserConversationRole role) {
		return UserConversation.builder()
				.user(user)
				.conversation(conversation)
				.userRole(role)
				.build();
	}
}
