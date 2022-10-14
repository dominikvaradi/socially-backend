package hu.dominikvaradi.sociallybackend.flows.message.domain.dto;

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
public class MessageResponseDto {
	private UUID id;
	private UUID userId;
	private String userName;
	private UUID conversationId;
	private String content;
	private ZonedDateTime created;
	EnumMap<Reaction, Long> reactionsCount;
}
