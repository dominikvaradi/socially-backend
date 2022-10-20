package hu.dominikvaradi.sociallybackend.flows.security.service;

import hu.dominikvaradi.sociallybackend.flows.common.config.ApplicationProperties;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.TokenResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.exception.JwtAuthenticationFailedException;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtUtilServiceImpl implements JwtUtilService {
	private final ApplicationProperties applicationProperties;

	public TokenResponseDto createJwtAccessTokenForUser(User user) {
		Instant expiresAt = Instant.now()
				.plusMillis(applicationProperties.getJwt().getAccessTokenExpirationInMillis());

		String accessToken = Jwts.builder()
				.setIssuer(applicationProperties.getJwt().getIssuer())
				.setIssuedAt(new Date())
				.setExpiration(Date.from(expiresAt))
				.setSubject(user.getEmail())
				.signWith(getSigningKey())
				.compact();

		return TokenResponseDto.builder()
				.token(accessToken)
				.expiresAt(expiresAt.atZone(ZoneId.systemDefault()))
				.build();
	}

	public String decodeEmailFromJwtAccessToken(String accessToken) throws JwtAuthenticationFailedException {
		try {
			Claims claimsDecoded = Jwts.parserBuilder()
					.requireIssuer(applicationProperties.getJwt().getIssuer())
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(accessToken)
					.getBody();

			return claimsDecoded.getSubject();
		} catch (JwtException e) {
			throw new JwtAuthenticationFailedException("INVALID_ACCESS_TOKEN");
		}
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(applicationProperties.getJwt().getSecret());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
