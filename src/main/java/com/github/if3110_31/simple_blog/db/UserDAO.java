package com.github.if3110_31.simple_blog.db;

import java.util.Collection;
import javax.sql.RowSet;

import com.github.if3110_31.simple_blog.User;

/**
 * The interface for accessing user in the DB
 * @author Alvin Natawiguna
 *
 */
public interface UserDAO {
	/**
	 * Find user by id. Don't know what this is used for...
	 * @param id
	 * @return User object if exists, else null
	 */
	public User find(int id) throws DAOException;
	
	/**
	 * Find user by name. Used by the to-be search function
	 * @param name
	 * @return User object if exists, else null
	 */
	public User find(String name) throws DAOException;
	
	/**
	 * Find user by name and hash. Used by login
	 * @param name
	 * @param passwordHash
	 * @return User object if exists, else null
	 */
	public User find(String name, String passwordHash) throws DAOException;
	
	/**
	 * Creates a new user based on the properties of the given {@link User} object.
	 * user.id should be nullified
	 * 
	 * @param user the {@link User} object
	 * @return true if update succeeds, false if otherwise
	 * @throws DAOException
	 */
	public void create(User user) throws IllegalArgumentException, DAOException;
	
	/**
	 * Updates a user based on the properties of the given {@link User} object
	 * user.id should not be nullified
	 * 
	 * @param user the {@link User} object
	 * @return true if update succeeds, false if otherwise
	 * @throws DAOException
	 */
	public void update(User user) throws IllegalArgumentException, DAOException;
	
	/**
	 * Updates a user based on the properties of the given {@link User} object
	 * user.id should not be nullified
	 * 
	 * @param user the {@link User} object
	 * @param newPasswordHash new password hash
	 * @return true if update succeeds, false if otherwise
	 * @throws DAOException
	 */
	public void changePassword(User user, String newPasswordHash) throws IllegalArgumentException, DAOException;
	
	/**
	 * Deletes a user based on the properties of the given {@link User} object
	 * user.id should not be nullified
	 * 
	 * @param user the {@link User} object
	 * @return true if update succeeds, false if otherwise
	 * @throws DAOException
	 */
	public void delete(User user) throws IllegalArgumentException, DAOException;
	
	/**
	 * Todo
	 * 
	 * @return
	 * @throws DAOException
	 */
	public RowSet selectUserAsRowSet() throws DAOException;
	
	/**
	 * Todo
	 * 
	 * @return
	 * @throws DAOException
	 */
	public Collection<User> selectUserAsCollection() throws DAOException;
}
