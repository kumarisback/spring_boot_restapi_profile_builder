package com.rest.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String resourceNotFoundException(Exception ex, WebRequest request) {

		return "Something went wrong :" + ex.toString();
	}
	
	
	
	@ExceptionHandler(value = { InvalidBearerTokenException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String resourceNotFoundException1(Exception ex, WebRequest request) {

		return "Something went wrong :" + ex.toString();
	}

}