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

	// File size constants
	private static final int MAX_FILE_SIZE = 3 * 1024 * 1024; // 3 MB

	// SQL query constants
	private static final String SQL_GET_PROFILE_IMAGE = "SELECT user_img_url FROM user WHERE user_name = ?";
	private static final String SQL_UPDATE_PROFILE_IMAGE = "UPDATE user SET user_img_url = ? WHERE user_name = ?";

	// File path constants
	private static final String PROFILE_SAVE_FOLDER = "/profile";
	private static final String PROFILE_IMAGE_PATH_PREFIX = "resources/images/system/profile/";
	private static final String DEFAULT_PROFILE_IMAGE_PATH = "resources/images/system/default.png";
	private static final String EMPTY_SUBFOLDER = "";

	// Database column constants
	private static final String COLUMN_USER_IMG_URL = "user_img_url";

	// Error message constants
	private static final String ERROR_GET_PROFILE_IMAGE = "Error retrieving profile image for user: ";
	private static final String ERROR_UPDATE_PROFILE_IMAGE = "Error updating profile image for user: ";
	

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
			ps = conn.prepareStatement(SQL_GET_PROFILE_IMAGE);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				profileImageUrl = rs.getString(COLUMN_USER_IMG_URL);
			}
		} catch (Exception e) {
			System.err.println(ERROR_GET_PROFILE_IMAGE + userName);
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
			ps = conn.prepareStatement(SQL_UPDATE_PROFILE_IMAGE);
			ps.setString(1, imagePath);
			ps.setString(2, userName);
			int rowsAffected = ps.executeUpdate();
			success = (rowsAffected > 0);
		} catch (Exception e) {
			System.err.println(ERROR_UPDATE_PROFILE_IMAGE + userName);
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
		boolean uploaded = imageUtil.uploadImage(filePart, EMPTY_SUBFOLDER, PROFILE_SAVE_FOLDER);

		if (uploaded) {
			String imageName = imageUtil.getImageNameFromPart(filePart);
			return PROFILE_IMAGE_PATH_PREFIX + imageName;
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
		return DEFAULT_PROFILE_IMAGE_PATH;
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