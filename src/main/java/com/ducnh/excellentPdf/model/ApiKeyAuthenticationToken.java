package com.ducnh.excellentPdf.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	private final Object principal;
	private Object credentials;

	public ApiKeyAuthenticationToken(String apiKey) {
		super(null);
		this.principal = null;
		this.credentials = apiKey;
		setAuthenticated(false);
	}
	
	public ApiKeyAuthenticationToken(
			Object principal, String apiKey,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = apiKey;
		super.setAuthenticated(true);
	}
	
	@Override
	public Object getCredentials() {
		return credentials;
	}
	
	@Override
	public Object getPrincipal() {
		return principal;
	}
	
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException("Cannot set this token to trusted. Use constructor which takes a GrantedAuthority list instead.");
		}
		super.setAuthenticated(false);
	}
	
	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		credentials = null;
	}

}
