package hu.dominikvaradi.sociallybackend.flows.post.repository;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
	Page<PostReaction> findByPostOrderByUserLastNameAsc(Post post, Pageable pageable);

	Optional<PostReaction> findByUserAndPost(User user, Post post);

	long countByPostAndReaction(Post post, Reaction reaction);

	@Modifying
	@Query(value = "truncate table post_reactions restart identity cascade", nativeQuery = true)
	void truncate();
}