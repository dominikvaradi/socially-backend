package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@Setter
public class UserLoginResponseDto {
	@NotNull
	private UUID userId;

	@NotNull
	private String userName;

	@NotNull
	private String userEmail;

	@NotNull
	private TokenResponseDto accessToken;

	@NotNull
	private TokenResponseDto refreshToken;
}
