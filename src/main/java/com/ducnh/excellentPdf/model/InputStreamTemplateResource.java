package com.ducnh.excellentPdf.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.thymeleaf.templateresource.ITemplateResource;

public class InputStreamTemplateResource implements ITemplateResource {
	private InputStream inputStream;
	private String characterEncoding;

	public InputStreamTemplateResource(InputStream inputStream, String characterEncoding) {
		this.inputStream = inputStream;
		this.characterEncoding = characterEncoding;
	}
	
	@Override
	public String getDescription() {
		return "InputStream resource [Stream]";
	}

	@Override
	public String getBaseName() {
		return "streamResource";
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public Reader reader() throws IOException {
		return new InputStreamReader(inputStream, characterEncoding);
	}

	@Override
	public ITemplateResource relative(String relativeLocation) {
		throw new UnsupportedOperationException("Relative resources not supported");
	}

}
