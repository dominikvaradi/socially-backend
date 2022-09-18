package hu.dominikvaradi.sociallybackend.flows.user.domain;

import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
@Entity
public class User extends BaseDomain {
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "email")
	private String password;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;
		return Objects.equals(this.getId(), user.getId())
				&& Objects.equals(this.getPublicId(), user.getPublicId())
				&& Objects.equals(this.getVersion(), user.getVersion())
				&& Objects.equals(firstName, user.firstName)
				&& Objects.equals(lastName, user.lastName)
				&& Objects.equals(email, user.email)
				&& Objects.equals(password, user.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getPublicId(), getVersion(), firstName, lastName, email, password);
	}
}