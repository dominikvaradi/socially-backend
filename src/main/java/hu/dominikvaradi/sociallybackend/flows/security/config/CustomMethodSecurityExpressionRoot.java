package hu.dominikvaradi.sociallybackend.flows.security.config;

import hu.dominikvaradi.sociallybackend.flows.conversation.domain.Conversation;
import hu.dominikvaradi.sociallybackend.flows.conversation.domain.UserConversation;
import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.security.domain.JwtUserDetails;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums.UserConversationRole.ADMIN;
import static hu.dominikvaradi.sociallybackend.flows.friendship.domain.enums.FriendshipStatus.FRIENDSHIP_REQUEST_ACCEPTED;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	private final FriendshipRepository friendshipRepository;

	private Object filterObject;
	private Object returnObject;
	private Object target;

	public CustomMethodSecurityExpressionRoot(Authentication authentication, FriendshipRepository friendshipRepository) {
		super(authentication);

		this.friendshipRepository = friendshipRepository;
	}

	public boolean isAuthenticationUserFriendOf(User otherUser) {
		User user = ((JwtUserDetails) this.getPrincipal()).getUser();

		return isUserFriendOf(user, otherUser);
	}

	public boolean isUserFriendOf(User user, User otherUser) {
		Optional<Friendship> friendship = friendshipRepository.findByRequesterAndAddressee(user, otherUser);

		return friendship.isPresent() && friendship.get().getStatus() == FRIENDSHIP_REQUEST_ACCEPTED;
	}

	public boolean isAuthenticationUserEqualsOrFriendOf(User otherUser) {
		User user = ((JwtUserDetails) this.getPrincipal()).getUser();

		return isUserEqualsOrFriendOf(user, otherUser);
	}

	public boolean isUserEqualsOrFriendOf(User user, User otherUser) {
		if (Objects.equals(user, otherUser)) {
			return true;
		}

		Optional<Friendship> friendship = friendshipRepository.findByRequesterAndAddressee(user, otherUser);

		return friendship.isPresent() && friendship.get().getStatus() == FRIENDSHIP_REQUEST_ACCEPTED;
	}

	public boolean isAuthenticationUserFriendOfOtherUsers(Collection<User> otherUsers) {
		User user = ((JwtUserDetails) this.getPrincipal()).getUser();

		return isUserFriendOfOtherUsers(user, otherUsers);
	}

	public boolean isUserFriendOfOtherUsers(User user, Collection<User> otherUsers) {
		if (otherUsers.isEmpty()) {
			return false;
		}

		for (User u : otherUsers) {
			if (!isUserFriendOf(user, u)) {
				return false;
			}
		}

		return true;
	}

	public boolean isAuthenticationUserMemberOfConversation(Conversation conversation) {
		User user = ((JwtUserDetails) this.getPrincipal()).getUser();

		return isUserMemberOfConversation(user, conversation);
	}

	public boolean isUserMemberOfConversation(User user, Conversation conversation) {
		for (UserConversation uc : conversation.getUserConversations()) {
			if (Objects.equals(uc.getUser(), user)) {
				return true;
			}
		}

		return false;
	}

	public boolean isAuthenticationUserAdminOfConversation(Conversation conversation) {
		User user = ((JwtUserDetails) this.getPrincipal()).getUser();

		return isUserAdminOfConversation(user, conversation);
	}

	public boolean isUserAdminOfConversation(User user, Conversation conversation) {
		for (UserConversation uc : conversation.getUserConversations()) {
			if (Objects.equals(uc.getUser(), user) && uc.getUserRole() == ADMIN) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	@Override
	public Object getFilterObject() {
		return filterObject;
	}

	@Override
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	@Override
	public Object getReturnObject() {
		return returnObject;
	}

	@Override
	public Object getThis() {
		return target;
	}
}