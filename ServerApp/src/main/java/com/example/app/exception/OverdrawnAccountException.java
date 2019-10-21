package com.example.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class OverdrawnAccountException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public OverdrawnAccountException(String msg) {
		super(msg);
	}

}
