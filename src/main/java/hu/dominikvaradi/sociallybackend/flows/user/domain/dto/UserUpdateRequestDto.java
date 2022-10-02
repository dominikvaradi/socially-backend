package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class UserUpdateRequestDto {
	private UUID id;
	private String name;
	private LocalDate birthDate;
	private String birthCountry;
	private String birthCity;
	private String currentCountry;
	private String currentCity;
}
