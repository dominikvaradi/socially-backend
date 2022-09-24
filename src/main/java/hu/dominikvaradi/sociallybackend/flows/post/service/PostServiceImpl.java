package hu.dominikvaradi.sociallybackend.flows.post.service;

import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostCreateDto;
import hu.dominikvaradi.sociallybackend.flows.post.domain.dto.PostUpdateDto;
import hu.dominikvaradi.sociallybackend.flows.post.repository.PostReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.post.repository.PostRepository;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;
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
	private final UserService userService;

	@Override
	public Optional<Post> findPostByPublicId(UUID postPublicId) {
		return postRepository.findByPublicId(postPublicId);
	}

	@Override
	public Post createNewPost(UUID authorUserPublicId, UUID addresseeUserPublicId, PostCreateDto postCreateDto) {
		User author = userService.findUserByPublicId(authorUserPublicId).orElseThrow(); // TODO REST Exception 404
		User addressee = author;

		if (!Objects.equals(authorUserPublicId, addresseeUserPublicId)) {
			addressee = userService.findUserByPublicId(addresseeUserPublicId).orElseThrow(); // TODO REST Exception 404
		}

		Post newPost = Post.builder()
				.author(author)
				.addressee(addressee)
				.header(postCreateDto.getHeader())
				.content(postCreateDto.getContent())
				.build();

		return postRepository.save(newPost);
	}

	@Override
	public Page<Post> getPostsOnUsersTimeline(UUID userPublicId, Pageable pageable) {
		return postRepository.findByAddresseePublicIdOrderByCreatedDesc(userPublicId, pageable);
	}

	@Override
	public Page<Post> getFeedPostsForUser(UUID userPublicId, Pageable pageable) {
		Set<UUID> feedUserPublicIds = userService.getFriendsOfUser(userPublicId)
				.stream()
				.map(User::getPublicId)
				.collect(Collectors.toSet());

		feedUserPublicIds.add(userPublicId);

		return postRepository.findByAuthorPublicIdIsInOrAddresseePublicIdIsInOrderByCreatedDesc(feedUserPublicIds, feedUserPublicIds, pageable);
	}

	@Override
	public Post updatePost(UUID postPublicId, PostUpdateDto postUpdateDto) {
		Post post = postRepository.findByPublicId(postPublicId).orElseThrow(); // TODO REST Exception 404

		if (!Objects.equals(post.getPublicId(), postUpdateDto.getId())) {
			throw new RuntimeException(); // TODO REST Exception - bad request, rossz id-t rakott a request bodyba.
		}

		post.setHeader(postUpdateDto.getHeader());
		post.setContent(postUpdateDto.getContent());

		return postRepository.save(post);
	}

	@Override
	public void deletePost(UUID postPublicId) {
		Post post = postRepository.findByPublicId(postPublicId).orElseThrow(); // TODO REST Exception 404

		postRepository.delete(post);
	}

	@Override
	public PostReaction addReactionToPost(UUID postPublicId, UUID userPublicId, Reaction reaction) {
		Post post = postRepository.findByPublicId(postPublicId).orElseThrow(); // TODO REST Exception 404
		User user = userService.findUserByPublicId(userPublicId).orElseThrow(); // TODO REST Exception 404

		if (postReactionRepository.findByUserPublicIdAndPostPublicIdAndReaction(userPublicId, postPublicId, reaction).isPresent()) {
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
	public void deleteReactionFromPost(UUID postPublicId, UUID userPublicId, Reaction reaction) {
		PostReaction postReaction = postReactionRepository.findByUserPublicIdAndPostPublicIdAndReaction(userPublicId, postPublicId, reaction)
				.orElseThrow(); // TODO REST Exception 404

		postReactionRepository.delete(postReaction);
	}

	@Override
	public Set<PostReaction> getReactionsByPost(UUID postPublicId) {
		return postReactionRepository.findByPostPublicIdOrderByUserLastNameAsc(postPublicId);
	}

	@Override
	public Map<Reaction, Long> getReactionsCountByPost(UUID postPublicId) {
		Map<Reaction, Long> reactionCount = new EnumMap<>(Reaction.class);

		for (Reaction reaction : Reaction.values()) {
			reactionCount.put(reaction, postReactionRepository.countByPostPublicIdAndReaction(postPublicId, reaction));
		}

		return reactionCount;
	}
}
