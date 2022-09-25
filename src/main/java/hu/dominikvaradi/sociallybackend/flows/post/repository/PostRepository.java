package hu.dominikvaradi.sociallybackend.flows.post.repository;

import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByPublicId(UUID publicId);

	Page<Post> findByAddresseeOrderByCreatedDesc(User user, Pageable pageable);

	@Query("select p from Post p where p.author in ?1 or p.addressee in ?1 order by p.created DESC")
	Page<Post> findByAuthorOrUserIsInOrderByCreatedDesc(Collection<User> users, Pageable pageable);
}