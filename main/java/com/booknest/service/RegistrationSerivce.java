package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.booknest.config.DbConfiguration;
import com.booknest.model.UserModel;
import com.booknest.util.PasswordUtil;

public class RegistrationSerivce {
	private boolean isConnectionError = false;
	private Connection dbConn;
	private static PasswordUtil passwordUtil = new PasswordUtil();

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public RegistrationSerivce() {
		try {

			dbConn = DbConfiguration.getDbConnection();

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}

	}

	/**
	 * Registers a new student in the database.
	 *
	 * @param studentModel the student details to be registered
	 * @return Boolean indicating the success of the operation
	 * @throws Exception
	 */
	public Boolean addUser(UserModel userModel) throws Exception {
		if (isConnectionError) {
			throw new Exception("Database connection error.");

		}
		// Check for existing user
		if (isUserExists(userModel.getUser_name(), userModel.getEmail())) {
			throw new Exception("A user already exists with this username or email.");
		}

		String insertQuery = "INSERT INTO user (first_name, last_name, user_name, password, email,phone_number, address) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
			// Insert student details
			String hashedPassword = passwordUtil.hashPassword(userModel.getPassword());
			if (hashedPassword == null) {
				throw new Exception("Password hashing failed.");

			}

			insertStmt.setString(1, userModel.getFirst_name());
			insertStmt.setString(2, userModel.getLast_name());
			insertStmt.setString(3, userModel.getUser_name());
			insertStmt.setString(4, hashedPassword);
			insertStmt.setString(5, userModel.getEmail());
			insertStmt.setString(6, userModel.getPhone_number());
			insertStmt.setString(7, userModel.getAddress());
			return insertStmt.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public boolean isUserExists(String userName, String email) {
		String query = "SELECT COUNT(*) FROM user WHERE user_name = ? OR email = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			// Bind parameters to prevent SQL injection
			stmt.setString(1, userName);
			stmt.setString(2, email);

			// Execute the query
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // Check if the count is greater than 0
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // Default to no user found if an exception occurs
	}

}
