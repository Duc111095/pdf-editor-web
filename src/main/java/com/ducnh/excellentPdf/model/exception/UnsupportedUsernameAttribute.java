package com.ducnh.excellentPdf.model.exception;

public class UnsupportedUsernameAttribute extends RuntimeException {
	private static final long serialVersionUID = 7266065740513275118L;

	public UnsupportedUsernameAttribute(String message) {
		super(message);
	}
}
