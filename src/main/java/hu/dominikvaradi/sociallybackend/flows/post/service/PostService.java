package hu.dominikvaradi.sociallybackend.flows.post.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.EnumMap;
import java.util.UUID;

public interface PostService {
	Post findPostByPublicId(UUID postPublicId);

	Post createPost(User authorUser, User addresseeUser, PostCreateRequestDto postCreateRequestDto);

	Page<Post> findAllPostsOnUsersTimeline(User user, Pageable pageable);

	Page<Post> findAllPostsForUsersFeed(User user, Pageable pageable);

	Post updatePost(Post post, PostUpdateRequestDto postUpdateRequestDto);

	void deletePost(Post post);

	PostReaction addReactionToPost(Post post, User user, Reaction reaction);

	void deleteReactionFromPost(Post post, User user, Reaction reaction);

	Page<PostReaction> findAllReactionsByPost(Post post, Pageable pageable);

	EnumMap<Reaction, Long> findAllReactionCountsByPost(Post post);

	long findCommentCountByPost(Post post);
}
