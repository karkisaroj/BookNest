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

@WebServlet(asyncSupported = true, urlPatterns = { "/myaccount" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50) // 50MB
public class MyaccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserServiceImpl();
	private static final String USERNAME_SESSION_KEY = "userName";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if user is logged in
		if (!SessionUtil.isLoggedIn(request, USERNAME_SESSION_KEY)) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// Get user attributes from session
		String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
		String firstName = SessionUtil.getAttribute(request, "firstName", String.class);
		String lastName = SessionUtil.getAttribute(request, "lastName", String.class);
		String email = SessionUtil.getAttribute(request, "email", String.class);
		String phoneNumber = SessionUtil.getAttribute(request, "phoneNumber", String.class);
		String address = SessionUtil.getAttribute(request, "address", String.class);

		// Get profile image URL
		String profileImageUrl = userService.getProfileImageUrl(userName);

		// Set attributes for the JSP
		request.setAttribute("userName", userName);
		request.setAttribute("firstName", firstName);
		request.setAttribute("lastName", lastName);
		request.setAttribute("email", email);
		request.setAttribute("phoneNumber", phoneNumber);
		request.setAttribute("address", address);
		request.setAttribute("profileImageUrl", profileImageUrl);

		// Check for flash messages
		String successMessage = SessionUtil.getAttribute(request, "successMessage", String.class);
		if (successMessage != null) {
			request.setAttribute("successMessage", successMessage);
			SessionUtil.removeAttribute(request, "successMessage");
		}

		// Forward to the JSP
		String forwardPath = "/WEB-INF/pages/myaccount.jsp";
		System.out.println(">>> MyaccountController forwarding to page: " + forwardPath);
		request.getRequestDispatcher(forwardPath).forward(request, response);
		System.out.println(">>> MyaccountController doGet END (after forward)");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if user is logged in
		String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String successFlashMessage = null;
		String errorMessage = null;

		try {
			// Get uploaded file
			Part filePart = request.getPart("image");

			// Validate file size (3MB limit)
			if (filePart.getSize() > 3 * 1024 * 1024) {
				errorMessage = "File size exceeds the maximum allowed size of 3 MB.";
				request.setAttribute("errorMessage", errorMessage);
				doGet(request, response);
				return;
			}

			// Save the profile image to UploadedProfilePicture folder
			imageUtil util = new imageUtil();
			String saveFolder = "images/UploadedProfilePicture";
			boolean uploaded = util.uploadImage(filePart, getServletContext().getRealPath("resources"), saveFolder);

			if (uploaded) {
				String imageName = util.getImageNameFromPart(filePart);
				// The path to store in database
				String relativePath = "resources/images/UploadedProfilePicture/" + imageName;

				// Update database with new image URL
				boolean updated = userService.updateProfileImageUrl(userName, relativePath);

				if (updated) {
					successFlashMessage = "Profile picture updated successfully!";
				} else {
					errorMessage = "Failed to update profile picture information in the database.";
				}
			} else {
				errorMessage = "Failed to upload the profile picture.";
			}

		} catch (Exception e) {
			System.err.println("User: " + userName + " - Error during profile update: " + e.getMessage());
			e.printStackTrace();
			errorMessage = "An unexpected error occurred while updating your profile.";
		}

		// Handle response based on outcome
		if (errorMessage != null) {
			System.out.println(">>> MyaccountController doPost ERROR: " + errorMessage);
			request.setAttribute("errorMessage", errorMessage);
			doGet(request, response);
		} else {
			System.out.println(">>> MyaccountController doPost SUCCESS");
			SessionUtil.setAttribute(request, "successMessage", successFlashMessage);
			response.sendRedirect(request.getContextPath() + "/myaccount");
		}

		System.out.println(">>> MyaccountController doPost END");
	}
}