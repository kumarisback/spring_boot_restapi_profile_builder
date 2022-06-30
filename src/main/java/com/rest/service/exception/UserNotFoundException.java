package com.rest.service.exception;

public class UserNotFoundException extends RuntimeException {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 6514967588717164538L;

	public UserNotFoundException(Long id) {
		 super("Could not find employee " + id);
	}
	public UserNotFoundException(String username) {
		 super("Could not find employee " + username);
	}
}
