package com.github.if3110_31.simple_blog.db;

import java.util.Collection;

import javax.sql.RowSet;

import java.sql.*;

import static com.github.if3110_31.simple_blog.db.DAOUtil.*;

import com.github.if3110_31.simple_blog.model.User;

/**
 * Implementation of the UserDAO interface
 * 
 * @link http://balusc.blogspot.com/2008/07/dao-tutorial-data-layer.html
 * @author Alvin Natawiguna
 *
 */
public class UserDAOJDBC implements UserDAO {
	private DAOFactory daoFactory;
	
	UserDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public User find(int id) throws DAOException {
		String sql = "SELECT * FROM user WHERE id=?";
		return find(sql, id);
	}

	@Override
	public User find(String name) throws DAOException {
		String sql = "SELECT * FROM user WHERE name=?";
		return find(sql, name);
	}

	@Override
	public User find(String name, String passwordHash) throws DAOException {
		String sql = "SELECT * FROM user WHERE name=? AND password=?";
		return find(sql, name, passwordHash);
	}
	
	/**
	 * 
	 * @param sql
	 * @param values
	 * @return User object if user exists, otherwise null
	 */
	private User find(String sql, Object...values) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, sql, false, values);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return user;
	}
	
	private static User map(ResultSet rs) throws SQLException {
		User user = new User();
		
		user.setId(rs.getLong("id"));
		user.setUsername(rs.getString("name"));
		user.setRole(User.Role.getRole(rs.getInt("roleId")));
		user.setPassword(rs.getString("password"));
		
		return user;
	}

	@Override
	public void create(User user) throws IllegalArgumentException, DAOException {
		if(user.getId() != null) {
			throw new IllegalArgumentException("User is already created: the User ID is not null.");
		}
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        String sql = "INSERT INTO user (name, password, roleId) VALUES (?, ?, ?)";
        
        Object values[] = {
        	user.getUsername(),
        	user.getPassword(),
        	user.getRoleId()
        };
        
        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, sql, true, values);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 0) {
            	throw new DAOException("User creation failed. No rows affected");
            }
            
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
            	user.setId(generatedKeys.getLong("id"));
            } else {
            	throw new DAOException("User creation failed. No keys generated");
            }
            
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement);
        }
	}

	@Override
	public void update(User user) throws IllegalArgumentException, DAOException {
		if(user.getId() == null) {
			throw new IllegalArgumentException("User not defined: the ID is null");
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String sql = "UPDATE user SET role=? WHERE id=?";
		
		Object[] values = {
			user.getRoleId(),
			user.getId()
		};
		
		try {
			connection = daoFactory.getConnection();
			preparedStatement = prepareStatement(connection, sql, false, values);
			
			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows == 0) {
				throw new DAOException("User update failed. No rows affected.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			close(connection, preparedStatement);
		}
	}

	@Override
	public void delete(User user) throws IllegalArgumentException, DAOException {
		if(user.getId() == null) {
			throw new IllegalArgumentException("User not defined: the ID is null");
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String sql = "DELETE FROM user WHERE id=?";
		
		Object[] values = {
			user.getId()
		};
		
		try {
			connection = daoFactory.getConnection();
			preparedStatement = prepareStatement(connection, sql, false, values);
			
			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows == 0) {
				throw new DAOException("User deletion failed. No rows affected.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			close(connection, preparedStatement);
		}
	}

	@Override
	public RowSet selectUserAsRowSet() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<User> selectUserAsCollection() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePassword(User user, String newPasswordHash) throws IllegalArgumentException, DAOException {
		if(user.getId() == null) {
			throw new IllegalArgumentException("User not defined: the ID is null");
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String sql = "SELECT * FROM user WHERE id=? AND password=?";
		
		Object[] values = {
			user.getId(),
			user.getPassword()
		};
		
		try {
			connection = daoFactory.getConnection();
			preparedStatement = prepareStatement(connection, sql, false, values);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) { // password is correct
				close(resultSet);
				close(preparedStatement);
				
				sql = "UPDATE user SET password=? WHERE id=?";
				Object[] newValues = {
					newPasswordHash,
					user.getId()
				};
				
				preparedStatement = prepareStatement(connection, sql, false, newValues);
				int affectedRows = preparedStatement.executeUpdate();
				if(affectedRows == 0) {
					throw new DAOException("Changing password failed. Unknown error");
				} else {
					user.setPassword(newPasswordHash);
				}
			} else {
				throw new DAOException("Changing password failed. Old password does not match.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			close(connection, preparedStatement);
		}
	}
}
