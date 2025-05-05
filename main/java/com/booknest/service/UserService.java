package com.booknest.service; // Correct package

import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

public interface UserService {

	/**
	 * Retrieves the profile image URL for a given username. Returns a default image
	 * path if no specific image is set, not found, or an error occurs.
	 *
	 * @param userName The username of the user.
	 * @return The relative path to the user's profile image or a default path.
	 */
	String getProfileImageUrl(String userName);

	/**
	 * Updates the profile image URL for a given username in the database.
	 *
	 * @param userName The username of the user to update.
	 * @param imageUrl The new relative path to the profile image.
	 * @return true if the update was successful, false otherwise.
	 * @throws SQLException If a database access error occurs during the update.
	 */
	boolean updateProfileImageUrl(String userName, String imageUrl) throws SQLException;

	/**
	 * Validates the uploaded image file based on size.
	 *
	 * @param filePart The uploaded file part.
	 * @throws IllegalArgumentException if validation fails (e.g., size exceeded).
	 */
	void validateProfileImage(Part filePart) throws IllegalArgumentException;

	/**
	 * Saves the uploaded profile image to the designated location determined by
	 * imageUtil.
	 *
	 * @param filePart The uploaded file part.
	 * @return The relative path where the image was saved (e.g.,
	 *         "resources/images/system/profile/imagename.jpg").
	 * @throws IOException              If an error occurs during file saving.
	 * @throws IllegalArgumentException If the file part is invalid or cannot be
	 *                                  processed.
	 */
	String saveProfileImage(Part filePart) throws IOException, IllegalArgumentException;

}