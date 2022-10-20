package hu.dominikvaradi.sociallybackend.flows.message.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MessageCreateRequestDto {
	@NotBlank
	private String content;
}
