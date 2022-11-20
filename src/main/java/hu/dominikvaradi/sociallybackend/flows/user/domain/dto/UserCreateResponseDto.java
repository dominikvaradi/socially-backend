package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.TokenResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@Setter
public class UserCreateResponseDto {
	@NotNull
	private UUID userId;

	@NotNull
	private String userFirstName;

	@NotNull
	private String userLastName;

	@NotNull
	private TokenResponseDto accessToken;

	@NotNull
	private TokenResponseDto refreshToken;
}
