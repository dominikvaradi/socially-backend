package hu.dominikvaradi.sociallybackend.flows.security.service;

import hu.dominikvaradi.sociallybackend.flows.common.config.ApplicationProperties;
import hu.dominikvaradi.sociallybackend.flows.security.domain.RefreshToken;
import hu.dominikvaradi.sociallybackend.flows.security.repository.RefreshTokenRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final ApplicationProperties applicationProperties;

	public Optional<RefreshToken> findRefreshTokenByToken(UUID token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(User user) {
		RefreshToken createdRefreshToken = RefreshToken.builder()
				.user(user)
				.expiresAt(Instant.now().plusMillis(applicationProperties.getJwt().getRefreshTokenExpirationInMillis()))
				.build();

		return refreshTokenRepository.save(createdRefreshToken);
	}

	public RefreshToken createAndDeleteExistingRefreshToken(User user) {
		Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUser(user);
		existingRefreshToken.ifPresent(refreshTokenRepository::delete);

		RefreshToken createdRefreshToken = RefreshToken.builder()
				.user(user)
				.expiresAt(Instant.now().plusMillis(applicationProperties.getJwt().getRefreshTokenExpirationInMillis()))
				.build();

		return refreshTokenRepository.save(createdRefreshToken);
	}

	public boolean isRefreshTokenExpired(RefreshToken refreshToken) {
		return refreshToken.getExpiresAt().compareTo(Instant.now()) < 0;
	}
}
