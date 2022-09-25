package hu.dominikvaradi.sociallybackend.flows.user.service;

import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserUpdateDto;
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
	public User createUser(UserCreateDto userCreateDto) {
		Optional<User> existingUser = userRepository.findByEmail(userCreateDto.getEmail());
		if (existingUser.isPresent()) {
			throw new RuntimeException(); // TODO REST Exception - email címmel már létezik user.
		}

		User newUser = User.builder()
				.email(userCreateDto.getEmail())
				.password(userCreateDto.getPassword()) // TODO encrypt password
				.name(userCreateDto.getName())
				.birthDate(userCreateDto.getBirthDate())
				.birthCountry(userCreateDto.getBirthCountry())
				.birthCity(userCreateDto.getBirthCity())
				.currentCountry(userCreateDto.getCurrentCountry())
				.currentCity(userCreateDto.getCurrentCity())
				.build();

		return userRepository.save(newUser);
	}

	@Override
	public User updateUser(User user, UserUpdateDto userUpdateDto) {
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
