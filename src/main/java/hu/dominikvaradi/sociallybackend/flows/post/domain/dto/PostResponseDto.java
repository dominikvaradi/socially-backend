package hu.dominikvaradi.sociallybackend.flows.post.domain.dto;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.UUID;

@Builder
@Getter
@Setter
public class PostResponseDto {
	UUID id;
	String header;
	String content;
	UUID authorId;
	String authorName;
	UUID addresseeId;
	String addresseeName;
	LocalDateTime created;
	EnumMap<Reaction, Integer> reactionsCount;
	int commentsCount;
}
