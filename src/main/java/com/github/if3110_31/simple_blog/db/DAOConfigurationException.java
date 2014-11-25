package com.github.if3110_31.simple_blog.db;

public class DAOConfigurationException extends RuntimeException {

	/**
	 * serialVersionUID of the class 
	 */
	private static final long serialVersionUID = 1L;
	
	public DAOConfigurationException(String message) {
		super(message);
	}
	
	public DAOConfigurationException(Throwable cause) {
		super(cause);
	}
	
	public DAOConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
