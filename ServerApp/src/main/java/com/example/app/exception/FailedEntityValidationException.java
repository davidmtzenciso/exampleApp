package com.example.app.exception;

public class FailedEntityValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FailedEntityValidationException(String msg) {
		super(msg); 
	}

}
