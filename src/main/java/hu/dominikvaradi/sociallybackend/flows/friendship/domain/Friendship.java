package hu.dominikvaradi.sociallybackend.flows.friendship.domain;

import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "friendships")
public class Friendship extends BaseDomain {
	@ManyToOne(optional = false)
	@JoinColumn(name = "requester_user_id", nullable = false)
	private User requester;

	@ManyToOne(optional = false)
	@JoinColumn(name = "addressee_user_id", nullable = false)
	private User addressee;

	@ManyToOne(optional = false)
	@JoinColumn(name = "last_status_modifier_user_id", nullable = false)
	private User lastStatusModifier;

	@Enumerated(STRING)
	@Column(name = "status", nullable = false)
	private FriendshipStatus status;

	@Column(name = "status_last_modified", nullable = false)
	private Instant statusLastModified;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Friendship other = (Friendship) o;
		return getId() != null && Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
