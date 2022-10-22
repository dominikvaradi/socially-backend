package hu.dominikvaradi.sociallybackend.flows.test.rest;

import hu.dominikvaradi.sociallybackend.flows.common.config.ApplicationProperties;
import hu.dominikvaradi.sociallybackend.flows.common.domain.dto.EmptyRestApiResponseDto;
import hu.dominikvaradi.sociallybackend.flows.common.exception.RestApiException;
import hu.dominikvaradi.sociallybackend.flows.test.service.TestDataService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.singletonList;

@Hidden
@RequiredArgsConstructor
@RestController
public class TestController {
	private final ApplicationProperties applicationProperties;
	private final TestDataService testDataService;

	@PostMapping("/api/test/reset-db")
	public ResponseEntity<EmptyRestApiResponseDto> resetTestData() {
		if (!applicationProperties.getEnvironment().isTestingEndpointsEnabled()) {
			throw new RestApiException("TESTING_ENDPOINTS_DISABLED", (short) 403);
		}

		testDataService.truncateAllExistingData();

		testDataService.createTestData();

		EmptyRestApiResponseDto responseData = EmptyRestApiResponseDto.builder()
				.messages(singletonList("Test data successfully reseted."))
				.build();

		return ResponseEntity.ok(responseData);
	}
}
