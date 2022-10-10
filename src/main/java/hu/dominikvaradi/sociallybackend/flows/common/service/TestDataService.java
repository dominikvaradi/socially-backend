package hu.dominikvaradi.sociallybackend.flows.common.service;

public interface TestDataService {
	void truncateAllExistingData();
	
	void createTestData();
}
