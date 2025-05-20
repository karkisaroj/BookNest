package com.booknest.service;

import com.booknest.config.DbConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Service for managing user account operations related to profile management.
 * This class provides methods for retrieving and updating user profile images.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class UserAccountService {

	/**
	 * Gets the profile image URL for a user from the database. If the user has no
	 * profile image or is not found, returns the default image path.
	 * 
	 * @param userName the user name of the user to retrieve profile image for
	 * @return the profile image URL or default image URL if not found
	 * @throws ClassNotFoundException if database driver class is not found
	 */
	public String getUserProfileImageUrl(String userName) throws ClassNotFoundException {
		String profileImageUrl = "resources/images/system/default.png"; // Default image

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbConfiguration.getDbConnection();
			String sql = "SELECT user_img_url FROM user WHERE user_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			var rs = ps.executeQuery();

			if (rs.next()) {
				String dbImageUrl = rs.getString("user_img_url");
				if (dbImageUrl != null && !dbImageUrl.trim().isEmpty()) {
					profileImageUrl = dbImageUrl;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(ps, conn);
		}

		return profileImageUrl;
	}

	/**
	 * Updates the user's profile image URL in the database. This method sets a new
	 * image URL for the specified user.
	 * 
	 * @param userName the user name of the user to update
	 * @param imageUrl the new profile image URL to set
	 * @return true if the update was successful, false otherwise
	 * @throws ClassNotFoundException if database driver class is not found
	 */
	public boolean updateUserProfileImage(String userName, String imageUrl) throws ClassNotFoundException {
		boolean success = false;
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbConfiguration.getDbConnection();
			String sql = "UPDATE user SET user_img_url = ? WHERE user_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, imageUrl);
			ps.setString(2, userName);
			int rowsAffected = ps.executeUpdate();

			success = (rowsAffected > 0);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(ps, conn);
		}

		return success;
	}

	/**
	 * Helper method to safely close database resources. Ensures that
	 * PreparedStatement and Connection objects are properly closed.
	 * 
	 * @param ps   The PreparedStatement to close
	 * @param conn The Connection to close
	 */
	private void closeResources(PreparedStatement ps, Connection conn) {
		try {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}