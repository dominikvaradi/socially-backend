package hu.dominikvaradi.sociallybackend.flows.security.domain;

import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
public class JwtUserDetails implements UserDetails {
	private User user;
	private Collection<GrantedAuthority> permissions;

	public JwtUserDetails(User user) {
		this.user = user;
		this.permissions = user.getRole().getPermissions()
				.stream()
				.map(p -> new SimpleGrantedAuthority(p.name()))
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissions;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
