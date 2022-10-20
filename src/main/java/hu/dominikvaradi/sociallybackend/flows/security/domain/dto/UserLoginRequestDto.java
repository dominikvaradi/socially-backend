package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserLoginRequestDto {
	@NotBlank
	private String email;

	@NotBlank
	private String password;
}
