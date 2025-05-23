package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.util.imageUtil;

import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat; // For formatting file size message

/**
 * Implementation of UserService interface that handles user profile operations.
 * Provides functionality for managing user profile images including retrieval,
 * update, validation and saving operations.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class UserServiceImpl implements UserService {

	// --- Configuration Constants ---
	private static final String DEFAULT_PROFILE_IMAGE = "resources/images/system/person.png";
	private static final long MAX_FILE_SIZE_BYTES = 3 * 1024 * 1024;
	private static final String PROFILE_IMAGE_SAVE_SUBFOLDER = "/profile";
	private static final String PROFILE_IMAGE_RELATIVE_BASE_PATH = "resources/images/UploadedProfilePicture";

	private final imageUtil imgUtil;
	private static final DecimalFormat df = new DecimalFormat("#.##");

	/**
	 * Default constructor that initializes image utility.
	 */
	public UserServiceImpl() {
		this.imgUtil = new imageUtil();
	}

	/**
	 * Retrieves the profile image URL for a specific user. If no image is found or
	 * an error occurs, returns the default profile image URL.
	 * 
	 * @param userName The username of the user whose profile image URL to retrieve
	 * @return The URL of the user's profile image or default image if not found
	 */
	@Override
	public String getProfileImageUrl(String userName) {
		String profileImageUrl = DEFAULT_PROFILE_IMAGE;

		String sql = "SELECT user_img_url FROM user WHERE user_name = ?";

		if (userName == null || userName.trim().isEmpty()) {
			System.err.println("Attempted to get profile image URL for null or empty username.");
			return DEFAULT_PROFILE_IMAGE;
		}

		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, userName);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String dbImageUrl = rs.getString("user_img_url");
					if (dbImageUrl != null && !dbImageUrl.trim().isEmpty()) {
						profileImageUrl = dbImageUrl.trim().replace("\\", "/");
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(
					"Database error fetching profile image URL for user: " + userName + ". Returning default.");
			e.printStackTrace();
		} catch (Exception e) {
			System.err
					.println("Non-SQL error during profile image fetch for user: " + userName + ". Returning default.");
			e.printStackTrace();
		}
		return profileImageUrl;
	}

	/**
	 * Updates a user's profile image URL in the database. Ensures path separators
	 * are standardized to forward slashes.
	 * 
	 * @param userName The username of the user whose profile image to update
	 * @param imageUrl The new profile image URL to set
	 * @return true if update was successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	@Override
	public boolean updateProfileImageUrl(String userName, String imageUrl) throws SQLException {
		String sql = "UPDATE user SET user_img_url = ? WHERE user_name = ?";
		int updatedRows = 0;

		if (userName == null || userName.trim().isEmpty() || imageUrl == null) {
			System.err.println("Invalid input for updating profile image URL.");
			return false;
		}
		String consistentImageUrl = imageUrl.trim().replace("\\", "/");

		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, consistentImageUrl);
			ps.setString(2, userName);
			updatedRows = ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Database error updating profile image URL for user: " + userName);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("Non-SQL error during profile image update for user: " + userName);
			e.printStackTrace();
			throw new SQLException("Failed to update profile image due to underlying error.", e);
		}
		return updatedRows > 0;
	}

	// --- Image Handling Methods ---

	/**
	 * Validates a profile image file part. Checks if the file exists and doesn't
	 * exceed maximum allowed file size.
	 * 
	 * @param filePart The uploaded file part to validate
	 * @throws IllegalArgumentException If the file is empty or exceeds size limit
	 */
	@Override
	public void validateProfileImage(Part filePart) throws IllegalArgumentException {
		if (filePart == null || filePart.getSize() == 0) {
			throw new IllegalArgumentException("No file uploaded or file is empty.");
		}

		// *** RE-ADD SIZE CHECK ***
		if (filePart.getSize() > MAX_FILE_SIZE_BYTES) {
			// Create a user-friendly message
			double maxSizeMB = (double) MAX_FILE_SIZE_BYTES / (1024 * 1024);
			throw new IllegalArgumentException(
					"File size exceeds the maximum allowed size of " + df.format(maxSizeMB) + " MB.");
		}
	}

	/**
	 * Saves a profile image to the server. Uses imageUtil to handle the actual file
	 * saving operation.
	 * 
	 * @param filePart The uploaded file part containing the image
	 * @return The relative path to the saved image
	 * @throws IOException              If an error occurs during file saving
	 * @throws IllegalArgumentException If the file part is invalid
	 */
	@Override
	public String saveProfileImage(Part filePart) throws IOException, IllegalArgumentException {
		if (filePart == null || filePart.getSize() == 0) {
			// This case is already handled by validateProfileImage, but good for robustness
			throw new IllegalArgumentException("Cannot save an empty file part.");
		}

		boolean uploaded = imgUtil.uploadImage(filePart, "", PROFILE_IMAGE_SAVE_SUBFOLDER);

		if (!uploaded) {
			throw new IOException("Image upload failed using imageUtil.");
		}

		String imageName = imgUtil.getImageNameFromPart(filePart);
		if (imageName == null || imageName.trim().isEmpty()) {
			throw new IllegalArgumentException("Could not determine the name of the saved image from imageUtil.");
		}

		String relativePath = (PROFILE_IMAGE_RELATIVE_BASE_PATH + PROFILE_IMAGE_SAVE_SUBFOLDER + "/" + imageName.trim())
				.replace("\\", "/");
		return relativePath;
	}
}