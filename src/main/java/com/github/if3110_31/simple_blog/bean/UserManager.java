package com.github.if3110_31.simple_blog.bean;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.github.if3110_31.simple_blog.SHA512;
import com.github.if3110_31.simple_blog.db.*;
import com.github.if3110_31.simple_blog.model.User;
import com.mysql.jdbc.StringUtils;

@ManagedBean(name = "userManager")
@SessionScoped
public class UserManager implements Serializable {
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1998717464534180478L;
	
	/**
	 * Identifier of the sessionId at the cookie
	 */
	public static final String sessionCookieID = "blog-sessionHash";
	
	private User user;
	
	private UserDAO userDao;
	private UserSessionDAO userSessionDao;

	/**
	 * Initialization: checks whether the user has the cookie
	 */
	@PostConstruct
	private void init() throws DAOException {
		DAOFactory daoFactory = DAOFactory.getInstance("simple-blog");
		
		userDao = daoFactory.getUserDAO();
		userSessionDao = daoFactory.getUserSessionDAO();
		
		Cookie sessionId = CookieHelper.getCookie(sessionCookieID);
		
		String sessionIdHash;
		if(sessionId != null) {
			sessionIdHash = sessionId.getValue();
		} else {
			sessionIdHash = null;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sessionIdHash)) {
			Long userId = userSessionDao.getUserId(sessionIdHash);
			
			if(userId != null) {
				user = userDao.find(userId);
				
				if(user != null) {
					createSession(user, false);
					clear();
				} else {
					throw new DAOException("No user with userID " + userId + "found");
				}
			} else {
				// assume expired session
				FacesMessage message = new FacesMessage("Session expired");
				FacesContext.getCurrentInstance()
					.addMessage(null, message);
				sessionId.setMaxAge(0);
			}
		}
	}
	
	/**
	 * the username
	 */
	private String username;
	
	/**
	 * password in cleartext, not hashed
	 */
	private String password;
	
	/**
	 * "Remember Me" option
	 */
	private boolean remember;
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean getRemember() {
		return remember;
	}
	
	public void setRemember(boolean remember) {
		this.remember = remember;
	}
	
	public boolean isLoggedIn() {
		boolean ret;
		ret = (BeanUtil.getSession(false) != null);
		
		if(ret) {
			ret = user != null;
		}
		
		return ret;
	}
	
	/**
	 * Do a login
	 * @return index if successful, return to login if failed
	 */
	public String login(boolean remember) {
		String retval;
		
		if(!isLoggedIn()) {
			user = userDao.find(username, SHA512.hashText(password));
			if(user != null) {
				createSession(user, true);
				clear();
				
				// redirect
				retval = "index";
			} else {
				FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage("Your username or password isn't valid"));
				retval = "login";
			}
		} else {
			retval = "index";
		}
		
		return retval;
	}
	
	/**
	 * Creates a new user session
	 * @param user the {@link User} object, the user data storage
	 * @param newLogin if true, stores the session in the database. Used for recently logged in users.
	 */
	private void createSession(User user, boolean newLogin) {
		HttpSession session = BeanUtil.getSession(true);
		
		session.setAttribute("userId", user.getId());
		session.setAttribute("userName", user.getName());
		session.setAttribute("userEmail", user.getEmail());
		session.setAttribute("userRole", user.getRoleId());
		
		// store the session & create the cookie
		int expiry = remember ? 3600 * 24 * 7 : 3600;
		
		String sessionHash = SHA512.hashText(session.getId());
		
		if(newLogin) {
			userSessionDao.createUserSession(user.getId(), sessionHash, expiry);
		}
		
		CookieHelper.setCookie(sessionCookieID, sessionHash, expiry);
		Cookie sessionCookie = CookieHelper.getCookie(sessionCookieID);
		
		if(sessionCookie != null)
			sessionCookie.setHttpOnly(true);
	}
	
	/**
	 * Clears all temporary data from this {@link UserManager} bean
	 * TODO: delete this function; just grab the data directly from the login form
	 */
	private void clear() {
		username = null;
		password = null;
		remember = false;
	}
	
	/**
	 * Do a logout
	 * @return destination
	 */
	public String logout() {
		deleteSession();
		
		return "index";
	}
	
	/**
	 * Deletes the logged in user session and invalidates its cookie
	 */
	private void deleteSession() {
		userSessionDao.deleteUserSession((Long)BeanUtil.getSession(false).getAttribute("userId"));
		
		ExternalContext ec = BeanUtil.getExternalContext();
		ec.invalidateSession();
		
		Cookie sessionCookie = CookieHelper.getCookie(sessionCookieID);
		assert(sessionCookie != null);
		
		sessionCookie.setMaxAge(0);
	}
}
