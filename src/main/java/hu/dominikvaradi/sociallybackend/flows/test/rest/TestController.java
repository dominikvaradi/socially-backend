package hu.dominikvaradi.sociallybackend.flows.test.rest;

import hu.dominikvaradi.sociallybackend.flows.common.config.ApplicationProperties;
import hu.dominikvaradi.sociallybackend.flows.common.exception.RestApiException;
import hu.dominikvaradi.sociallybackend.flows.test.service.TestDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
	private final ApplicationProperties applicationProperties;
	private final TestDataService testDataService;

	@PostMapping("/api/test/reset-db")
	public ResponseEntity<String> resetTestData() {
		if (!applicationProperties.getEnvironment().isTestingEndpointsEnabled()) {
			throw new RestApiException("Testing endpoints disabled.", (short) 403);
		}

		testDataService.truncateAllExistingData();

		testDataService.createTestData();

		return ResponseEntity.ok("Test data successfully reseted.");
	}
}
