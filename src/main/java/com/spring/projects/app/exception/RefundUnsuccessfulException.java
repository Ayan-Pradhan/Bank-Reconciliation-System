package com.spring.projects.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RefundUnsuccessfulException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public RefundUnsuccessfulException(String message) {
		super(message);
	}

}
