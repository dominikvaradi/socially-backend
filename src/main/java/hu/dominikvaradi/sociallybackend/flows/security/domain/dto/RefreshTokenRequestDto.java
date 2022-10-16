package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
public class RefreshTokenRequestDto {
	@NotBlank
	private UUID refreshToken;
}
