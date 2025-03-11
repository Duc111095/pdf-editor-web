package com.ducnh.excellentPdf.config;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;

import com.ducnh.excellentPdf.model.ApplicationProperties;
import com.ducnh.excellentPdf.model.ApplicationProperties.CustomPaths.Operations;
import com.ducnh.excellentPdf.model.ApplicationProperties.CustomPaths.Pipeline;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Getter
public class RuntimePathConfig {
	private final ApplicationProperties properties;
	private final String basePath;
	private final String weasyPrintPath;
	private final String unoConvertPath;
	
	// Pipeline paths
	private final String pipelineWatchedFoldersPath;
	private final String pipelineFinishedFoldersPath;
	private final String pipelineDefaultWebUiConfigs;
	private final String pipelinePath;
	
	public RuntimePathConfig(ApplicationProperties properties) {
		this.properties = properties;
		this.basePath = InstallationPathConfig.getPath();
		
		this.pipelinePath = Path.of(basePath, "pipeline").toString();
		String defaultWatchedFolders = Path.of(this.pipelinePath, "watchedFolders").toString();
		String defaultFinishedFolders = Path.of(this.pipelinePath, "finishedFolders").toString();
		String defaultWebUIConfigs = Path.of(this.pipelinePath, "defaultWebUIConfigs").toString();
		
		Pipeline pipeline = properties.getSystem().getCustomPaths().getPipeline();
		this.pipelineWatchedFoldersPath = 
				resolvePath(
					defaultWatchedFolders,
					pipeline != null ? pipeline.getWatchedFolderDir() : null);
		this.pipelineFinishedFoldersPath =
				resolvePath(
					defaultFinishedFolders,
					pipeline != null ? pipeline.getFinishedFolderDir() : null);
		this.pipelineDefaultWebUiConfigs =
				resolvePath(
					defaultWebUIConfigs,
					pipeline != null ? pipeline.getWebUIConfigDir() : null);
		boolean isDocker = isRunningInDocker();
		
		// Initialize Operation paths
		String defaultWeasyPrintPath = isDocker ? "/opt/venv/bin/weasyprint" : "weasyprint";
		String defaultUnoConvertPath = isDocker ? "/opt/venv/bin/unoconvert" : "unoconvert";
		
		Operations operations = properties.getSystem().getCustomPaths().getOperations();
		this.weasyPrintPath = 
				resolvePath(
					defaultWeasyPrintPath,
					operations != null ? operations.getWeasyprint() : null);
		this.unoConvertPath = 
				resolvePath(
					defaultUnoConvertPath,
					operations != null ? operations.getUnoconvert() : null);
				
	}
	
	private String resolvePath(String defaultPath, String customPath) {
		return StringUtils.isNotBlank(customPath) ? customPath : defaultPath;
	}
	
	private boolean isRunningInDocker() {
		return Files.exists(Path.of("/.dockerenv"));
	}

}
