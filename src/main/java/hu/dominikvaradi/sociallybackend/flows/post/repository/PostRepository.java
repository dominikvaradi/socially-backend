package hu.dominikvaradi.sociallybackend.flows.post.repository;

import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByPublicId(UUID publicId);

	Page<Post> findByAddresseePublicIdOrderByCreatedDesc(UUID publicId, Pageable pageable);

	Page<Post> findByAuthorPublicIdIsInOrAddresseePublicIdIsInOrderByCreatedDesc(Collection<UUID> authorPublicIds, Collection<UUID> addresseePublicIds, Pageable pageable);
}