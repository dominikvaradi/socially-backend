package hu.dominikvaradi.sociallybackend.flows.post.domain;

import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "posts")
public class Post extends BaseDomain {
	@Column(name = "header")
	private String header;

	@Column(name = "content", nullable = false)
	private String content;

	@ManyToOne(optional = false)
	@JoinColumn(name = "author_user_id", nullable = false)
	private User author;

	@ManyToOne(optional = false)
	@JoinColumn(name = "addressee_user_id", nullable = false)
	private User addressee;

	@ToString.Exclude
	@OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
	private Set<PostReaction> reactions = new HashSet<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
	private Set<Comment> comments = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Post other = (Post) o;
		return getId() != null && Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
