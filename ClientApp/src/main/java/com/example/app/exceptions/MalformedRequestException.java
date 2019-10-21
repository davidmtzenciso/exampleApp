package com.example.app.exceptions;

public class MalformedRequestException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MalformedRequestException(String msg) {
		super(msg);
	}

}
