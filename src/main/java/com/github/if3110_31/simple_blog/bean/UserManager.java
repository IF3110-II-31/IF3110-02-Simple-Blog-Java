package com.github.if3110_31.simple_blog.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.github.if3110_31.simple_blog.SHA512;
import com.github.if3110_31.simple_blog.db.*;
import com.github.if3110_31.simple_blog.model.User;

@ManagedBean(name = "userManager")
@SessionScoped
public class UserManager {
	private User user;
	private UserDAO userDao = DAOFactory.getInstance("simple-blog").getUserDAO();

	/**
	 * the username
	 */
	private String username;
	/**
	 * password in cleartext, not hashed
	 */
	private String password;
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean isLoggedIn() {
		return user != null;
	}
	
	/**
	 * Do a login
	 * @return index if successful, return to login if failed
	 */
	public String login() {
		String retval;
		
		System.err.println(SHA512.hashText(password));
		
		user = userDao.find(username, SHA512.hashText(password));
		if(user != null) {
			// set session, redirect to home, clear password & username?
			HttpSession session = BeanUtil.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userName", user.getUsername());
			session.setAttribute("userRole", user.getRoleId());
			
			retval = "index";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your username or password isn't valid"));
			retval = "login";
		}
		
		return retval;
	}
	
	/**
	 * Do a logout
	 * @return destination
	 */
	public String logout() {
		HttpSession session = BeanUtil.getSession();
		session.invalidate();
		
		// redirect to home
		return "index";
	}
}
