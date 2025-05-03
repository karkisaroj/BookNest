package com.booknest.util;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.http.Part;

/**
 * Utility class for handling image file uploads.
 */
public class imageUtil {

	/**
	 * Extracts the file name from the given Part object.
	 */
	public String getImageNameFromPart(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		String imageName = null;

		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}

		// Replace spaces with underscores to avoid URL issues
		if (imageName != null && !imageName.isEmpty()) {
			imageName = imageName.replaceAll("\\s+", "_");
			return imageName;
		}

		return "default_profile.png";
	}

	/**
	 * Uploads an image to the specified folder.
	 */
	public boolean uploadImage(Part part, String realPath, String saveFolder) {
		try {
			// Get the image name
			String imageName = getImageNameFromPart(part);

			// 1. Upload to the deployed location
			String deployedPath = realPath + File.separator + saveFolder;
			File deployedDir = new File(deployedPath);
			if (!deployedDir.exists()) {
				deployedDir.mkdirs();
			}
			String deployedFilePath = deployedPath + File.separator + imageName;
			System.out.println("Writing to deployed path: " + deployedFilePath);
			part.write(deployedFilePath);

			// 2. Also upload to the source project location for persistence
			String projectPath = "C:\\\\Users\\\\noble\\\\NewWorkSpaceCoursework\\\\BookNest\\\\src\\\\main\\\\webapp\\\\resources\\\\"
					+ saveFolder.replace('/', File.separatorChar);
			File projectDir = new File(projectPath);
			if (!projectDir.exists()) {
				projectDir.mkdirs();
			}

			// Copy the file from deployed location to project location
			try {
				java.nio.file.Files.copy(new File(deployedFilePath).toPath(),
						new File(projectPath + File.separator + imageName).toPath(),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Also copied to project path: " + projectPath + File.separator + imageName);
			} catch (Exception e) {
				System.err.println("Warning: Could not copy to project source: " + e.getMessage());
				// Don't fail the upload if this secondary copy fails
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getSavePath(String saveFolder) {
		return "C:\\Users\\noble\\NewWorkSpaceCoursework\\BookNest\\src\\main\\webapp\\resources\\" + saveFolder;
	}
}