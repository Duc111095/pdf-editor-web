package com.ducnh.excellentPdf.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.ducnh.excellentPdf.model.ApplicationProperties;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LanguageService {
	
	private final ApplicationProperties applicationProperties;
	private final PathMatchingResourcePatternResolver resourcePatternResolver =
			new PathMatchingResourcePatternResolver();
	
	public LanguageService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	public Set<String> getSupportedLanguages() {
		try {
			Resource[] resources = 
					resourcePatternResolver.getResources("classpath*:message_*.properties");
			
			return Arrays.stream(resources)
					.map(Resource::getFilename)
					.filter(
							filename -> 
									filename != null
											&& filename.startsWith("messages_")
											&& filename.endsWith(".properties"))
					.map(filename -> filename.replace("messsages_", "").replace(".properties", ""))
					.filter (
							languageCode -> {
								Set<String> allowedLanguagesSet =
										new HashSet<>(applicationProperties.getUi().getLanguages());
								return allowedLanguagesSet.isEmpty()
										|| allowedLanguagesSet.contains(languageCode)
										|| "en_GB".equals(languageCode);
							})
					.collect(Collectors.toSet());
		} catch (IOException e) {
			log.error("Error retrieving supported languages", e);
			return new HashSet<>();
		}
	}
}
