package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.booknest.config.DbConfiguration;
import com.booknest.util.imageUtil;
import jakarta.servlet.http.Part;
import java.io.IOException;

/**
 * Service for handling user account profile image operations. This service
 * provides methods to retrieve, update, and manage user profile images.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class MyAccountService {
	private final imageUtil imageUtil;
	private static final int MAX_FILE_SIZE = 3 * 1024 * 1024; // 3 MB

	/**
	 * Default constructor that initializes the image utility.
	 */
	public MyAccountService() {
		this.imageUtil = new imageUtil();
	}

	/**
	 * Retrieves the profile image URL for a user from the database.
	 * 
	 * @param userName The username of the user
	 * @return The profile image URL or null if not found
	 */
	public String getProfileImageUrl(String userName) {
		Connection conn = null;
		PreparedStatement ps = null;
		String profileImageUrl = null;

		try {
			conn = DbConfiguration.getDbConnection();
			String sql = "SELECT user_img_url FROM user WHERE user_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				profileImageUrl = rs.getString("user_img_url");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources(ps, conn);
		}

		return profileImageUrl;
	}

	/**
	 * Updates the user's profile image URL in the database.
	 * 
	 * @param userName  The username of the user
	 * @param imagePath The relative path of the image
	 * @return true if update was successful, false otherwise
	 */
	public boolean updateProfileImageUrl(String userName, String imagePath) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean success = false;

		try {
			conn = DbConfiguration.getDbConnection();
			String sql = "UPDATE user SET user_img_url = ? WHERE user_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, imagePath);
			ps.setString(2, userName);
			int rowsAffected = ps.executeUpdate();
			success = (rowsAffected > 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources(ps, conn);
		}

		return success;
	}

	/**
	 * Checks if the file size is within the allowed limit.
	 * 
	 * @param filePart The uploaded file part
	 * @return true if file size is valid, false otherwise
	 */
	public boolean isValidFileSize(Part filePart) {
		return filePart.getSize() <= MAX_FILE_SIZE;
	}

	/**
	 * Uploads a profile image to the server.
	 * 
	 * @param filePart The uploaded file part
	 * @return The relative path of the uploaded image or null if upload failed
	 * @throws IOException if an I/O error occurs during file processing
	 */
	public String uploadProfileImage(Part filePart) throws IOException {
		String saveFolder = "/profile"; // Based on your actual file structure
		boolean uploaded = imageUtil.uploadImage(filePart, "", saveFolder);

		if (uploaded) {
			String imageName = imageUtil.getImageNameFromPart(filePart);
			return "resources/images/system/profile/" + imageName; // Updated path to match your structure
		}

		return null;
	}

	/**
	 * Gets the default profile image path for users who haven't uploaded a profile
	 * image.
	 * 
	 * @return The path to the default profile image
	 */
	public String getDefaultProfileImagePath() {
		return "resources/images/system/default.png";
	}

	/**
	 * Helper method to close database resources safely.
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
			// Ignore closing errors
		}
	}
}