package hu.dominikvaradi.sociallybackend.flows.conversation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
class UserConversationKey implements Serializable {
	@Column(name = "user_id")
	Long userId;

	@Column(name = "conversation_id")
	Long conversationId;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}

		UserConversationKey other = (UserConversationKey) o;
		return getUserId() != null && getConversationId() != null
				&& Objects.equals(getUserId(), other.getUserId())
				&& Objects.equals(getConversationId(), other.getConversationId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getConversationId());
	}
}