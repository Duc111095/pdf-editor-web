package com.ducnh.excellentPdf.model.exception;

public class BackupNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 3423775005598748785L;
	public BackupNotFoundException(String message) {
		super(message);
	}

}
