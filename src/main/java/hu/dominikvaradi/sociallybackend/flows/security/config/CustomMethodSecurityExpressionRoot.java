package hu.dominikvaradi.sociallybackend.flows.security.config;

import hu.dominikvaradi.sociallybackend.flows.friendship.domain.Friendship;
import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import hu.dominikvaradi.sociallybackend.flows.security.domain.JwtUserDetails;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

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

	public boolean isUserFriendOf(User otherUser) {
		User user = ((JwtUserDetails) this.getPrincipal()).getUser();

		return areUsersFriends(user, otherUser);
	}

	public boolean areUsersFriends(User user1, User user2) {
		Optional<Friendship> friendship = friendshipRepository.findByRequesterAndAddressee(user1, user2);

		return friendship.isPresent() && friendship.get().getStatus() == FRIENDSHIP_REQUEST_ACCEPTED;
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