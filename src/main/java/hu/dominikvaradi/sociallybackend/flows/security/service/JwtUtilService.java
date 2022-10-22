package hu.dominikvaradi.sociallybackend.flows.security.service;

import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.TokenResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.exception.JwtAuthenticationFailedException;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;

public interface JwtUtilService {
	TokenResponseDto createJwtAccessTokenForUser(User user);

	String decodeEmailFromJwtAccessToken(String accessToken) throws JwtAuthenticationFailedException;
}
