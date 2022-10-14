package hu.dominikvaradi.sociallybackend.flows.security.rest;

import hu.dominikvaradi.sociallybackend.flows.security.domain.JwtUserDetails;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.TokenResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.UserLoginRequestDto;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.UserLoginResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.service.JwtUtilService;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
import hu.dominikvaradi.sociallybackend.flows.user.transformers.User2UserCreateResponseDtoTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
	private final UserService userService;
	private final JwtUtilService jwtUtilService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/api/auth/register")
	public ResponseEntity<UserCreateResponseDto> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
		UserCreateResponseDto responseData = User2UserCreateResponseDtoTransformer.transform(userService.createUser(userCreateRequestDto));

		log.debug("User successfully created with email: " + responseData.getEmail());

		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/api/auth/login")
	public ResponseEntity<UserLoginResponseDto> loginUser(@RequestBody UserLoginRequestDto userLoginRequestDto) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
		User user = jwtUserDetails.getUser();

		TokenResponseDto accessToken = jwtUtilService.createJwtAccessTokenForUser(user);

		log.debug("User successfully logged in with email: " + user.getEmail());

		UserLoginResponseDto responseData = UserLoginResponseDto.builder()
				.userId(user.getPublicId())
				.userName(user.getName())
				.userEmail(user.getEmail())
				.accessToken(accessToken)
				.refreshToken(null) // TODO
				.build();

		return ResponseEntity.ok(responseData);
	}
}