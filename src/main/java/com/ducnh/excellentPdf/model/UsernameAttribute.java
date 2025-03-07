package com.ducnh.excellentPdf.model;

public enum UsernameAttribute {
	EMAIL("email"),
	LOGIN("login"),
	PROFILE("profile"),
	NAME("name"),
	USERNAME("username"),
	NICKNAME("nickname"),
	GIVEN_NAME("given_name"),
	MIDDLE_NAME("middle_name"),
	FAMILY_NAME("family_name"),
	PREFERRED_NAME("preferred_name"),
	PREFERRED_USERNAME("preferred_username");
	
	private final String name;
	
	UsernameAttribute(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
