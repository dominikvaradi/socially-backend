package hu.dominikvaradi.sociallybackend.flows.security.service;

import hu.dominikvaradi.sociallybackend.flows.common.config.ApplicationProperties;
import hu.dominikvaradi.sociallybackend.flows.security.domain.RefreshToken;
import hu.dominikvaradi.sociallybackend.flows.security.repository.RefreshTokenRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final ApplicationProperties applicationProperties;

	@Override
	public Optional<RefreshToken> findRefreshTokenByToken(UUID token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public Optional<RefreshToken> findRefreshTokenByUser(User user) {
		return refreshTokenRepository.findByUser(user);
	}

	@Override
	public RefreshToken createRefreshToken(User user) {
		RefreshToken createdRefreshToken = RefreshToken.builder()
				.user(user)
				.expiresAt(Instant.now().plusMillis(applicationProperties.getJwt().getRefreshTokenExpirationInMillis()))
				.build();

		return refreshTokenRepository.save(createdRefreshToken);
	}

	@Override
	public boolean isRefreshTokenExpired(RefreshToken refreshToken) {
		return refreshToken.getExpiresAt().compareTo(Instant.now()) < 0;
	}

	@Override
	public void deleteRefreshToken(RefreshToken refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}
}
