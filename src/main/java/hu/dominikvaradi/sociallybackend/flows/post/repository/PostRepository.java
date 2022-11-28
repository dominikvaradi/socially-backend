package hu.dominikvaradi.sociallybackend.flows.post.repository;

import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByPublicId(UUID publicId);

	Page<Post> findByAddresseeOrderByCreatedDesc(User user, Pageable pageable);

	Page<Post> findByAddresseeIsInOrderByCreatedDesc(Collection<User> users, Pageable pageable);

	@Modifying
	@Query(value = "truncate table posts restart identity cascade", nativeQuery = true)
	void truncate();
}