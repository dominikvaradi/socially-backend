package hu.dominikvaradi.sociallybackend.flows.conversation.repository;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	Optional<Conversation> findByPublicId(UUID conversationPublicId);

	Page<Conversation> findByUserConversationsUserOrderByLastMessageSentDesc(User user, Pageable pageable);
}