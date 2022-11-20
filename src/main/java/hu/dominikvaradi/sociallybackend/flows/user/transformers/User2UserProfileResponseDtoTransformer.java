package hu.dominikvaradi.sociallybackend.flows.user.transformers;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserProfileResponseDto;

public class User2UserProfileResponseDtoTransformer {
	private User2UserProfileResponseDtoTransformer() {
		/* Static class */
	}

	public static UserProfileResponseDto transform(User user) {
		return UserProfileResponseDto.builder()
				.id(user.getPublicId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.birthDate(user.getBirthDate())
				.birthCountry(user.getBirthCountry())
				.birthCity(user.getBirthCity())
				.currentCountry(user.getCurrentCountry())
				.currentCity(user.getCurrentCity())
				.build();
	}
}
