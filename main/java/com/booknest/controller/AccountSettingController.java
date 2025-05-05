package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.booknest.service.AccountSettingService;
import com.booknest.util.SessionUtil;
import com.booknest.util.ValidationUtil;

/**
 * Controller for handling account settings functionality.
 * Allows users to update their personal information, email, and password.
 * 
 * @author Noble Nepal
 * @version 1.0
 * @since 2025-05-05
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/accountsetting" })
public class AccountSettingController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String USERNAME_SESSION_KEY = "userName";
    private static final String ACCOUNT_SETTINGS_JSP = "/WEB-INF/pages/AccountSetting.jsp";
    
    private AccountSettingService accountSettingService;
    private ValidationUtil validationUtil;
    
    @Override
    public void init() throws ServletException {
        this.accountSettingService = new AccountSettingService();
        this.validationUtil = new ValidationUtil();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(">>> AccountSettingController: Processing GET request");
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(request, USERNAME_SESSION_KEY)) {
            System.out.println(">>> AccountSettingController: User not logged in, redirecting to login page");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get the username from session
        String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
        System.out.println(">>> AccountSettingController: Loading account settings for user: " + userName);
        
        try {
            // Use session data to pre-populate form fields
            request.setAttribute("firstName", SessionUtil.getAttribute(request, "firstName", String.class));
            request.setAttribute("lastName", SessionUtil.getAttribute(request, "lastName", String.class));
            request.setAttribute("phoneNumber", SessionUtil.getAttribute(request, "phoneNumber", String.class));
            request.setAttribute("email", SessionUtil.getAttribute(request, "email", String.class));
            
        } catch (Exception e) {
            System.err.println(">>> AccountSettingController: Error retrieving user information: " + e.getMessage());
            request.setAttribute("errorMessage", "Failed to load your account information.");
        }
        
        // Forward to the JSP
        System.out.println(">>> AccountSettingController: Forwarding to JSP: " + ACCOUNT_SETTINGS_JSP);
        request.getRequestDispatcher(ACCOUNT_SETTINGS_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(">>> AccountSettingController: Processing POST request");
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(request, USERNAME_SESSION_KEY)) {
            System.out.println(">>> AccountSettingController: User not logged in, redirecting to login page");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
        
        // Determine which form was submitted
        if (request.getParameter("first-name") != null) {
            System.out.println(">>> AccountSettingController: Processing personal information update for user: " + userName);
            processPersonalInfoUpdate(request, response, userName);
            
        } else if (request.getParameter("email") != null) {
            System.out.println(">>> AccountSettingController: Processing email update for user: " + userName);
            processEmailUpdate(request, response, userName);
            
        } else if (request.getParameter("current-password") != null) {
            System.out.println(">>> AccountSettingController: Processing password update for user: " + userName);
            processPasswordUpdate(request, response, userName);
            
        } else {
            System.err.println(">>> AccountSettingController: Invalid form submission, no recognized parameters");
            request.setAttribute("errorMessage", "Invalid form submission");
            doGet(request, response);
        }
    }
    
    /**
     * Process personal information update form submission
     */
    private void processPersonalInfoUpdate(HttpServletRequest request, HttpServletResponse response, String userName) 
            throws ServletException, IOException {
        
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("second-name");
        String phoneNumber = request.getParameter("phone-number");
        
        // Log received data
        System.out.println(">>> AccountSettingController: Received form data - firstName: " + firstName 
                + ", lastName: " + lastName + ", phoneNumber: " + phoneNumber);
        
        // Validate inputs
        if (validationUtil.isNullOrEmpty(firstName) || validationUtil.isNullOrEmpty(lastName)) {
            System.err.println(">>> AccountSettingController: Invalid form data - firstName or lastName is empty");
            request.setAttribute("accountInfoError", "First name and last name are required fields.");
            doGet(request, response);
            return;
        }
        
        // Validate phone number if provided
        if (!validationUtil.isNullOrEmpty(phoneNumber) && !validationUtil.isValidPhoneNumber(phoneNumber)) {
            System.err.println(">>> AccountSettingController: Invalid phone number format: " + phoneNumber);
            request.setAttribute("accountInfoError", "Please enter a valid phone number (must be 10 digits and start with 98).");
            doGet(request, response);
            return;
        }
        
        // Update database
        boolean updated = accountSettingService.updatePersonalInfo(userName, firstName, lastName, phoneNumber);
        
        if (updated) {
            System.out.println(">>> AccountSettingController: Successfully updated personal information for user: " + userName);
            
            // Update session attributes
            SessionUtil.setAttribute(request, "firstName", firstName);
            SessionUtil.setAttribute(request, "lastName", lastName);
            SessionUtil.setAttribute(request, "phoneNumber", phoneNumber);
            
            request.setAttribute("accountInfoSuccess", "Personal information updated successfully!");
        } else {
            System.err.println(">>> AccountSettingController: Failed to update personal information for user: " + userName);
            request.setAttribute("accountInfoError", "Failed to update your information. Please try again later.");
        }
        
        doGet(request, response);
    }
    
    /**
     * Process email update form submission
     */
    private void processEmailUpdate(HttpServletRequest request, HttpServletResponse response, String userName) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String confirmEmail = request.getParameter("confirm-email");
        
        // Log received data
        System.out.println(">>> AccountSettingController: Received form data - email: " + email 
                + ", confirmEmail: " + confirmEmail);
        
        // Validate inputs
        if (validationUtil.isNullOrEmpty(email) || validationUtil.isNullOrEmpty(confirmEmail)) {
            System.err.println(">>> AccountSettingController: Invalid form data - email or confirmEmail is empty");
            request.setAttribute("emailError", "Email and confirmation email are required fields.");
            doGet(request, response);
            return;
        }
        
        if (!email.equals(confirmEmail)) {
            System.err.println(">>> AccountSettingController: Email mismatch: " + email + " vs " + confirmEmail);
            request.setAttribute("emailError", "Email and confirmation email do not match.");
            doGet(request, response);
            return;
        }
        
        if (!validationUtil.isValidEmail(email)) {
            System.err.println(">>> AccountSettingController: Invalid email format: " + email);
            request.setAttribute("emailError", "Please enter a valid email address.");
            doGet(request, response);
            return;
        }
        
        // Check if email is taken by another user
        if (accountSettingService.isEmailTaken(email, userName)) {
            System.err.println(">>> AccountSettingController: Email already in use by another account: " + email);
            request.setAttribute("emailError", "This email address is already in use by another account.");
            doGet(request, response);
            return;
        }
        
        // Update database
        boolean updated = accountSettingService.updateEmail(userName, email);
        
        if (updated) {
            System.out.println(">>> AccountSettingController: Successfully updated email for user: " + userName);
            
            // Update session attribute
            SessionUtil.setAttribute(request, "email", email);
            
            request.setAttribute("emailSuccess", "Email address updated successfully!");
        } else {
            System.err.println(">>> AccountSettingController: Failed to update email for user: " + userName);
            request.setAttribute("emailError", "Failed to update your email. Please try again later.");
        }
        
        doGet(request, response);
    }
    
    /**
     * Process password update form submission
     */
    private void processPasswordUpdate(HttpServletRequest request, HttpServletResponse response, String userName) 
            throws ServletException, IOException {
        
        String currentPassword = request.getParameter("current-password");
        String newPassword = request.getParameter("new-password");
        String confirmPassword = request.getParameter("confirm-new-password");
        
        // Log received data (omitting actual passwords for security)
        System.out.println(">>> AccountSettingController: Processing password update request");
        
        // Validate inputs
        if (validationUtil.isNullOrEmpty(currentPassword) || 
            validationUtil.isNullOrEmpty(newPassword) || 
            validationUtil.isNullOrEmpty(confirmPassword)) {
            
            System.err.println(">>> AccountSettingController: Invalid form data - one or more password fields are empty");
            request.setAttribute("passwordError", "All password fields are required.");
            doGet(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            System.err.println(">>> AccountSettingController: Password mismatch - new password and confirmation don't match");
            request.setAttribute("passwordError", "New password and confirmation do not match.");
            doGet(request, response);
            return;
        }
        
        if (!validationUtil.isValidPassword(newPassword)) {
            System.err.println(">>> AccountSettingController: Invalid password format - doesn't meet complexity requirements");
            request.setAttribute("passwordError", 
                "Password must contain at least one capital letter, one number, and one special character.");
            doGet(request, response);
            return;
        }
        
        // Verify current password
        if (!accountSettingService.verifyCurrentPassword(userName, currentPassword)) {
            System.err.println(">>> AccountSettingController: Current password verification failed for user: " + userName);
            request.setAttribute("passwordError", "Current password is incorrect.");
            doGet(request, response);
            return;
        }
        
        // Update database
        boolean updated = accountSettingService.updatePassword(userName, newPassword);
        
        if (updated) {
            System.out.println(">>> AccountSettingController: Successfully updated password for user: " + userName);
            request.setAttribute("passwordSuccess", "Password updated successfully!");
        } else {
            System.err.println(">>> AccountSettingController: Failed to update password for user: " + userName);
            request.setAttribute("passwordError", "Failed to update your password. Please try again later.");
        }
        
        doGet(request, response);
    }
}