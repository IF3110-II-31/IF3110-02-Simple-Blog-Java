package com.github.if3110_31.simple_blog.bean;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BeanUtil {
	public static HttpSession getSession(boolean createNew) {
		return (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(createNew);
	}
	
	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	public static HttpServletRequest getHttpRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public static HttpServletResponse getHttpResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
}
