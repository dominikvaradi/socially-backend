package hu.dominikvaradi.sociallybackend.flows.post.service;

import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentRepository;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.ReactionCountResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.common.exception.EntityConflictException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final PostReactionRepository postReactionRepository;
	private final CommentRepository commentRepository;
	private final FriendshipRepository friendshipRepository;

	@Override
	public Post findPostByPublicId(UUID postPublicId) {
		return postRepository.findByPublicId(postPublicId)
				.orElseThrow(() -> new EntityNotFoundException("POST_NOT_FOUND"));
	}

	@Override
	public Post createPost(User authorUser, User addresseeUser, PostCreateRequestDto postCreateRequestDto) {
		Post newPost = Post.builder()
				.author(authorUser)
				.addressee(addresseeUser)
				.header(postCreateRequestDto.getHeader())
				.content(postCreateRequestDto.getContent())
				.build();

		return postRepository.save(newPost);
	}

	@Override
	public Page<Post> findAllPostsOnUsersTimeline(User user, Pageable pageable) {
		return postRepository.findByAddresseeOrderByCreatedDesc(user, pageable);
	}

	@Override
	public Page<Post> findAllPostsForUsersFeed(User user, Pageable pageable) {
		Set<User> feedUsers = friendshipRepository.findAllAcceptedByUser(user)
				.stream()
				.map(fs -> fs.getRequester().equals(user) ? fs.getAddressee() : fs.getRequester())
				.collect(Collectors.toSet());

		feedUsers.add(user);

		return postRepository.findByAuthorOrUserIsInOrderByCreatedDesc(feedUsers, pageable);
	}

	@Override
	public Post updatePost(Post post, PostUpdateRequestDto postUpdateRequestDto) {
		post.setHeader(postUpdateRequestDto.getHeader());
		post.setContent(postUpdateRequestDto.getContent());

		return postRepository.save(post);
	}

	@Override
	public void deletePost(Post post) {
		postRepository.delete(post);
	}

	@Override
	public PostReaction addReactionToPost(Post post, User user, Reaction reaction) {
		if (postReactionRepository.findByUserAndPostAndReaction(user, post, reaction).isPresent()) {
			throw new EntityConflictException("REACTION_ALREADY_EXISTS_ON_POST_MADE_BY_USER");
		}

		PostReaction newCommentReaction = PostReaction.builder()
				.reaction(reaction)
				.user(user)
				.post(post)
				.build();

		return postReactionRepository.save(newCommentReaction);
	}

	@Override
	public void deleteReactionFromPost(Post post, User user, Reaction reaction) {
		PostReaction postReaction = postReactionRepository.findByUserAndPostAndReaction(user, post, reaction)
				.orElseThrow(() -> new EntityNotFoundException("REACTION_NOT_FOUND"));

		postReactionRepository.delete(postReaction);
	}

	@Override
	public Page<PostReaction> findAllReactionsByPost(Post post, Pageable pageable) {
		return postReactionRepository.findByPostOrderByUserNameAsc(post, pageable);
	}

	@Override
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

	@Override
	public long findCommentCountByPost(Post post) {
		return commentRepository.countByPost(post);
	}
}
