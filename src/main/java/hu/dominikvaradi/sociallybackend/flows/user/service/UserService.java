package hu.dominikvaradi.sociallybackend.flows.user.service;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {
	Optional<User> findUserByPublicId(UUID userPublicId);

	Set<User> getUsersByName(String name);

	Set<User> getFriendsOfUser(UUID userPublicId);
}
