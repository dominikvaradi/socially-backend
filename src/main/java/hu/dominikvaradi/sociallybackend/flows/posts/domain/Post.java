package hu.dominikvaradi.sociallybackend.flows.posts.domain;

import hu.dominikvaradi.sociallybackend.flows.comments.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostReaction> reactions = new ArrayList<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

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
