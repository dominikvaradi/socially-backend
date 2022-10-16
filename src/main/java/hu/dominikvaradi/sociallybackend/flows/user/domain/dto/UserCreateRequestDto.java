package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class UserCreateRequestDto {
	@NotBlank
	@Email(regexp = "[^@]+@[^@]+\\.[^@.]+")
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String name;

	@NotNull
	private LocalDate birthDate;

	private String birthCountry;

	private String birthCity;

	private String currentCountry;

	private String currentCity;
}
