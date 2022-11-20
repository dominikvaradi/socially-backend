package hu.dominikvaradi.sociallybackend.flows.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.Comment;
import hu.dominikvaradi.sociallybackend.flows.comment.domain.CommentReaction;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.comment.repository.CommentRepository;
import hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction;
import hu.dominikvaradi.sociallybackend.flows.common.exception.InternalServerErrorException;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.ConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.conversation.repository.UserConversationRepository;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.message.repository.MessageReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.message.repository.MessageRepository;
import hu.dominikvaradi.sociallybackend.flows.post.domain.Post;
import hu.dominikvaradi.sociallybackend.flows.post.domain.PostReaction;
import hu.dominikvaradi.sociallybackend.flows.post.repository.PostReactionRepository;
import hu.dominikvaradi.sociallybackend.flows.post.repository.PostRepository;
import hu.dominikvaradi.sociallybackend.flows.security.domain.enums.Role;
import hu.dominikvaradi.sociallybackend.flows.test.domain.TestUser;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.ANGRY;
import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.FUNNY;
import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.HEART;
import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.LIKE;
import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_REQUEST_ACCEPTED;

@RequiredArgsConstructor
@Transactional
@Service
public class TestDataServiceImpl implements TestDataService {
	private final UserRepository userRepository;
	private final FriendshipRepository friendshipRepository;
	private final PostRepository postRepository;
	private final PostReactionRepository postReactionRepository;
	private final CommentRepository commentRepository;
	private final CommentReactionRepository commentReactionRepository;
	private final ConversationRepository conversationRepository;
	private final UserConversationRepository userConversationRepository;
	private final MessageRepository messageRepository;
	private final MessageReactionRepository messageReactionRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void truncateAllExistingData() {
		userRepository.truncate();
		friendshipRepository.truncate();
		postRepository.truncate();
		postReactionRepository.truncate();
		commentRepository.truncate();
		commentReactionRepository.truncate();
		conversationRepository.truncate();
		userConversationRepository.truncate();
		messageRepository.truncate();
		messageReactionRepository.truncate();
	}

	@Override
	public void createTestData() {
		createTestUserData();
		createTestFriendshipData();
		createTestPostAndCommentData();
	}

	private void createTestUserData() {
		try {
			List<TestUser> testUsers = loadTestUsers();

			List<User> newUsers = new ArrayList<>();

			for (TestUser userData : testUsers) {
				newUsers.add(User.builder()
						.email(String.join(".", userData.getName().toLowerCase().split(" ")) + "@socially.hu")
						.password(passwordEncoder.encode(userData.getPassword()))
						.name(userData.getName())
						.birthDate(userData.getBirthDate())
						.birthCountry(userData.getCountry())
						.birthCity(userData.getCity())
						.currentCountry(userData.getCountry())
						.currentCity(userData.getCity())
						.role(Role.NORMAL_USER)
						.build());
			}

			userRepository.saveAll(newUsers);
		} catch (IOException e) {
			throw new InternalServerErrorException("CANNOT_LOAD_TEST_USERS", e);
		}
	}

