package com.ducnh.excellentPdf.model.provider;

import static com.ducnh.excellentPdf.model.UsernameAttribute.EMAIL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.ducnh.excellentPdf.model.UsernameAttribute;
import com.ducnh.excellentPdf.model.exception.UnsupportedUsernameAttribute;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Provider {
	public static final String EXCEPTION_MESSAGE = "The attribute %s is not supported for %s";
	
	private String issuer;
	private String name;
	private String clientName;
	private String clientId;
	private String clientSecret;
	private Collection<String> scopes;
	private UsernameAttribute useAsUsername;
	private String logoutUrl;
	private String authorizationUri;
	private String tokenUri;
	private String userInfoUri;
	
	public Provider(
			String issuer, 
			String name, 
			String clientName,
			String clientId,
			String clientSecret,
			Collection<String> scopes,
			UsernameAttribute useAsUsername,
			String logoutUrl,
			String authorizationUri,
			String tokenUri,
			String userInfoUri) {
		this.issuer = issuer;
		this.name = name;
		this.clientName = clientName;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scopes = scopes == null ? new ArrayList<>() : scopes;
		this.useAsUsername = useAsUsername != null ? validateUsernameAttribute(useAsUsername) : EMAIL;
		this.logoutUrl = logoutUrl;
		this.authorizationUri = authorizationUri;
		this.tokenUri = tokenUri;
		this.userInfoUri = userInfoUri;
	}
	
	public void setScopes(String scopes) {
		if (scopes != null && !scopes.isBlank()) {
			this.scopes = Arrays.stream(scopes.split(",")).map(String::trim).toList();
		}
	}
	
	private UsernameAttribute validateUsernameAttribute(UsernameAttribute usernameAttribute) {
		switch (name) {
			case "google" -> {
				return validateGoogleUsernameAttribute(usernameAttribute);
			}
			case "github" -> {
				return validateGithubUsernameAttribute(usernameAttribute);
			}
			case "keycloak" -> {
				return validateKeycloakUsernameAttribute(usernameAttribute);
			}
			default -> {
				return usernameAttribute;
			}
		}
	}
	
	private UsernameAttribute validateKeycloakUsernameAttribute(UsernameAttribute usernameAttribute) {
		switch (usernameAttribute) {
			case EMAIL, NAME, GIVEN_NAME, FAMILY_NAME, PREFERRED_USERNAME -> {
				return usernameAttribute;
			}
			default -> throw new UnsupportedUsernameAttribute(String.format(EXCEPTION_MESSAGE, usernameAttribute, clientName));
		}
	}
	
	private UsernameAttribute validateGoogleUsernameAttribute(UsernameAttribute usernameAttribute) {
		switch (usernameAttribute) {
			case EMAIL, NAME, GIVEN_NAME, FAMILY_NAME -> {
				return usernameAttribute;
			}
			default -> throw new UnsupportedUsernameAttribute(String.format(EXCEPTION_MESSAGE, usernameAttribute, clientName));
		}
	}
	
	private UsernameAttribute validateGithubUsernameAttribute(UsernameAttribute usernameAttribute) {
		switch (usernameAttribute) {
			case LOGIN, EMAIL, NAME -> {
				return usernameAttribute;
			}
			default -> throw new UnsupportedUsernameAttribute(String.format(EXCEPTION_MESSAGE, usernameAttribute, clientName));
		}
	}
	
	@Override
	public String toString() {
		return "Provider [name="
				+ getName()
				+ ", clientName="
				+ getClientName()
				+ ", clientId="
				+ getClientId()
				+ ", clientSecret"
				+ (getClientSecret() != null && !getClientSecret().isEmpty() ? "******" : "NULL")
				+ ", scopes="
				+ getScopes()
				+ ", useAsUsername="
				+ getUseAsUsername()
				+ "]";
	}
}
