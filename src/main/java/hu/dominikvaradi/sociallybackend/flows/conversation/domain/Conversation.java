package hu.dominikvaradi.sociallybackend.flows.conversation.domain;

import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.ConversationType;
import hu.dominikvaradi.sociallybackend.flows.message.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "conversations")
public class Conversation extends BaseDomain {
	@Enumerated(STRING)
	@Column(name = "type", nullable = false)
	private ConversationType type;

	@ToString.Exclude
	@Builder.Default
	@OneToMany(fetch = EAGER, mappedBy = "conversation", cascade = ALL, orphanRemoval = true)
	private Set<UserConversation> userConversations = new java.util.LinkedHashSet<>();

	@ToString.Exclude
	@Builder.Default
	@OneToMany(mappedBy = "conversation", cascade = ALL, orphanRemoval = true)
	private Set<Message> messages = new HashSet<>();

	@Column(name = "last_message_sent", nullable = false)
	private Instant lastMessageSent;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}

		Conversation other = (Conversation) o;
		return getId() != null && Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
