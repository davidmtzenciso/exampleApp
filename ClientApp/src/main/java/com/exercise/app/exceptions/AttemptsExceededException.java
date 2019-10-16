package com.exercise.app.exceptions;

public class AttemptsExceededException extends Exception {


	private static final long serialVersionUID = 1L;
	private final String message;
	
	public AttemptsExceededException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
