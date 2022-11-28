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
import java.util.Objects;
import java.util.stream.Collectors;

import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.ANGRY;
import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.FUNNY;
import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.HEART;
import static hu.dominikvaradi.sociallybackend.flows.common.domain.enums.Reaction.LIKE;
import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_REQUEST_ACCEPTED;

@RequiredArgsConstructor
@Transactional
@Service
public class TestDataService {
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
						.email((userData.getFirstName().toLowerCase()) + "." + (userData.getLastName().toLowerCase()) + "@socially.hu")
						.password(passwordEncoder.encode(userData.getPassword()))
						.firstName(userData.getFirstName())
						.lastName(userData.getLastName())
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

		User rick = findUserByName("Rick Sanchez");
		User morty = findUserByName("Morty Smith");
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, eren));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, mikasa));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, levi));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, naruto));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, hinata));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, sakura));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, itachi));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, sasuke));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, tanjiro));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, nezuko));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, kanao));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, zenitsu));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, takumi));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, ryosuke));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, mako));
		newFriendships.add(createFriendshipBetweenTwoUsers(rick, morty));

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

		String shortContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
		String mediumContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		String longContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur euismod libero vitae dui laoreet pretium. Fusce imperdiet, dui at cursus ullamcorper, est eros semper sem, at sodales ligula risus quis purus. Aenean eu lacinia felis. Phasellus at lorem nec lorem consectetur varius. Vestibulum ullamcorper lobortis facilisis. Nulla id vulputate ex, vel congue lectus. Proin in ligula at nunc auctor tincidunt eget ac neque. Aenean vel orci ornare, mattis quam aliquet, pellentesque velit. Morbi bibendum dolor vitae lacus tincidunt, nec rutrum arcu rutrum. Nulla facilisi. Pellentesque molestie magna ac magna lacinia faucibus. Donec cursus venenatis erat et viverra.\n" +
				"\n" +
				"Donec in mi commodo, rhoncus diam nec, aliquet sapien. Nunc convallis erat vitae efficitur sollicitudin. Aliquam id molestie mauris, vel elementum dolor. Proin elementum pretium vulputate. In ornare ex nunc, in faucibus risus accumsan in. Nam sed semper urna. Integer aliquam porttitor nulla, ut posuere orci aliquet sit amet. Quisque condimentum ut ipsum quis accumsan. Vivamus dignissim felis a nibh bibendum consequat. Mauris sed metus quis turpis venenatis consectetur eu sit amet eros. Donec condimentum, arcu sed pretium tincidunt, felis augue iaculis nunc, ut sagittis sem nisl eget lectus. Sed sollicitudin magna quis diam consectetur, non pellentesque sapien sollicitudin.\n" +
				"\n" +
				"Duis vestibulum justo at sem pellentesque congue. Nunc elementum scelerisque urna, et maximus nunc fringilla quis. Vivamus ultricies in mauris ut rutrum. Pellentesque finibus ornare ante, non efficitur ligula euismod ut. Ut efficitur nisi vitae ex scelerisque mattis. Curabitur rhoncus pulvinar neque. Suspendisse id eros id lacus egestas porta. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.";

		List<User> users = userRepository.findAll();
		for (User user : users) {
			List<User> friendsOfUser = friendshipRepository.findAllAcceptedByUser(user)
					.stream()
					.map(friendship -> (Objects.equals(user, friendship.getRequester())) ? friendship.getAddressee() : friendship.getRequester())
					.collect(Collectors.toList());


			final long postCount = ((user.getId() % 3) * 5) + ((user.getId() % 2) * 6);
			for (int i = 0; i < postCount; ++i) {
				String header = (i % 2 == 0) ? ("Post title #" + i) : null;
				String content = shortContent;
				if (i % 3 == 1) {
					content = mediumContent;
				} else if (i % 3 == 2) {
					content = longContent;
				}

				Post postByUser = Post.builder()
						.header(header)
						.content(content)
						.author(user)
						.addressee(user)
						.build();

				for (User friendOfUser : friendsOfUser) {
					Reaction reaction;
					switch ((int) (friendOfUser.getId() % 4)) {
						case 1:
							reaction = HEART;
							break;
						case 2:
							reaction = FUNNY;
							break;
						case 3:
							reaction = ANGRY;
							break;
						default:
							reaction = LIKE;
							break;
					}

					postByUser.getReactions().add(PostReaction.builder()
							.user(friendOfUser)
							.post(postByUser)
							.reaction(reaction)
							.build());

					Comment createdCommentOnPost = Comment.builder()
							.user(friendOfUser)
							.post(postByUser)
							.content(shortContent)
							.build();

					postByUser.getComments().add(createdCommentOnPost);
				}

				for (Comment commentByPost : postByUser.getComments()) {
					for (User friendOfUser : friendsOfUser) {
						Reaction reaction;
						switch ((int) (friendOfUser.getId() % 4)) {
							case 1:
								reaction = HEART;
								break;
							case 2:
								reaction = FUNNY;
								break;
							case 3:
								reaction = ANGRY;
								break;
							default:
								reaction = LIKE;
								break;
						}

						commentByPost.getReactions().add(CommentReaction.builder()
								.user(friendOfUser)
								.comment(commentByPost)
								.reaction(reaction)
								.build());
					}
				}

				newPosts.add(postByUser);
			}
		}

		postRepository.saveAll(newPosts);
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