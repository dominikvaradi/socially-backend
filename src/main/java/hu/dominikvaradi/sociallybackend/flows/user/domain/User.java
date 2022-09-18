package hu.dominikvaradi.sociallybackend.flows.user.domain;

import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User extends BaseDomain {
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "password")
	private String password;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}

		User other = (User) o;
		return getId() != null && Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}