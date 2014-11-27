package com.github.if3110_31.simple_blog.model;

public class RoleException extends RuntimeException {
	public RoleException(String message) {
		super(message);
	}
	
	public RoleException(Throwable e) {
		super(e);
	}
	
	public RoleException(String message, Throwable e) {
		super(message, e);
	}
}
