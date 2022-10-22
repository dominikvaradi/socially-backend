package hu.dominikvaradi.sociallybackend.flows.user.service;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;
import java.util.UUID;

public interface UserService {
	User findUserByPublicId(UUID userPublicId);

	User createUser(UserCreateRequestDto userCreateRequestDto);

	@PreAuthorize("authentication.principal.user == #user")
	User updateUser(User user, UserUpdateRequestDto userUpdateDto);

	Page<User> findAllUsersByName(String name, Pageable pageable);

	Page<User> findAllFriendsByUser(User user, Pageable pageable);

	Set<User> findAllUsersByPublicIds(Set<UUID> userPublicIds);
}
