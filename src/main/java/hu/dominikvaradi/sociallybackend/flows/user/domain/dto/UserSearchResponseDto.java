package hu.dominikvaradi.sociallybackend.flows.user.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserSearchResponseDto {
	UUID id;
	String name;
}
