package com.ducnh.excellentPdf.controller.api.pipeline;

public interface UserServiceInterface {
	String getApiKeyForUser(String username);
	
	String getCurrentUsername();
	
	long getTotalUsersCount();
}
