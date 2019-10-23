package com.example.app.exceptions;

public class AuthenticationFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthenticationFailedException(String msg) {
		super(msg);
	}
}
