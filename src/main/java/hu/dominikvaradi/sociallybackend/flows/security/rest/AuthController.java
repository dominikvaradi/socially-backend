package hu.dominikvaradi.sociallybackend.flows.security.rest;

import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.RestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.domain.JwtUserDetails;
import hu.dominikvaradi.sociallybackend.flows.security.domain.RefreshToken;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.RefreshTokenRequestDto;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.RefreshTokenResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.TokenResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.UserLoginRequestDto;
import hu.dominikvaradi.sociallybackend.flows.security.domain.dto.UserLoginResponseDto;
import hu.dominikvaradi.sociallybackend.flows.security.exception.RefreshTokenVerificationException;
import hu.dominikvaradi.sociallybackend.flows.security.service.JwtUtilService;
import hu.dominikvaradi.sociallybackend.flows.security.service.RefreshTokenService;
import hu.dominikvaradi.sociallybackend.flows.user.domain.User;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateRequestDto;
import hu.dominikvaradi.sociallybackend.flows.user.domain.dto.UserCreateResponseDto;
import hu.dominikvaradi.sociallybackend.flows.user.service.UserService;
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

import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
	private final UserService userService;
	private final RefreshTokenService refreshTokenService;
	private final JwtUtilService jwtUtilService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/auth/register")
	public ResponseEntity<RestApiResponseDto<UserCreateResponseDto>> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
		User createdUser = userService.createUser(userCreateRequestDto);

		TokenResponseDto accessToken = jwtUtilService.createJwtAccessTokenForUser(createdUser);

		RefreshToken createdRefreshToken = refreshTokenService.createRefreshToken(createdUser);
		TokenResponseDto refreshTokenResponseData = TokenResponseDto.builder()
				.token(createdRefreshToken.getToken().toString())
				.expiresAt(createdRefreshToken.getExpiresAt().atZone(ZoneId.systemDefault()))
				.build();

		log.debug("User successfully created and logged in with email: " + createdUser.getEmail());

		UserCreateResponseDto responseData = UserCreateResponseDto.builder()
				.userId(createdUser.getPublicId())
				.userFirstName(createdUser.getFirstName())
				.userLastName(createdUser.getLastName())
				.accessToken(accessToken)
				.refreshToken(refreshTokenResponseData)
				.build();

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PostMapping("/auth/login")
	public ResponseEntity<RestApiResponseDto<UserLoginResponseDto>> loginUser(@RequestBody UserLoginRequestDto userLoginRequestDto) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
		User user = jwtUserDetails.getUser();

		TokenResponseDto accessToken = jwtUtilService.createJwtAccessTokenForUser(user);

		Optional<RefreshToken> existingRefreshToken = refreshTokenService.findRefreshTokenByUser(user);
		existingRefreshToken.ifPresent(refreshTokenService::deleteRefreshToken);

		RefreshToken createdRefreshToken = refreshTokenService.createRefreshToken(user);

		TokenResponseDto refreshTokenResponseData = TokenResponseDto.builder()
				.token(createdRefreshToken.getToken().toString())
				.expiresAt(createdRefreshToken.getExpiresAt().atZone(ZoneId.systemDefault()))
				.build();

		log.debug("User successfully logged in with email: " + user.getEmail());

		UserLoginResponseDto responseData = UserLoginResponseDto.builder()
				.userId(user.getPublicId())
				.userFirstName(user.getFirstName())
				.userLastName(user.getLastName())
				.accessToken(accessToken)
				.refreshToken(refreshTokenResponseData)
				.build();

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}

	@PostMapping("/auth/refresh-token")
	public ResponseEntity<RestApiResponseDto<RefreshTokenResponseDto>> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
		Optional<RefreshToken> foundRefreshToken = refreshTokenService.findRefreshTokenByToken(refreshTokenRequestDto.getRefreshToken());
		if (foundRefreshToken.isEmpty()) {
			throw new RefreshTokenVerificationException("REFRESH_TOKEN_NOT_FOUND");
		}

		RefreshToken oldRefreshToken = foundRefreshToken.get();
		if (refreshTokenService.isRefreshTokenExpired(oldRefreshToken)) {
			refreshTokenService.deleteRefreshToken(oldRefreshToken);
			throw new RefreshTokenVerificationException("REFRESH_TOKEN_EXPIRED");
		}

		User user = oldRefreshToken.getUser();
		RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

		refreshTokenService.deleteRefreshToken(oldRefreshToken);

		TokenResponseDto accessToken = jwtUtilService.createJwtAccessTokenForUser(user);
		TokenResponseDto refreshTokenResponseData = TokenResponseDto.builder()
				.token(newRefreshToken.getToken().toString())
				.expiresAt(newRefreshToken.getExpiresAt().atZone(ZoneId.systemDefault()))
				.build();

		log.debug("Tokens successfully refreshed for user: " + user.getEmail());

		RefreshTokenResponseDto responseData = RefreshTokenResponseDto.builder()
				.accessToken(accessToken)
				.refreshToken(refreshTokenResponseData)
				.build();

		return ResponseEntity.ok(RestApiResponseDto.buildFromDataWithoutMessages(responseData));
	}
}