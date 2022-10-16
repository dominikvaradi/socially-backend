package hu.dominikvaradi.sociallybackend.flows.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.dominikvaradi.sociallybackend.flows.common.config.ApplicationProperties;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.EmptyRestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.exception.JwtAuthenticationFailedException;
import hu.dominikvaradi.sociallybackend.flows.security.service.JwtUserDetailsService;
import hu.dominikvaradi.sociallybackend.flows.security.service.JwtUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.singletonList;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAccessTokenFilter extends OncePerRequestFilter {
	private final ApplicationProperties applicationProperties;
	private final JwtUtilService jwtUtilService;
	private final JwtUserDetailsService jwtUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String prefix = applicationProperties.getJwt().getPrefix();

		String authorizationHeader = request.getHeader("Authorization");
		if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(prefix)) {
			String jwtAccessToken = authorizationHeader.substring(prefix.length()).trim();

			try {
				String email = jwtUtilService.decodeEmailFromJwtAccessToken(jwtAccessToken);

				UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				log.debug("Authentication succeeded for user, email: " + userDetails.getUsername());
			} catch (JwtAuthenticationFailedException e) {
				log.debug("Authentication failed for user, invalid access token provided by user");
			} catch (Exception e) {
				log.error("Internal server error happened while authenticating a user (validating access-token): " + e.getMessage(), e);

				ObjectMapper mapper = new ObjectMapper();
				EmptyRestApiResponseDto responseData = EmptyRestApiResponseDto.builder()
						.httpStatusCode(INTERNAL_SERVER_ERROR.value())
						.messages(singletonList("INTERNAL_SERVER_ERROR"))
						.build();

				response.setStatus(SC_INTERNAL_SERVER_ERROR);
				response.setHeader("Content-Type", "application/json");
				response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseData));

				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
