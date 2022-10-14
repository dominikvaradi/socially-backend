package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RefreshTokenResponseDto {
	private TokenResponseDto accessToken;
	private TokenResponseDto refreshToken;
}
