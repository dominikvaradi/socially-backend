package hu.dominikvaradi.sociallybackend.flows.comment.domain.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CommentUpdateDto {
	private UUID id;
	String content;
}
