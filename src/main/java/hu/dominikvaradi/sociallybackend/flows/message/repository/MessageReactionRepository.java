package hu.dominikvaradi.sociallybackend.flows.message.repository;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
	Page<MessageReaction> findAllByMessageOrderByUserLastNameAsc(Message message, Pageable pageable);

	Optional<MessageReaction> findByUserAndMessage(User user, Message message);

	long countByMessageAndReaction(Message message, Reaction reaction);

	@Modifying
	@Query(value = "truncate table message_reactions restart identity cascade", nativeQuery = true)
	void truncate();
}