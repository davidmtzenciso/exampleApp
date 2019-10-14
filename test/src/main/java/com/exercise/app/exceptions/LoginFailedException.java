package com.exercise.app.exceptions;

public class LoginFailedException extends Exception {

	private final String message;
	private static final long serialVersionUID = 1L;
	
	public LoginFailedException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
