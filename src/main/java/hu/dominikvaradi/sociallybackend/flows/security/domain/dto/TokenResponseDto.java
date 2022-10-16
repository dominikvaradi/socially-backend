package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
public class TokenResponseDto {
	@NotNull
	private String token;

	@NotNull
	private ZonedDateTime expiresAt;
}
