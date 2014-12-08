package com.github.if3110_31.simple_blog.db;

import java.util.Date;

/**
 * Interface for interacting with the User Session
 * @author Alvin Natawiguna
 *
 */
public interface UserSessionDAO {
	/**
	 * Get the sessionId of the user
	 * @param userId the defined userId
	 * @return the sessionId of the current user
	 * @throws DAOException if something happens in the DB
	 */
	public String getSessionId(Long userId) throws IllegalArgumentException, DAOException;
	
	/**
	 * Get the userId that has the matching sessionId
	 * @param sessionId
	 * @return the userId with the matching sessionId, or null if does not exists
	 * @throws DAOException if something happens in the DB
	 */
	public Long getUserId(String sessionId) throws IllegalArgumentException, DAOException;
	
	/**
	 * Stores a record of userSession with the matching userId
	 * @param userId the userId
	 * @param sessionId the sessionId of the user
	 * @param expiry the value (in seconds) of how long the session will last.
	 * @throws DAOException if something happens in the DB
	 */
	public void createUserSession(Long userId, String sessionId, int expiry) 
			throws IllegalArgumentException, DAOException;
	
	/**
	 * Deletes the user's session record from the DB
	 * @param userId
	 * @throws DAOException
	 */
	public void deleteUserSession(Long userId) throws IllegalArgumentException, DAOException;
	
	/**
	 * Clears the user session table
	 * @throws DAOException
	 */
	public void clearUserSession() throws DAOException;
}
