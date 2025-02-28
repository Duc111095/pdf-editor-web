package com.ducnh.excellentPdf.model.exception;

public class NoProviderFoundException extends Exception {

	private static final long serialVersionUID = 5958248668658779234L;
	
	public NoProviderFoundException(String message) {
		super(message);
	}
	
	public NoProviderFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
