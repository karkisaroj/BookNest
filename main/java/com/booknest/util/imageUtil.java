package com.booknest.util;

import java.io.File;
import jakarta.servlet.http.Part;

/**
 * Utility class for handling image file uploads.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class imageUtil {

	/**
	 * Extracts the file name from the given Part object.
	 * 
	 * @param part The uploaded file part
	 * @return The extracted filename with spaces replaced by underscores, or
	 *         default image name if not found
	 */
	public String getImageNameFromPart(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");

		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				String imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
				if (imageName != null && !imageName.isEmpty()) {
					// Replace spaces with underscores to avoid URL encoding issues
					return imageName.replaceAll("\\s+", "_");
				}
				break;
			}
		}

		// Return default image if no filename found
		return "default_profile.png";
	}

	/**
	 * Uploads an image to the specified folder.
	 * 
	 * @param part       The uploaded file part
	 * @param realPath   The real path to the web application root
	 * @param saveFolder The folder within the web application to save the file to
	 * @return true if upload was successful, false otherwise
	 */
	public boolean uploadImage(Part part, String realPath, String saveFolder) {
		try {
			// Get the image name
			String imageName = getImageNameFromPart(part);

			// Create directory if it doesn't exist
			String deployedPath = realPath + File.separator + saveFolder;
			File deployedDir = new File(deployedPath);
			if (!deployedDir.exists()) {
				deployedDir.mkdirs();
			}

			// Write the file to the destination
			String deployedFilePath = deployedPath + File.separator + imageName;
			part.write(deployedFilePath);

			return true;
		} catch (Exception e) {
			// Silently log the error but prevent stack trace from displaying to users
			e.printStackTrace();
			return false;
		}
	}
}