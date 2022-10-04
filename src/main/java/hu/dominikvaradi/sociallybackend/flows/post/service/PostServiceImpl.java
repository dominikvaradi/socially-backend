package hu.dominikvaradi.sociallybackend.flows.post.service;

import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentRepository;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
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

import java.util.EnumMap;
import java.util.Objects;
import java.util.Optional;
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
	public Optional<Post> findPostByPublicId(UUID postPublicId) {
		return postRepository.findByPublicId(postPublicId);
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
		if (!Objects.equals(post.getPublicId(), postUpdateRequestDto.getId())) {
			throw new RuntimeException(); // TODO REST Exception - bad request, rossz id-t rakott a request bodyba.
		}

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
			throw new RuntimeException();
			// TODO REST Exception - már létezik az entity, conflict lenne.
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
				.orElseThrow(); // TODO REST Exception 404

		postReactionRepository.delete(postReaction);
	}

	@Override
	public Page<PostReaction> findAllReactionsByPost(Post post, Pageable pageable) {
		return postReactionRepository.findByPostOrderByUserNameAsc(post, pageable);
	}

	@Override
	public EnumMap<Reaction, Long> findAllReactionCountsByPost(Post post) {
		EnumMap<Reaction, Long> reactionCount = new EnumMap<>(Reaction.class);

		for (Reaction reaction : Reaction.values()) {
			reactionCount.put(reaction, postReactionRepository.countByPostAndReaction(post, reaction));
		}

		return reactionCount;
	}

	@Override
	public long findCommentCountByPost(Post post) {
		return commentRepository.countByPost(post);
	}
}
