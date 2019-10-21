package com.example.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class FailedEntityValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FailedEntityValidationException(String msg) {
		super(msg); 
	}

}
