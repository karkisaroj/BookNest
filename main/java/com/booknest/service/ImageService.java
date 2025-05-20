package com.booknest.service;

import com.booknest.util.imageUtil;
import jakarta.servlet.http.Part;
import java.io.IOException;

/**
 * Service for handling image validation, uploads, and related operations. This
 * service provides methods for working with profile images and other image
 * uploads.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class ImageService {

	private static final long MAX_FILE_SIZE = 3 * 1024 * 1024; // 3MB
	private final imageUtil util;

	/**
	 * Default constructor that initializes the image utility.
	 */
	public ImageService() {
		this.util = new imageUtil();
	}

	/**
	 * Validates if an uploaded file is a valid image within size constraints.
	 * Checks for null, empty files, size limits, and ensures content type is an
	 * image.
	 * 
	 * @param filePart The uploaded file part to validate
	 * @return True if the image is valid for upload, false otherwise
	 */
	public boolean validateImage(Part filePart) {
		if (filePart == null || filePart.getSize() <= 0) {
			return false;
		}

		if (filePart.getSize() > MAX_FILE_SIZE) {
			return false;
		}

		String contentType = filePart.getContentType();
		return contentType != null && contentType.startsWith("image/");
	}

	/**
	 * Uploads a profile image to the server after validation.
	 * 
	 * @param filePart The uploaded file part containing the image
	 * @return The relative path to the saved image if successful, null if failed
	 * @throws IOException If an I/O error occurs during file processing
	 */
	public String uploadProfileImage(Part filePart) throws IOException {
		if (!validateImage(filePart)) {
			return null;
		}

		String saveFolder = "/profilePicture";
		boolean uploaded = util.uploadImage(filePart, "", saveFolder);

		if (uploaded) {
			String imageName = util.getImageNameFromPart(filePart);
			return "resources/images/system/profilePicture/" + imageName;
		}

		return null;
	}

	/**
	 * Gets the maximum allowed file size for uploads in bytes.
	 * 
	 * @return The maximum file size in bytes
	 */
	public long getMaxFileSize() {
		return MAX_FILE_SIZE;
	}

	/**
	 * Gets a user-friendly formatted string of the maximum file size.
	 * 
	 * @return The maximum file size as a formatted string (e.g. "3 MB")
	 */
	public String getFormattedMaxFileSize() {
		return (MAX_FILE_SIZE / (1024 * 1024)) + " MB";
	}
}