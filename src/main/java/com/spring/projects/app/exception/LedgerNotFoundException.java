package com.spring.projects.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LedgerNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public LedgerNotFoundException(String message) {
		super(message);
	}
	
	

}
