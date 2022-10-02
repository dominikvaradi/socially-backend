package hu.dominikvaradi.sociallybackend.flows.post.domain.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PostUpdateRequestDto {
	private UUID id;
	private String header;
	private String content;
}
