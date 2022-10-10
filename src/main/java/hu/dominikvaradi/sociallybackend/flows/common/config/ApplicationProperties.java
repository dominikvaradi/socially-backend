package hu.dominikvaradi.sociallybackend.flows.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
	private Environment environment;

	@Getter
	@Setter
	public static class Environment {
		private boolean testingEndpointsEnabled;
	}
}
