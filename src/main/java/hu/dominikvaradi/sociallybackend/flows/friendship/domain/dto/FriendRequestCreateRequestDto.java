package hu.dominikvaradi.sociallybackend.flows.friendship.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class FriendRequestCreateRequestDto {
	@NotNull
	private UUID addresseeUserId;
}
