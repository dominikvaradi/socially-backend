package hu.dominikvaradi.sociallybackend.flows.security.service;

import hu.dominikvaradi.sociallybackend.flows.security.domain.RefreshToken;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {
	Optional<RefreshToken> findRefreshTokenByToken(UUID token);

	Optional<RefreshToken> findRefreshTokenByUser(User user);

	RefreshToken createRefreshToken(User user);

	boolean isRefreshTokenExpired(RefreshToken refreshToken);

	void deleteRefreshToken(RefreshToken refreshToken);
}
