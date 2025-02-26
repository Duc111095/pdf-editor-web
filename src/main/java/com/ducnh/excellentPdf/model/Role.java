package com.ducnh.excellentPdf.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ducnh.excellentPdf.modelconstants.CommonConstant;

public enum Role {
	
	// Unlimited access
	ADMIN("ROLE_ADMIN", Integer.MAX_VALUE, Integer.MAX_VALUE, "adminUserSettings.admin"),
	
	// Unlimited access
	USER("ROLE_USER", Integer.MAX_VALUE, Integer.MAX_VALUE, "adminUserSettings.user" ),
	
	// 40 API calls Per Day, 40 web calls
	LIMITTED_API_USER("ROLE_LIMITED_API_USER", CommonConstant.LIMITED_API_CALLS_PER_DAY, CommonConstant.LIMITED_API_CALLS_PER_DAY, "adminUserSettings.apiUser"), 
	
	// 20 API calls Per Day, 20 web calls
	EXTRA_LIMITTED_API_USER("ROLE_EXTRA_LIMITTED_API_USER", CommonConstant.EXTRA_LIMITED_API_CALLS_PER_DAY, CommonConstant.EXTRA_LIMITED_API_CALLS_PER_DAY, "adminUserSettings.extraApiUser"),
	
	// 0 API calls per day and 20 web calls
	WEB_ONLY_USER("ROLE_WEB_ONLY_USER", 0, 20, "adminUserSettings.webOnlyUser"),
	
	INTERNAL_API_USER("STIRLING_PDF_BACKEND_API_USER", Integer.MAX_VALUE, Integer.MAX_VALUE, "adminUserSettings.internalApiUser"), 
	
	DEMO_USER("ROLE_DEMO_USER", 100, 100, "adminUserSettings.demoUser");
	
	
	private final String roleId;
	private final int apiCallsPerDay;
	private final int webCallsPerDay;
	private final String roleName;
	
	Role(String roleId, int apiCallsPerDay, int webCallsPerDay, String roleName) {
		this.roleId = roleId;
		this.apiCallsPerDay = apiCallsPerDay;
		this.webCallsPerDay = webCallsPerDay;
		this.roleName = roleName;
	}
	
	public static String getRoleNameByRoleId(String roleId) {
		// Using the fromString method to get the Role enum based on the roleId;
		Role role = fromString(roleId);
		// Return the roleName of the found Role enum
		return role.getRoleName();
	}
	
	// Method to retrieve all role IDs and role names
	public static Map<String, String> getAllRoleDetails() {
		// Using LinkedHashMap to preserve order
		Map<String, String> roleDetails = new LinkedHashMap<>();
		for (Role role : Role.values()) {
			roleDetails.put(role.getRoleId(), role.getRoleName());
		}
		return roleDetails;
	}
	
	public static Role fromString(String roleId) {
		for (Role role : Role.values()) {
			if (role.getRoleId().equalsIgnoreCase(roleId)) {
				return role;
			}
		}
		throw new IllegalArgumentException("No Role defined for id: " + roleId);
	}
	
	public String getRoleId() {
		return roleId;
	}
	
	public int getApiCallsPerDay() {
		return apiCallsPerDay;
	}
	
	public int getWebCallsPerDay() {
		return webCallsPerDay;
	}
	
	public String getRoleName() {
		return roleName;
	}
}
