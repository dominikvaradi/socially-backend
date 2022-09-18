package hu.dominikvaradi.sociallybackend.flows.comments.domain;

import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "comment_reactions")
public class CommentReaction extends BaseDomain {
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "comment_id", nullable = false)
	private Comment comment;

	@Enumerated(EnumType.STRING)
	@Column(name = "reaction", nullable = false)
	private Reaction reaction;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CommentReaction other = (CommentReaction) o;
		return getId() != null && Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
