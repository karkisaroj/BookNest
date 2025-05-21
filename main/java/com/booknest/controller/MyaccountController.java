package com.booknest.controller;

import com.booknest.service.UserService;
import com.booknest.service.UserServiceImpl;
import com.booknest.util.SessionUtil;
import com.booknest.util.imageUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;

/**
 * Controller for handling user account operations including profile image
 * uploads.
 * 
 * @author Saroj Karki 23047612
 */
@WebServlet("/myaccount")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50) // 50MB
public class MyAccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserServiceImpl();

	// Session attribute constants
	private static final String USERNAME_SESSION_KEY = "userName";
	private static final String FIRST_NAME_SESSION_KEY = "firstName";
	private static final String LAST_NAME_SESSION_KEY = "lastName";
	private static final String EMAIL_SESSION_KEY = "email";
	private static final String PHONE_SESSION_KEY = "phoneNumber";
	private static final String ADDRESS_SESSION_KEY = "address";
	private static final String SUCCESS_MESSAGE_KEY = "successMessage";
	private static final String ERROR_MESSAGE_KEY = "errorMessage";

	// Request attribute constants
	private static final String PROFILE_IMAGE_URL_PARAM = "profileImageUrl";

	// Form parameter constants
	private static final String IMAGE_PARAM = "image";

	// Path constants
	private static final String LOGIN_PAGE_PATH = "/login";
	// Fix this path to match your project structure
	private static final String MYACCOUNT_JSP_PATH = "/WEB-INF/pages/myaccount.jsp";

	// Resource path constants
	private static final String PROFILE_IMAGE_DIR = "images/UploadedProfilePicture";
	private static final String RELATIVE_IMAGE_PATH_PREFIX = "resources/images/UploadedProfilePicture/";

	// Message constants
	private static final String FILE_SIZE_ERROR_MESSAGE = "File size exceeds the maximum allowed size of 3 MB.";
	private static final String DB_UPDATE_ERROR_MESSAGE = "Failed to update profile picture information in the database.";
	private static final String UPLOAD_ERROR_MESSAGE = "Failed to upload the profile picture.";
	private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred while updating your profile.";
	private static final String PROFILE_UPDATE_SUCCESS_MESSAGE = "Profile picture updated successfully!";
	private static final String NO_FILE_SELECTED_MESSAGE = "No file was selected for upload.";

	// Configuration constants
	private static final int MAX_FILE_SIZE_MB = 3;
	private static final int MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("MyAccountController: doGet method called");

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute(USERNAME_SESSION_KEY);

		// Check if user is logged in
		if (userName == null || userName.isEmpty()) {
			System.out.println("User not logged in, redirecting to login page");
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE_PATH);
			return;
		}

		System.out.println("User logged in: " + userName);

		// Get user attributes from session
		String firstName = (String) session.getAttribute(FIRST_NAME_SESSION_KEY);
		String lastName = (String) session.getAttribute(LAST_NAME_SESSION_KEY);
		String email = (String) session.getAttribute(EMAIL_SESSION_KEY);
		String phoneNumber = (String) session.getAttribute(PHONE_SESSION_KEY);
		String address = (String) session.getAttribute(ADDRESS_SESSION_KEY);

		// Get profile image URL from service
		String profileImageUrl = userService.getProfileImageUrl(userName);
		System.out.println("Profile image URL: " + profileImageUrl);

		// Set attributes for the JSP
		request.setAttribute(USERNAME_SESSION_KEY, userName);
		request.setAttribute(FIRST_NAME_SESSION_KEY, firstName);
		request.setAttribute(LAST_NAME_SESSION_KEY, lastName);
		request.setAttribute(EMAIL_SESSION_KEY, email);
		request.setAttribute(PHONE_SESSION_KEY, phoneNumber);
		request.setAttribute(ADDRESS_SESSION_KEY, address);
		request.setAttribute(PROFILE_IMAGE_URL_PARAM, profileImageUrl);

		// Forward to the JSP with correct path
		System.out.println("Forwarding to " + MYACCOUNT_JSP_PATH);
		request.getRequestDispatcher(MYACCOUNT_JSP_PATH).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("MyAccountController: doPost method called");

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute(USERNAME_SESSION_KEY);

		// Check if user is logged in
		if (userName == null || userName.isEmpty()) {
			System.out.println("User not logged in, redirecting to login page");
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE_PATH);
			return;
		}

		try {
			// Get uploaded file
			Part filePart = request.getPart(IMAGE_PARAM);
			System.out.println("File part received: " + (filePart != null ? "yes" : "no"));

			if (filePart != null && filePart.getSize() > 0) {
				System.out.println("File size: " + filePart.getSize() + " bytes");

				// Validate file size
				if (filePart.getSize() > MAX_FILE_SIZE_BYTES) {
					System.out.println("File too large");
					session.setAttribute(ERROR_MESSAGE_KEY, FILE_SIZE_ERROR_MESSAGE);
					response.sendRedirect(request.getContextPath() + "/myaccount");
					return;
				}

				// Try to upload the file
				imageUtil util = new imageUtil();
				String realPath = getServletContext().getRealPath("");
				System.out.println("Real path: " + realPath);

				boolean uploaded = util.uploadImage(filePart, realPath, PROFILE_IMAGE_DIR);

				if (uploaded) {
					String imageName = util.getImageNameFromPart(filePart);
					String relativePath = RELATIVE_IMAGE_PATH_PREFIX + imageName;
					System.out.println("Image uploaded, path: " + relativePath);

					// Update database
					try {
						boolean updated = userService.updateProfileImageUrl(userName, relativePath);
						if (updated) {
							System.out.println("Database updated successfully");
							session.setAttribute(SUCCESS_MESSAGE_KEY, PROFILE_UPDATE_SUCCESS_MESSAGE);
						} else {
							System.out.println("Database update failed");
							session.setAttribute(ERROR_MESSAGE_KEY, DB_UPDATE_ERROR_MESSAGE);
						}
					} catch (Exception e) {
						System.err.println("Error updating database: " + e.getMessage());
						session.setAttribute(ERROR_MESSAGE_KEY, DB_UPDATE_ERROR_MESSAGE);
					}
				} else {
					System.err.println("File upload failed");
					session.setAttribute(ERROR_MESSAGE_KEY, UPLOAD_ERROR_MESSAGE);
				}
			} else {
				System.out.println("No file selected");
				session.setAttribute(ERROR_MESSAGE_KEY, NO_FILE_SELECTED_MESSAGE);
			}
		} catch (Exception e) {
			System.err.println("Exception during file upload: " + e.getMessage());
			e.printStackTrace();
			session.setAttribute(ERROR_MESSAGE_KEY, UNEXPECTED_ERROR_MESSAGE);
		}

		// Redirect back to account page
		response.sendRedirect(request.getContextPath() + "/myaccount");
	}
}