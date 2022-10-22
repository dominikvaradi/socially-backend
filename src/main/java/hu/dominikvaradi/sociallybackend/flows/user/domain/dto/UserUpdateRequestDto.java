package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
public class UserUpdateRequestDto {
	@NotBlank
	private String name;

	private LocalDate birthDate;
	
	private String birthCountry;

	private String birthCity;

	private String currentCountry;

	private String currentCity;
}
