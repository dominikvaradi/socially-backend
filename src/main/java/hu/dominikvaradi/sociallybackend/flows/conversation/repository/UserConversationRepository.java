package hu.dominikvaradi.sociallybackend.flows.conversation.repository;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConversationRepository extends JpaRepository<UserConversation, UserConversationKey> {
}