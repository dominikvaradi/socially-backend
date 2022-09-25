package hu.dominikvaradi.sociallybackend.flows.user.service;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
	Optional<User> findUserByPublicId(UUID userPublicId);

	User createUser(UserCreateDto userCreateDto);

	User updateUser(User user, UserUpdateDto userUpdateDto);

	Page<User> findAllUsersByName(String name, Pageable pageable);

	Page<User> findAllFriendsByUser(User user, Pageable pageable);
}
