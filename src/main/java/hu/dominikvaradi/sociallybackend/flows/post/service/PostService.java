package hu.dominikvaradi.sociallybackend.flows.post.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;
import java.util.UUID;

public interface PostService {
	@PostAuthorize("isAuthenticationUserEqualsOrFriendOf(returnObject.addressee)")
	Post findPostByPublicId(UUID postPublicId);

	@PreAuthorize("authentication.principal.user == #authorUser && isUserEqualsOrFriendOf(#authorUser, #addresseeUser)")
	Post createPost(User authorUser, User addresseeUser, PostCreateRequestDto postCreateRequestDto);

	@PreAuthorize("authentication.principal.user == #authorUser && isUserEqualsOrFriendOf(#authorUser, #addresseeUser)")
	Page<Post> findAllPostsOnUsersTimeline(User user, Pageable pageable);

	@PreAuthorize("authentication.principal.user == #user")
	Page<Post> findAllPostsForUsersFeed(User user, Pageable pageable);

	@PreAuthorize("authentication.principal.user == #post.author")
	Post updatePost(Post post, PostUpdateRequestDto postUpdateRequestDto);

	@PreAuthorize("authentication.principal.user == #post.author || authentication.principal.user == #post.addressee")
	void deletePost(Post post);

	@PreAuthorize("authentication.principal.user == #user && isUserEqualsOrFriendOf(#user, #post.addressee)")
	PostReaction addReactionToPost(Post post, User user, Reaction reaction);

	@PreAuthorize("authentication.principal.user == #user && isUserEqualsOrFriendOf(#user, #post.addressee)")
	void deleteReactionFromPost(Post post, User user, Reaction reaction);

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	Page<PostReaction> findAllReactionsByPost(Post post, Pageable pageable);

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	Set<ReactionCountResponseDto> findAllReactionCountsByPost(Post post);

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	long findCommentCountByPost(Post post);
}
