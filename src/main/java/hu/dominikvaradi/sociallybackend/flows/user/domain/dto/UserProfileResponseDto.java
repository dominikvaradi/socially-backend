package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
public class UserProfileResponseDto {
	@NotNull
	private UUID id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	private LocalDate birthDate;

	private String birthCountry;

	private String birthCity;

	private String currentCountry;

	private String currentCity;
}
