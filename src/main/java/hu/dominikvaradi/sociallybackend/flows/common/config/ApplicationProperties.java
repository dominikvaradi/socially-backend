package hu.dominikvaradi.sociallybackend.flows.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
	private Environment environment;
	private Jwt jwt;

	@Setter
	@Getter
	public static class Environment {
		private boolean testingEndpointsEnabled;
	}

	@Setter
	@Getter
	public static class Jwt {
		private String prefix;
		private String secret;
		private long accessTokenExpirationInMillis;
		private long refreshTokenExpirationInMillis;
		private String issuer;
	}
}
