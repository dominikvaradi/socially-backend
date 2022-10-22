package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
public class RefreshTokenResponseDto {
	@NotNull
	private TokenResponseDto accessToken;

	@NotNull
	private TokenResponseDto refreshToken;
}
