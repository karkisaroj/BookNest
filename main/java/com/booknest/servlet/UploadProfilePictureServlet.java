package com.booknest.servlet;

import com.booknest.config.DbConfiguration;
import com.booknest.util.imageUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/uploadProfilePicture")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 3, // 3 MB
		maxRequestSize = 1024 * 1024 * 5 // 5 MB
)
public class UploadProfilePictureServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			// Check if the user is logged in
			String userName = (String) request.getSession().getAttribute("userName");
			if (userName == null) {
				response.sendRedirect(request.getContextPath() + "/login");
				return;
			}

			// Retrieve the image file (input field name="image")
			Part filePart = request.getPart("image");

			// Use the provided imageUtil to handle the upload
			imageUtil util = new imageUtil();
			String saveFolder = "/profile"; // This will be appended in getSavePath()
			boolean uploaded = util.uploadImage(filePart, "", saveFolder);

			if (uploaded) {
				// Compute the relative path (must match the public structure of your web app)
				String imageName = util.getImageNameFromPart(filePart);
				String relativePath = "resources/images/system/profile/" + imageName;

				// Update user_img_url in the user table for this user
				Connection conn = null;
				PreparedStatement ps = null;
				try {
					conn = DbConfiguration.getDbConnection();
					String sql = "UPDATE user SET user_img_url = ? WHERE user_name = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, relativePath);
					ps.setString(2, userName);
					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (ps != null)
						try {
							ps.close();
						} catch (Exception ignore) {
						}
					if (conn != null)
						try {
							conn.close();
						} catch (Exception ignore) {
						}
				}
			}

			// Redirect to the account controller's URL (which forwards to the jsp)
			response.sendRedirect(request.getContextPath() + "/myaccount");

		} 

	}


