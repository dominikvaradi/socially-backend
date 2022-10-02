package hu.dominikvaradi.sociallybackend.flows.user.service;

import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final FriendshipRepository friendshipRepository;

	@Override
	public Optional<User> findUserByPublicId(UUID userPublicId) {
		return userRepository.findByPublicId(userPublicId);
	}

	@Override
	public User createUser(UserCreateRequestDto userCreateRequestDto) {
		Optional<User> existingUser = userRepository.findByEmail(userCreateRequestDto.getEmail());
		if (existingUser.isPresent()) {
			throw new RuntimeException(); // TODO REST Exception - email címmel már létezik user.
		}

		User newUser = User.builder()
				.email(userCreateRequestDto.getEmail())
				.password(userCreateRequestDto.getPassword()) // TODO encrypt password
				.name(userCreateRequestDto.getName())
				.birthDate(userCreateRequestDto.getBirthDate())
				.birthCountry(userCreateRequestDto.getBirthCountry())
				.birthCity(userCreateRequestDto.getBirthCity())
				.currentCountry(userCreateRequestDto.getCurrentCountry())
				.currentCity(userCreateRequestDto.getCurrentCity())
				.build();

		return userRepository.save(newUser);
	}

	@Override
	public User updateUser(User user, UserUpdateRequestDto userUpdateDto) {
		if (!user.getPublicId().equals(userUpdateDto.getId())) {
			throw new RuntimeException(); // TODO REST Exception - bad request, rossz id-t rakott a request bodyba.
		}

		user.setName(userUpdateDto.getName());
		user.setBirthDate(userUpdateDto.getBirthDate());
		user.setBirthCountry(userUpdateDto.getBirthCountry());
		user.setBirthCity(userUpdateDto.getBirthCity());
		user.setCurrentCountry(userUpdateDto.getCurrentCountry());
		user.setCurrentCity(userUpdateDto.getCurrentCity());

		return userRepository.save(user);
	}

	@Override
	public Page<User> findAllUsersByName(String name, Pageable pageable) {
		return userRepository.findByNameContainsIgnoreCase(name, pageable);
	}

	@Override
	public Page<User> findAllFriendsByUser(User user, Pageable pageable) {
		return friendshipRepository.findAllAcceptedByUser(user, pageable)
				.map(fs -> fs.getRequester().equals(user) ? fs.getAddressee() : fs.getRequester());
	}
}
