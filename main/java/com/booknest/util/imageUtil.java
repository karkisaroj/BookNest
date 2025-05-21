package com.booknest.util;

import java.io.File;
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
				System.out.println("No file uploaded or empty file");
				return false;
			}

			String imageName = getImageNameFromPart(part);
			String normalizedFolder = saveFolder.replace('\\', '/');

			// Direct path to project directory - ignore realPath parameter
			String projectPath = "C:\\Users\\noble\\NewWorkSpaceCoursework\\BookNest\\src\\main\\webapp\\resources\\" + normalizedFolder;
			File projectDir = new File(projectPath);
			if (!projectDir.exists()) {
				projectDir.mkdirs();
			}

			String filePath = projectPath + "/" + imageName;
			System.out.println("Writing directly to: " + filePath);

			// Write directly to the project directory
			part.write(filePath);

			System.out.println("File uploaded successfully to: " + filePath);
			return true;

		} catch (Exception e) {
			System.err.println("Error in uploadImage: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public String getSavePath(String saveFolder) {
		return "resources/" + saveFolder.replace('\\', '/');
	}
}