package hu.dominikvaradi.sociallybackend.flows.conversation.repository;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserConversationRepository extends JpaRepository<UserConversation, UserConversationKey> {
	@Modifying
	@Query(value = "truncate table users_conversations restart identity cascade", nativeQuery = true)
	void truncate();
}