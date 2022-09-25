package hu.dominikvaradi.sociallybackend.flows.message.repository;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import hu.dominikvaradi.sociallybackend.flows.message.domain.MessageReaction;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
	Page<MessageReaction> findAllByMessageOrderByUserNameAsc(Message message, Pageable pageable);

	Optional<MessageReaction> findByUserAndMessageAndReaction(User user, Message message, Reaction reaction);

	long countByMessageAndReaction(Message message, Reaction reaction);
}