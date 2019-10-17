package com.example.app.exception;

public class OverdrawnAccountException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public OverdrawnAccountException(String msg) {
		super(msg);
	}

}
