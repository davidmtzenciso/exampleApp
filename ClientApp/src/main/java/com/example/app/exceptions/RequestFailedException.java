package com.example.app.exceptions;

public class RequestFailedException extends Exception {

	private final String message;
	private static final long serialVersionUID = 1L;
	
	public RequestFailedException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
