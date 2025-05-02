package com.booknest.service;

import com.booknest.util.imageUtil;
import jakarta.servlet.http.Part;
import java.io.IOException;

public class ImageService {

	private static final long MAX_FILE_SIZE = 3 * 1024 * 1024; // 3MB
	private final imageUtil util;

	public ImageService() {
		this.util = new imageUtil();
	}

	/**
	 * Validate if a file is valid for upload
	 * 
	 * @param filePart The uploaded file part
	 * @return True if valid, false otherwise
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
	 * Upload an image to the server
	 * 
	 * @param filePart The uploaded file part
	 * @return The relative path to the saved image, or null if failed
	 * @throws IOException If an I/O error occurs
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
	 * Get the maximum allowed file size
	 * 
	 * @return The maximum file size in bytes
	 */
	public long getMaxFileSize() {
		return MAX_FILE_SIZE;
	}

	/**
	 * Get formatted max file size for display
	 * 
	 * @return The maximum file size as a string (e.g. "3 MB")
	 */
	public String getFormattedMaxFileSize() {
		return (MAX_FILE_SIZE / (1024 * 1024)) + " MB";
	}
}