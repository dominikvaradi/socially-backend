package hu.dominikvaradi.sociallybackend.flows.comment.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentCreateRequestDto {
	@NotBlank
	String content;
}
