package com.blogit.exceptions;

public class DatabaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -722107190241092528L;

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
