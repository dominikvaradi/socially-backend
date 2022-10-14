package hu.dominikvaradi.sociallybackend.flows.comment.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CommentResponseDto {
	private UUID id;
	private String content;
	private UUID postId;
	private UUID authorId;
	private String authorName;
	private ZonedDateTime created;
	EnumMap<Reaction, Long> reactionsCount;
}
