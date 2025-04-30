package com.booknest.controller;

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

@WebServlet("/myaccount")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
	    maxFileSize = 1024 * 1024 * 10,      // 10MB
	    maxRequestSize = 1024 * 1024 * 50 )   // 50MB

public class MyAccountController extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user information (e.g., username) from the session
        String userName = (String) request.getSession().getAttribute("userName");
        

        if (userName == null) {
            // Redirect to login if user is not logged in
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Retrieve profile image URL from the database
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbConfiguration.getDbConnection();
            String sql = "SELECT user_img_url FROM user WHERE user_name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String profileImageUrl = rs.getString("user_img_url");
                if (profileImageUrl != null && !profileImageUrl.trim().isEmpty()) {
                    request.setAttribute("profileImageUrl", profileImageUrl);
                } else {
                    // Default placeholder image
                    request.setAttribute("profileImageUrl", "resources/images/system/default.png");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ignore) {}
        }

        // Forward to the JSP
        request.getRequestDispatcher("/WEB-INF/pages/myaccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve username from session
        String userName = (String) request.getSession().getAttribute("userName");
        if (userName == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // File upload logic
        try {
            Part filePart = request.getPart("image");
            
            if (filePart.getSize() > 3 * 1024 * 1024) { // 3 MB size limit
                request.setAttribute("errorMessage", "File size exceeds the maximum allowed size of 3 MB.");
                request.getRequestDispatcher("/WEB-INF/pages/myaccount.jsp").forward(request, response);
                return;
            }

            // Save the file using imageUtil
            imageUtil util = new imageUtil();
            String saveFolder = "/profile";
            boolean uploaded = util.uploadImage(filePart, "", saveFolder);

            if (uploaded) {
                String imageName = util.getImageNameFromPart(filePart);
     
                String relativePath = "resources/images/system/profile/" + imageName;

                // Update the database with the new image URL
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
                    try {
                        if (ps != null) ps.close();
                        if (conn != null) conn.close();
                    } catch (Exception ignore) {}
                }
            }

            // Redirect to myaccount page after successful upload
            response.sendRedirect(request.getContextPath() + "/myaccount");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred during file upload.");
            request.getRequestDispatcher("/WEB-INF/pages/myaccount.jsp").forward(request, response);
        }
    }
}