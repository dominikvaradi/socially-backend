package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserCreateRequestDto {
	private String email;
	private String password;
	private String name;
	private LocalDate birthDate;
	private String birthCountry;
	private String birthCity;
	private String currentCountry;
	private String currentCity;
}
