package com.example.app.exceptions;

public class AttemptsExceededException extends Exception {


	private static final long serialVersionUID = 1L;
	
	public AttemptsExceededException(String message) {
		super(message);
	}

}
