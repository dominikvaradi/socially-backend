package hu.dominikvaradi.sociallybackend.flows.post.domain.dto;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
	private String header;
	private String content;
}
