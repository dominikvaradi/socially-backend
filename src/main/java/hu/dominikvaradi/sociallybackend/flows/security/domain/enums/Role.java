package hu.dominikvaradi.sociallybackend.flows.security.domain.enums;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public enum Role {
	NORMAL_USER();

	@Getter
	private final Set<Permission> permissions = new HashSet<>();

	Role(Permission... permissions) {
		this.permissions.addAll(asList(permissions));
	}
}
