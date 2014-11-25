package com.github.if3110_31.simple_blog.db;


public class DAOException extends RuntimeException {

	/**
	 * serialVersionUID of the class 
	 */
	private static final long serialVersionUID = 1L;
	
	public DAOException(String message) {
		super(message);
	}
	
	public DAOException(Throwable cause) {
		super(cause);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
