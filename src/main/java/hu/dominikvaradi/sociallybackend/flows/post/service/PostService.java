package hu.dominikvaradi.sociallybackend.flows.post.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PostService {
	Optional<Post> findPostByPublicId(UUID postPublicId);

	Post createNewPost(UUID authorUserPublicId, UUID addresseeUserPublicId, PostCreateDto postCreateDto);

	Page<Post> getPostsOnUsersTimeline(UUID userPublicId, Pageable pageable);

	Page<Post> getFeedPostsForUser(UUID userPublicId, Pageable pageable);

	Post updatePost(UUID postPublicId, PostUpdateDto postUpdateDto);

	void deletePost(UUID postPublicId);

	PostReaction addReactionToPost(UUID postPublicId, UUID userPublicId, Reaction reaction);

	void deleteReactionFromPost(UUID postPublicId, UUID userPublicId, Reaction reaction);

	Set<PostReaction> getReactionsByPost(UUID postPublicId);

	Map<Reaction, Long> getReactionsCountByPost(UUID postPublicId);
}
