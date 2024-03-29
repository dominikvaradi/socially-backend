package hu.dominikvaradi.sociallybackend.flows.user.domain;

import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.security.domain.enums.Role;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.EnumType.STRING;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User extends BaseDomain {
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "birth_country")
	private String birthCountry;

	@Column(name = "birth_city")
	private String birthCity;

	@Column(name = "current_country")
	private String currentCountry;

	@Column(name = "current_city")
	private String currentCity;

	@Enumerated(STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@ToString.Exclude
	@Builder.Default
	@OneToMany(mappedBy = "user")
	private Set<UserConversation> userConversations = new HashSet<>();

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