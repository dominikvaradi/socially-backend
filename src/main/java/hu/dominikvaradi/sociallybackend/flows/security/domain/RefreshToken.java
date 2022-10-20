package hu.dominikvaradi.sociallybackend.flows.security.domain;

import hu.dominikvaradi.sociallybackend.flows.common.domain.BaseDomain;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseDomain {
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder.Default
	@Column(name = "token", nullable = false)
	private UUID token = UUID.randomUUID();

	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;
}
