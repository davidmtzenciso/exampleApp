package com.example.app.exception;

public class TransactionsNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TransactionsNotFoundException(String msg) {
		super(msg);
	}
}
