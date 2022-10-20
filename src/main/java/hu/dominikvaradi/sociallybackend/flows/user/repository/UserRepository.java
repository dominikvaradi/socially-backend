package hu.dominikvaradi.sociallybackend.flows.user.repository;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByPublicId(UUID publicId);

	Optional<User> findByEmail(String email);

	Page<User> findByNameContainsIgnoreCase(String name, Pageable pageable);

	Optional<User> findByNameIgnoreCase(String name);

	Set<User> findAllByPublicIdIsIn(Iterable<UUID> publicIds);

	@Modifying
	@Query(value = "truncate table users restart identity cascade", nativeQuery = true)
	void truncate();
}