	private void createTestFriendshipData() {
		List<Friendship> newFriendships = new ArrayList<>();

		User rick = findUserByName("Rick Sanchez");
		User morty = findUserByName("Morty Smith");
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, morty));

		User eren = findUserByName("Eren Yaeger");
		User mikasa = findUserByName("Mikasa Ackerman");
		User levi = findUserByName("Levi Ackerman");
		newFriendships.addAll(createEveryPossibleFriendshipsBetweenUsers(eren, mikasa, levi));

		User naruto = findUserByName("Naruto Uzumaki");
		User hinata = findUserByName("Hinata Hyuga");
		User sakura = findUserByName("Sakura Haruno");
		User itachi = findUserByName("Itachi Uchiha");
		User sasuke = findUserByName("Sasuke Uchiha");
		newFriendships.addAll(createEveryPossibleFriendshipsBetweenUsers(naruto, hinata, sakura, itachi, sasuke));

		User tanjiro = findUserByName("Tanjiro Kamado");
		User nezuko = findUserByName("Nezuko Kamado");
		User kanao = findUserByName("Kanao Tsuyuri");
		User zenitsu = findUserByName("Zenitsu Agatsuma");
		newFriendships.addAll(createEveryPossibleFriendshipsBetweenUsers(tanjiro, nezuko, kanao, zenitsu));

		User takumi = findUserByName("Takumi Fujiwara");
		User ryosuke = findUserByName("Ryosuke Takahashi");
		User mako = findUserByName("Mako Sato");
		newFriendships.addAll(createEveryPossibleFriendshipsBetweenUsers(takumi, ryosuke, mako));

		friendshipRepository.saveAll(newFriendships);
	}

	private List<Friendship> createEveryPossibleFriendshipsBetweenUsers(User... users) {
		List<Friendship> createdFriendships = new ArrayList<>();

		for (int i = 0; i < users.length - 1; ++i) {
			for (int j = i + 1; j < users.length; ++j) {
				createdFriendships.add(createFriendshipBetweenTwoUsers(users[i], users[j]));
			}
		}

		return createdFriendships;
	}

	private Friendship createFriendshipBetweenTwoUsers(User requester, User addressee) {
		return Friendship.builder()
				.requester(requester)
				.addressee(addressee)
				.lastStatusModifier(addressee)
				.status(FRIENDSHIP_REQUEST_ACCEPTED)
				.statusLastModified(Instant.now())
				.build();
	}

	private void createTestPostAndCommentData() {
		List<Post> newPosts = new ArrayList<>();

		User morty = findUserByName("Morty Smith");
		User rick = findUserByName("Rick Sanchez");
		User naruto = findUserByName("Naruto Uzumaki");
		User hinata = findUserByName("Hinata Hyuga");
		User sakura = findUserByName("Sakura Haruno");
		User sasuke = findUserByName("Sasuke Uchiha");
		User itachi = findUserByName("Itachi Uchiha");
		User levi = findUserByName("Levi Ackerman");
		User mikasa = findUserByName("Mikasa Ackerman");
		User eren = findUserByName("Eren Yaeger");
		User takumi = findUserByName("Takumi Fujiwara");
		User ryosuke = findUserByName("Ryosuke Takahashi");
		User mako = findUserByName("Mako Sato");

		Post postMorty = createPostForUser(morty, "I'm gonna kill Rick!", "So.. How do I say it?\n" +
				"Yesterday Rick teleported me to an other dimension, where these Minecraft lookalike elephant creatures were chasing me like hell.\n" +
				"I almost f*kin died out there, Rick you fool!");
		addReactionToPost(rick, postMorty, FUNNY);
		Comment commentRick = createCommentOnPost(rick, postMorty, "HAHHHAAA THATT WASS SOO FUNNYY");
		addReactionToComment(morty, commentRick, ANGRY);

		Post postNaruto = createPostForUser(naruto, null, "I AM THE HOKAGE NOW!");
		addReactionToPost(hinata, postNaruto, HEART);
		addReactionToPost(sakura, postNaruto, HEART);
		addReactionToPost(sasuke, postNaruto, LIKE);
		addReactionToPost(itachi, postNaruto, LIKE);
		Comment commentSasuke = createCommentOnPost(sasuke, postNaruto, "Well done, scaredy cat. :)");
		addReactionToComment(naruto, commentSasuke, FUNNY);
		addReactionToComment(sakura, commentSasuke, FUNNY);
		Comment commentHinata = createCommentOnPost(hinata, postNaruto, "I'm so proud of you, my love! <3");
		addReactionToComment(naruto, commentHinata, HEART);

		Post postHinata1 = createPostForUser(hinata, "I love you, Naruto-kun!", "I'm in sooo much love with you, my dear Naruto-kun!\n" +
				"I'm glad that you became hokage and reached your dreams, my love.\n");
		addReactionToPost(naruto, postHinata1, HEART);
		addReactionToPost(sakura, postHinata1, HEART);

		Post postHinata2 = createPostForUser(hinata, "Wedding", "We're gonna celebrate our weeding with Naruto-kun next week, on Saturday, soo all citizen of Konoha will be invited!!");
		addReactionToPost(naruto, postHinata2, HEART);
		addReactionToPost(sakura, postHinata2, HEART);
		addReactionToPost(sasuke, postHinata2, LIKE);
		addReactionToPost(itachi, postHinata2, LIKE);

		Post postLevi = createPostForUser(levi, null, "I am the strongest man in the world, so what's up?");
		addReactionToPost(mikasa, postLevi, ANGRY);
		addReactionToPost(eren, postLevi, ANGRY);

		Post postTakumi = createPostForUser(takumi, "Pure drifting", "Yesterday I have made my most perfect drifting of all on Akina mt.\n" +
				"Tomorrow i'll upload the footage of that sick sliding.");
		addReactionToPost(ryosuke, postTakumi, LIKE);
		addReactionToPost(mako, postTakumi, HEART);
		createCommentOnPost(ryosuke, postTakumi, "Well done, trainee! ;)");

		newPosts.add(postMorty);
		newPosts.add(postNaruto);
		newPosts.add(postHinata1);
		newPosts.add(postHinata2);
		newPosts.add(postLevi);
		newPosts.add(postTakumi);
		postRepository.saveAll(newPosts);
	}

	private Post createPostForUser(User user, String header, String content) {
		return Post.builder()
				.header(header)
				.content(content)
				.author(user)
				.addressee(user)
				.build();
	}

	private void addReactionToPost(User user, Post post, Reaction reaction) {
		post.getReactions().add(PostReaction.builder()
				.user(user)
				.post(post)
				.reaction(reaction)
				.build());
	}

	private Comment createCommentOnPost(User user, Post post, String content) {
		Comment createdComment = Comment.builder()
				.user(user)
				.post(post)
				.content(content)
				.build();

		post.getComments().add(createdComment);

		return createdComment;
	}

	private void addReactionToComment(User user, Comment comment, Reaction reaction) {
		comment.getReactions().add(CommentReaction.builder()
				.user(user)
				.comment(comment)
				.reaction(reaction)
				.build());
	}

	private User findUserByName(String name) {
		return userRepository.findByNameIgnoreCase(name)
				.orElseThrow(() -> new InternalServerErrorException("INTERNAL_SERVER_ERROR"));
	}

	private List<TestUser> loadTestUsers() throws IOException {
		Resource testUsersResource = new ClassPathResource("test/test-users.json");

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		return Arrays.asList(mapper.readValue(testUsersResource.getInputStream(), TestUser[].class));
	}
}