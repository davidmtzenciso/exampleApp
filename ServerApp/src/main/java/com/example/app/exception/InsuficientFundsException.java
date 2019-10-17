package com.example.app.exception;

public class InsuficientFundsException extends Exception {

	private static final long serialVersionUID = 1L;

	public InsuficientFundsException(String msg) {
		super(msg);
	}
}
