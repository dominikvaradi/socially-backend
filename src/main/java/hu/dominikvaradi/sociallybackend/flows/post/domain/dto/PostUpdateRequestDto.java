package hu.dominikvaradi.sociallybackend.flows.post.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostUpdateRequestDto {
	private String header;

	@NotBlank
	private String content;
}
