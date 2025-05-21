package com.booknest.util;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.http.Part;

/**
 * @author Saroj Pratap Karki 23047612
 */
public class imageUtil {

	public String getImageNameFromPart(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		String imageName = null;

		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
				break;
			}
		}

		if (imageName != null && !imageName.isEmpty()) {
			return imageName.replaceAll("\\s+", "_");
		}
		return "default_profile.png";
	}

	public boolean uploadImage(Part part, String realPath, String saveFolder) {
		try {
			if (part == null || part.getSize() == 0) {
				System.err.println("No file uploaded or empty file");
				return false;
			}

			String imageName = getImageNameFromPart(part);
			String normalizedFolder = saveFolder.replace('\\', '/');

			// Try multiple possible paths to handle different deployment scenarios
			String[] possiblePaths = {
					// Original hardcoded path
					"C:\\Users\\noble\\NewWorkSpaceCoursework\\BookNest\\src\\main\\webapp\\resources\\"
							+ normalizedFolder,

					// Path from servlet context (if running in server)
					realPath + File.separator + normalizedFolder,

					// Relative path from current directory
					System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
							+ "webapp" + File.separator + "resources" + File.separator + normalizedFolder,

					// Fallback path directly in resources
					"resources/" + normalizedFolder };

			// Try each path until one works
			for (String projectPath : possiblePaths) {
				try {
					System.out.println("Attempting to use path: " + projectPath);

					// Create directory if needed
					File projectDir = new File(projectPath);
					if (!projectDir.exists()) {
						boolean created = projectDir.mkdirs();
						System.out.println("Created directory: " + created);
					}

					// Check if directory exists and is writable
					if (!projectDir.exists() || !projectDir.canWrite()) {
						System.err.println("Directory doesn't exist or isn't writable: " + projectPath);
						continue;
					}

					String filePath = projectPath + File.separator + imageName;
					System.out.println("Writing file to: " + filePath);

					// Try to write the file
					part.write(filePath);
					System.out.println("File successfully written to: " + filePath);
					return true;

				} catch (IOException e) {
					System.err.println("Error writing to path " + projectPath + ": " + e.getMessage());
					// Continue to next path
				}
			}

			// If we get here, all paths failed
			System.err.println("All attempted paths failed for file upload");
			return false;

		} catch (Exception e) {
			System.err.println("Unexpected error during upload: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public String getSavePath(String saveFolder) {
		return "resources/" + saveFolder.replace('\\', '/');
	}
}