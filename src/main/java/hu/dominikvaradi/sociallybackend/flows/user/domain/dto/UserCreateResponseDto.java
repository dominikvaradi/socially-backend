package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
public class UserCreateResponseDto {
	private UUID id;
	private String email;
	private String name;
	private LocalDate birthDate;
	private String birthCountry;
	private String birthCity;
	private String currentCountry;
	private String currentCity;
}
