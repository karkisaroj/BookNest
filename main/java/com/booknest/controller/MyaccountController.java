package com.booknest.controller;

import com.booknest.service.UserService;
import com.booknest.service.UserServiceImpl;
import com.booknest.util.SessionUtil; // Import the utility class

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// No longer need direct HttpSession import
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/myaccount")
@MultipartConfig( /* ... */ )
public class MyaccountController extends HttpServlet {

    private static final long serialVersionUID = 8L; // Increment version

    private final UserService userService = new UserServiceImpl();
    private static final String USERNAME_SESSION_KEY = "userName"; // Define session keys as constants

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request, USERNAME_SESSION_KEY)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
        String firstName = SessionUtil.getAttribute(request, "firstName", String.class);
        String lastName = SessionUtil.getAttribute(request, "lastName", String.class);
        String email = SessionUtil.getAttribute(request, "email", String.class);
        String phoneNumber = SessionUtil.getAttribute(request, "phoneNumber", String.class);
        String address = SessionUtil.getAttribute(request, "address", String.class);

        String profileImageUrl = userService.getProfileImageUrl(userName);

        request.setAttribute("userName", userName);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("address", address);
        request.setAttribute("profileImageUrl", profileImageUrl);


        String successMessage = SessionUtil.getAttribute(request, "successMessage", String.class);
        if (successMessage != null) {
 
            request.setAttribute("successMessage", successMessage);
            SessionUtil.removeAttribute(request, "successMessage"); 
        }


        // --- 6. Forward to the JSP ---
        String forwardPath = "/WEB-INF/pages/myaccount.jsp";
        System.out.println(">>> MyaccountController forwarding to page: " + forwardPath);
        request.getRequestDispatcher(forwardPath).forward(request, response);
        System.out.println(">>> MyaccountController doGet END (after forward)");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

         String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
        if (userName == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String successFlashMessage = null; 
        String errorMessage = null;

        try {
            Part filePart = request.getPart("image");
            userService.validateProfileImage(filePart);
            String relativeDbPath = userService.saveProfileImage(filePart);
            System.out.println(">>> MyaccountController saved image to relative path: " + relativeDbPath);
            boolean updated = userService.updateProfileImageUrl(userName, relativeDbPath);
            System.out.println(">>> MyaccountController DB update result: " + updated);
            if (updated) {
                successFlashMessage = "Profile picture updated successfully!"; // Prepare flash message
            } else {
                errorMessage = "Failed to update profile picture information in the database.";
            }
        } catch (IllegalArgumentException e) {
             System.err.println("User: " + userName + " - Invalid image upload: " + e.getMessage());
             errorMessage = e.getMessage();
        } catch (IOException | ServletException e) {
            System.err.println("User: " + userName + " - File upload processing error: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An error occurred during file upload. Please try again.";
        } catch (SQLException e) {
            System.err.println("User: " + userName + " - Database error updating profile image: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An error occurred while saving profile information.";
        } catch (Exception e) {
             System.err.println("User: " + userName + " - Unexpected error during profile update: " + e.getMessage());
             e.printStackTrace();
             errorMessage = "An unexpected error occurred.";
        }

        // --- Respond ---
        if (errorMessage != null) {
            System.out.println(">>> MyaccountController doPost ERROR: " + errorMessage);
            request.setAttribute("errorMessage", errorMessage);
            // Forward back via doGet to render the page correctly with error
            doGet(request, response);
        } else {
            System.out.println(">>> MyaccountController doPost SUCCESS");
             // Set flash message using SessionUtil before redirecting
             if (successFlashMessage != null) {
                 SessionUtil.setAttribute(request, "successMessage", successFlashMessage);
             }
             response.sendRedirect(request.getContextPath() + "/myaccount"); // Redirect to GET
        }
        System.out.println(">>> MyaccountController doPost END");
    }
}