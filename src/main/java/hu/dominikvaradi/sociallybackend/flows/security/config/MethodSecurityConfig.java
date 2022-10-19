package hu.dominikvaradi.sociallybackend.flows.security.config;

import hu.dominikvaradi.sociallybackend.flows.friendship.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	private final FriendshipRepository friendshipRepository;

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler(friendshipRepository);
		expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
		
		return expressionHandler;
	}
}