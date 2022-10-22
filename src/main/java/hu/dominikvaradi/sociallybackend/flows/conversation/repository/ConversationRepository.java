package hu.dominikvaradi.sociallybackend.flows.conversation.repository;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	Optional<Conversation> findByPublicId(UUID conversationPublicId);

	Page<Conversation> findByUserConversationsUserOrderByLastMessageSentDesc(User user, Pageable pageable);

	@Query("select c from Conversation c left join c.userConversations userConversations where userConversations.user = ?1 and c.type = 'DIRECT'")
	List<Conversation> findAllDirectConversationsByUser(User user);

	@Modifying
	@Query(value = "truncate table conversations restart identity cascade", nativeQuery = true)
	void truncate();
}