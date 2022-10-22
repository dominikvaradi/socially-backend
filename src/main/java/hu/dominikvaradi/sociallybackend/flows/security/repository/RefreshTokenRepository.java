package hu.dominikvaradi.sociallybackend.flows.security.repository;

import hu.dominikvaradi.sociallybackend.flows.security.domain.RefreshToken;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(UUID token);

	Optional<RefreshToken> findByUser(User user);
}