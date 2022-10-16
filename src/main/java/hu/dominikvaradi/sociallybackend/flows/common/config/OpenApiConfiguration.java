package hu.dominikvaradi.sociallybackend.flows.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
		name = "BearerToken",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
@OpenAPIDefinition(
		info = @Info(
				title = "Socially - Backend API",
				//version = "${api.version}",
				version = "1.0.0",
				contact = @Contact(
						name = "Dominik Váradi", email = "varadidominik2000@gmail.com" //, url = "https://www.baeldung.com"
				),
				//description = "${api.description}"
				description = "Socially is a full-stack web application for people, who want to socialize with other people."
		),
		servers = @Server(
				//url = "${api.server.url}",
				url = "http://localhost:8080",
				description = "Development"
		)
)
public class OpenApiConfiguration {
}