package hu.dominikvaradi.sociallybackend.flows.user.repository;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByPublicId(UUID publicId);

	Optional<User> findByEmail(String email);

	Page<User> findByNameContainsIgnoreCase(String name, Pageable pageable);
}