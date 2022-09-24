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
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "conversations")
public class Conversation extends BaseDomain {
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private ConversationType type;

	@ToString.Exclude
	@OneToMany(mappedBy = "conversation")
	Set<UserConversation> userConversations = new HashSet<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Message> messages = new HashSet<>();

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
