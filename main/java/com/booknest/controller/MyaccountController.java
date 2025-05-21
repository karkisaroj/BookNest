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
import jakarta.servlet.http.Part;

import java.io.IOException;

/**
 * @author Saroj Karki 23047612
 */

@WebServlet(asyncSupported = true, urlPatterns = { "/myaccount" })
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

	// Request attribute constants
	private static final String PROFILE_IMAGE_URL_PARAM = "profileImageUrl";
	private static final String ERROR_MESSAGE_PARAM = "errorMessage";

	// Form parameter constants
	private static final String IMAGE_PARAM = "image";

	// Path constants
	private static final String LOGIN_PAGE_PATH = "/login";
	private static final String MYACCOUNT_PAGE_PATH = "/myaccount";
	private static final String MYACCOUNT_JSP_PATH = "/WEB-INF/pages/myaccount.jsp";

	// Resource path constants
	private static final String RESOURCE_PATH = "resources";
	private static final String PROFILE_IMAGE_DIR = "images/UploadedProfilePicture";
	private static final String RELATIVE_IMAGE_PATH_PREFIX = "resources/images/UploadedProfilePicture/";

	// Message constants
	private static final String FILE_SIZE_ERROR_MESSAGE = "File size exceeds the maximum allowed size of 3 MB.";
	private static final String DB_UPDATE_ERROR_MESSAGE = "Failed to update profile picture information in the database.";
	private static final String UPLOAD_ERROR_MESSAGE = "Failed to upload the profile picture.";
	private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred while updating your profile.";
	private static final String PROFILE_UPDATE_SUCCESS_MESSAGE = "Profile picture updated successfully!";

	// Configuration constants
	private static final int MAX_FILE_SIZE_MB = 3;
	private static final int MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if user is logged in
		if (!SessionUtil.isLoggedIn(request, USERNAME_SESSION_KEY)) {
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE_PATH);
			return;
		}

		// Get user attributes from session
		String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
		String firstName = SessionUtil.getAttribute(request, FIRST_NAME_SESSION_KEY, String.class);
		String lastName = SessionUtil.getAttribute(request, LAST_NAME_SESSION_KEY, String.class);
		String email = SessionUtil.getAttribute(request, EMAIL_SESSION_KEY, String.class);
		String phoneNumber = SessionUtil.getAttribute(request, PHONE_SESSION_KEY, String.class);
		String address = SessionUtil.getAttribute(request, ADDRESS_SESSION_KEY, String.class);

		// Get profile image URL
		String profileImageUrl = userService.getProfileImageUrl(userName);

		// Set attributes for the JSP
		request.setAttribute(USERNAME_SESSION_KEY, userName);
		request.setAttribute(FIRST_NAME_SESSION_KEY, firstName);
		request.setAttribute(LAST_NAME_SESSION_KEY, lastName);
		request.setAttribute(EMAIL_SESSION_KEY, email);
		request.setAttribute(PHONE_SESSION_KEY, phoneNumber);
		request.setAttribute(ADDRESS_SESSION_KEY, address);
		request.setAttribute(PROFILE_IMAGE_URL_PARAM, profileImageUrl);

		// Check for flash messages
		String successMessage = SessionUtil.getAttribute(request, SUCCESS_MESSAGE_KEY, String.class);
		if (successMessage != null) {
			request.setAttribute(SUCCESS_MESSAGE_KEY, successMessage);
			SessionUtil.removeAttribute(request, SUCCESS_MESSAGE_KEY);
		}

		// Forward to the JSP
		request.getRequestDispatcher(MYACCOUNT_JSP_PATH).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if user is logged in
		String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE_PATH);
			return;
		}

		String successFlashMessage = null;
		String errorMessage = null;

		try {
			// Get uploaded file
			Part filePart = request.getPart(IMAGE_PARAM);

			// Validate file size (3MB limit)
			if (filePart.getSize() > MAX_FILE_SIZE_BYTES) {
				errorMessage = FILE_SIZE_ERROR_MESSAGE;
				request.setAttribute(ERROR_MESSAGE_PARAM, errorMessage);
				doGet(request, response);
				return;
			}

			// Save the profile image to UploadedProfilePicture folder
			imageUtil util = new imageUtil();
			boolean uploaded = util.uploadImage(filePart, getServletContext().getRealPath(RESOURCE_PATH),
					PROFILE_IMAGE_DIR);

			if (uploaded) {
				String imageName = util.getImageNameFromPart(filePart);
				// The path to store in database
				String relativePath = RELATIVE_IMAGE_PATH_PREFIX + imageName;

				// Update database with new image URL
				boolean updated = userService.updateProfileImageUrl(userName, relativePath);

				if (updated) {
					successFlashMessage = PROFILE_UPDATE_SUCCESS_MESSAGE;
				} else {
					errorMessage = DB_UPDATE_ERROR_MESSAGE;
				}
			} else {
				errorMessage = UPLOAD_ERROR_MESSAGE;
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = UNEXPECTED_ERROR_MESSAGE;
		}

		// Handle response based on outcome
		if (errorMessage != null) {
			request.setAttribute(ERROR_MESSAGE_PARAM, errorMessage);
			doGet(request, response);
		} else {
			SessionUtil.setAttribute(request, SUCCESS_MESSAGE_KEY, successFlashMessage);
			response.sendRedirect(request.getContextPath() + MYACCOUNT_PAGE_PATH);
		}
	}
}