package com.github.if3110_31.simple_blog.bean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class BeanUtil {
	public static HttpSession getSession() {
		return (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}
}
