package hu.dominikvaradi.sociallybackend.flows.user.transformers;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateResponseDto;

public class User2UserCreateResponseDtoTransformer {
	private User2UserCreateResponseDtoTransformer() {
		/* Static class */
	}

	public static UserCreateResponseDto transform(User user) {
		return UserCreateResponseDto.builder()
				.id(user.getPublicId())
				.name(user.getName())
				.email(user.getEmail())
				.birthDate(user.getBirthDate())
				.birthCountry(user.getBirthCountry())
				.birthCity(user.getBirthCity())
				.currentCountry(user.getCurrentCountry())
				.currentCity(user.getCurrentCity())
				.build();
	}
}