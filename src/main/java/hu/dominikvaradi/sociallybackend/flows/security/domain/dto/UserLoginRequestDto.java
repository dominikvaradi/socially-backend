package hu.dominikvaradi.sociallybackend.flows.security.domain.dto;

import lombok.Getter;

@Getter
public class UserLoginRequestDto {
	private String email;
	private String password;
}
