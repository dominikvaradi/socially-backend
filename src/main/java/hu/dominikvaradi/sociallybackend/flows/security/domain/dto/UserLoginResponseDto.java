package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserLoginResponseDto {
	private UUID userId;
	private String userName;
	private String userEmail;
	private TokenResponseDto accessToken;
	private TokenResponseDto refreshToken;
}
