package hu.dominikvaradi.sociallybackend.flows.user.transformers;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserSearchResponseDto;

public class User2UserSearchResponseDtoTransformer {
	private User2UserSearchResponseDtoTransformer() {
		/* Static class */
	}

	public static UserSearchResponseDto transform(User user) {
		return UserSearchResponseDto.builder()
				.id(user.getPublicId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.build();
	}
}
