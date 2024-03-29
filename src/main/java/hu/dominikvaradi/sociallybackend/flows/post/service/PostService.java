package hu.dominikvaradi.sociallybackend.flows.post.service;

import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentRepository;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityNotFoundException;
import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.post.repository.PostReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.post.repository.PostRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostReactionRepository postReactionRepository;
	private final CommentRepository commentRepository;
	private final FriendshipRepository friendshipRepository;

	@PostAuthorize("isAuthenticationUserEqualsOrFriendOf(returnObject.addressee)")
	public Post findPostByPublicId(UUID postPublicId) {
		return postRepository.findByPublicId(postPublicId)
				.orElseThrow(() -> new EntityNotFoundException("POST_NOT_FOUND"));
	}

	@PreAuthorize("authentication.principal.user == #authorUser && isUserEqualsOrFriendOf(#authorUser, #addresseeUser)")
	public Post createPost(User authorUser, User addresseeUser, PostCreateRequestDto postCreateRequestDto) {
		Post newPost = Post.builder()
				.author(authorUser)
				.addressee(addresseeUser)
				.header(postCreateRequestDto.getHeader())
				.content(postCreateRequestDto.getContent())
				.build();

		return postRepository.save(newPost);
	}

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#user)")
	public Page<Post> findAllPostsOnUsersTimeline(User user, Pageable pageable) {
		return postRepository.findByAddresseeOrderByCreatedDesc(user, pageable);
	}

	@PreAuthorize("authentication.principal.user == #user")
	public Page<Post> findAllPostsForUsersFeed(User user, Pageable pageable) {
		Set<User> feedUsers = friendshipRepository.findAllAcceptedByUser(user)
				.stream()
				.map(fs -> fs.getRequester().equals(user) ? fs.getAddressee() : fs.getRequester())
				.collect(Collectors.toSet());

		feedUsers.add(user);

		return postRepository.findByAddresseeIsInOrderByCreatedDesc(feedUsers, pageable);
	}

	@PreAuthorize("authentication.principal.user == #post.author")
	public Post updatePost(Post post, PostUpdateRequestDto postUpdateRequestDto) {
		post.setHeader(postUpdateRequestDto.getHeader());
		post.setContent(postUpdateRequestDto.getContent());

		return postRepository.save(post);
	}

	@PreAuthorize("authentication.principal.user == #post.author || authentication.principal.user == #post.addressee")
	public void deletePost(Post post) {
		postRepository.delete(post);
	}

	@PreAuthorize("authentication.principal.user == #user && isUserEqualsOrFriendOf(#user, #post.addressee)")
	public PostReaction toggleReactionOnPost(Post post, User user, Reaction reaction) {
		Optional<PostReaction> existingPostReaction = postReactionRepository.findByUserAndPost(user, post);
		if (existingPostReaction.isPresent()) {
			postReactionRepository.delete(existingPostReaction.get());

			if (existingPostReaction.get().getReaction() == reaction) {
				return null;
			}
		}

		PostReaction newPostReaction = PostReaction.builder()
				.reaction(reaction)
				.user(user)
				.post(post)
				.build();

		return postReactionRepository.save(newPostReaction);
	}

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	public Page<PostReaction> findAllReactionsByPost(Post post, Reaction reaction, Pageable pageable) {
		if (reaction == null) {
			return postReactionRepository.findByPostOrderByUserLastNameAsc(post, pageable);
		}

		return postReactionRepository.findByPostAndReactionOrderByUserLastNameAsc(post, reaction, pageable);
	}

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	public Set<ReactionCountResponseDto> findAllReactionCountsByPost(Post post) {
		Set<ReactionCountResponseDto> reactionCounts = new HashSet<>();

		for (Reaction reaction : Reaction.values()) {
			reactionCounts.add(ReactionCountResponseDto.builder()
					.reaction(reaction)
					.count(postReactionRepository.countByPostAndReaction(post, reaction))
					.build());
		}

		return reactionCounts;
	}

	@PreAuthorize("isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	public long findCommentCountByPost(Post post) {
		return commentRepository.countByPost(post);
	}

	@PreAuthorize("authentication.principal.user == #user && isAuthenticationUserEqualsOrFriendOf(#post.addressee)")
	public Reaction getUsersReactionByPost(User user, Post post) {
		Optional<PostReaction> postReaction = postReactionRepository.findByUserAndPost(user, post);
		if (postReaction.isEmpty()) {
			return null;
		}

		return postReaction.get().getReaction();
	}
}
