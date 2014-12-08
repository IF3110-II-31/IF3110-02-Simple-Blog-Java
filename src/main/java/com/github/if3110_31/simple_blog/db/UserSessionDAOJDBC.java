package com.github.if3110_31.simple_blog.db;

import static com.github.if3110_31.simple_blog.db.DAOUtil.*;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.mysql.jdbc.StringUtils;

public class UserSessionDAOJDBC implements UserSessionDAO, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5508872127462869073L;

	private static final String SQL_TABLE = "user_session";
	
	private static final String SQL_GET_SESSIONID = "SELECT * FROM " + SQL_TABLE + " WHERE sessionIdHash=?"; 
	
	private static final String SQL_GET_USERID = "SELECT * FROM " + SQL_TABLE + " WHERE userId=?";
	
	private static final String SQL_CREATE_SESSION = "INSERT INTO " + SQL_TABLE + " (sessionIdHash, userId, expiryTime) VALUES (?, ?, ?)";
	
	private static final String SQL_DELETE_SESSION = "DELETE FROM " + SQL_TABLE + " WHERE userId=?";
	
	private static final String SQL_CLEAR_SESSION = "IF ((SELECT COUNT(*) FROM " + SQL_TABLE + ") > 0) THEN "
													+ "TRUNCATE TABLE " + SQL_TABLE;
	
	private DAOFactory daoFactory;
	
	public UserSessionDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String getSessionId(Long userId) throws IllegalArgumentException, DAOException {
		if(userId == null) {
			throw new IllegalArgumentException("userId cannot be empty/null.");
		}
		
		String ret = null;
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
        	connection = daoFactory.getConnection();
        	preparedStatement = connection.prepareStatement(SQL_GET_USERID);
        	preparedStatement.setLong(1, userId);
        	resultSet = preparedStatement.executeQuery();
        	
        	if(resultSet.next()) {
        		if(resultSet.getDate("expiryTime").getTime() < System.currentTimeMillis()) {
        			this.deleteUserSession(userId);
        		} else {
        			ret = resultSet.getString("sessionIdHash");
        		}
        	}
        } catch (SQLException e) {
        	throw new DAOException(e);
        } finally {
        	close(connection, preparedStatement, resultSet);
        }
		
		return ret;
	}

	@Override
	public void createUserSession(Long userId, String sessionId, int expiry)
			throws IllegalArgumentException, DAOException 
	{
		if(userId == null) {
			throw new IllegalArgumentException("userId cannot be empty/null.");
		}
		if(StringUtils.isEmptyOrWhitespaceOnly(sessionId)) {
			throw new IllegalArgumentException("sessionId cannot be empty/null.");
		}
		
		if(expiry > 0) {
			Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    
		    try {
		    	connection = daoFactory.getConnection();
		    	preparedStatement = connection.prepareStatement(SQL_CREATE_SESSION);
		    	
		    	preparedStatement.setString(1, sessionId);
		    	preparedStatement.setLong(2, userId);
		    	
		    	Timestamp timestamp = new Timestamp(System.currentTimeMillis() + (expiry * 1000));
		    	preparedStatement.setTimestamp(3, timestamp);
		    	
		    	int affectedRows = preparedStatement.executeUpdate();
		    	if(affectedRows == 0) {
		    		throw new DAOException("User session record failed to be created. No affected rows.");
		    	}
		    } catch (SQLException e) {
		    	throw new DAOException(e);
		    } finally {
		    	close(connection, preparedStatement);
		    }
		}
	}

	@Override
	public void deleteUserSession(Long userId) throws IllegalArgumentException, DAOException {
		if(userId == null) {
			throw new IllegalArgumentException("userId cannot be empty/null.");
		}
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
        	connection = daoFactory.getConnection();
        	preparedStatement = connection.prepareStatement(SQL_DELETE_SESSION);
        	
        	preparedStatement.setLong(1, userId);
        	
        	int affectedRows = preparedStatement.executeUpdate();
        	if(affectedRows == 0) {
        		throw new DAOException("User session record failed to be deleted. No affected rows.");
        	}
        } catch (SQLException e) {
        	throw new DAOException(e);
        } finally {
        	close(connection, preparedStatement);
        }
	}

	@Override
	public Long getUserId(String sessionId) throws IllegalArgumentException, DAOException {
		if(StringUtils.isEmptyOrWhitespaceOnly(sessionId)) {
			throw new IllegalArgumentException("sessionId cannot be empty/null.");
		}
		
		Long ret = null;
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
        	connection = daoFactory.getConnection();
        	preparedStatement = connection.prepareStatement(SQL_GET_SESSIONID);
        	preparedStatement.setString(1, sessionId);
        	resultSet = preparedStatement.executeQuery();
        	
        	if(resultSet.next()) {
        		// check whether session has expired or not
        		if(resultSet.getTimestamp("expiryTime").getTime() < System.currentTimeMillis()) {
        			deleteUserSession(resultSet.getLong("userId"));
        		} else {
        			ret = resultSet.getLong("userId");
        		}
        	}
        } catch (SQLException e) {
        	throw new DAOException(e);
        } finally {
        	close(connection, preparedStatement, resultSet);
        }
		
		return ret;
	}

	@Override
	public void clearUserSession() throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = daoFactory.getConnection();
			preparedStatement = connection.prepareStatement(SQL_CLEAR_SESSION);
			
			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows == 0) {
				throw new DAOException("Clear failed. No affected rows.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			close(connection, preparedStatement);
		}
	}

}
