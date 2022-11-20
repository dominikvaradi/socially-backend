package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class UserUpdateRequestDto {
	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotNull
	private LocalDate birthDate;

	private String birthCountry;

	private String birthCity;

	private String currentCountry;

	private String currentCity;
}
