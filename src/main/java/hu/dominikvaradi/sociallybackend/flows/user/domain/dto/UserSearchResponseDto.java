package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@Setter
public class UserSearchResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;
}
