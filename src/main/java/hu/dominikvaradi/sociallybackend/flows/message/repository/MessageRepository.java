package hu.dominikvaradi.sociallybackend.flows.message.repository;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {
	Optional<Message> findByPublicId(UUID messagePublicId);

	Page<Message> findByConversationOrderByCreatedDesc(Conversation conversation, Pageable pageable);

	@Modifying
	@Query(value = "truncate table messages restart identity cascade", nativeQuery = true)
	void truncate();
}