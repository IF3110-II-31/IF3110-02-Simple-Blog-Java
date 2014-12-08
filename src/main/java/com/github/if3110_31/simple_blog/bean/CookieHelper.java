package com.github.if3110_31.simple_blog.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class CookieHelper {
	/**
	 * Hidden constructor
	 */
	private CookieHelper() {
		
	}
	
	public static void setCookie(String key, String value, Integer expiry) {
		HttpServletRequest request = BeanUtil.getHttpRequest();
		Map<String, Object> cookies = BeanUtil.getExternalContext().getRequestCookieMap();
		
		Cookie cookie = (Cookie) cookies.get(key);
		if(cookie != null) {
			cookie.setValue(value);
			
			if(expiry != null) {
				// check whether we want to delete the cookie
				cookie.setMaxAge(expiry);
			}

			HttpServletResponse response = BeanUtil.getHttpResponse();
			
			response.addCookie(cookie);
		} else {
			Map<String, Object> cookieProperties = new HashMap<>();
			
			cookieProperties.put("maxAge", expiry);
			cookieProperties.put("path", request.getContextPath());
			
			BeanUtil.getExternalContext().addResponseCookie(key, value, cookieProperties);
		}
	}
	
	public static Cookie getCookie(String key) {
		return (Cookie) BeanUtil.getExternalContext().getRequestCookieMap().get(key);
	}
}
