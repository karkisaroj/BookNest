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
 * Manages form validation, database updates, and session synchronization.
 * 
 * @author 23047591 Noble-Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/accountsetting" })
public class AccountSettingController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    // Constants for session keys and paths
    private static final String USERNAME_SESSION_KEY = "userName";
    private static final String ACCOUNT_SETTINGS_JSP = "/WEB-INF/pages/AccountSetting.jsp";
    private static final String LOGIN_PATH = "/login";
    
    // Error message constants for personal info form
    private static final String PERSONAL_INFO_REQUIRED_FIELDS = "First name and last name are required fields.";
    private static final String PERSONAL_INFO_INVALID_PHONE = "Please enter a valid phone number (must be 10 digits and start with 98).";
    private static final String PERSONAL_INFO_UPDATE_FAILED = "Failed to update your information. Please try again later.";
    private static final String PERSONAL_INFO_UPDATE_SUCCESS = "Personal information updated successfully!";
    
    // Error message constants for email form
    private static final String EMAIL_REQUIRED_FIELDS = "Email and confirmation email are required fields.";
    private static final String EMAIL_MISMATCH = "Email and confirmation email do not match.";
    private static final String EMAIL_INVALID_FORMAT = "Please enter a valid email address.";
    private static final String EMAIL_ALREADY_IN_USE = "This email address is already in use by another account.";
    private static final String EMAIL_UPDATE_FAILED = "Failed to update your email. Please try again later.";
    private static final String EMAIL_UPDATE_SUCCESS = "Email address updated successfully!";
    
    // Error message constants for password form
    private static final String PASSWORD_REQUIRED_FIELDS = "All password fields are required.";
    private static final String PASSWORD_MISMATCH = "New password and confirmation do not match.";
    private static final String PASSWORD_REQUIREMENTS = "Password must contain at least one capital letter, one number, and one special character.";
    private static final String PASSWORD_CURRENT_INCORRECT = "Current password is incorrect.";
    private static final String PASSWORD_UPDATE_FAILED = "Failed to update your password. Please try again later.";
    private static final String PASSWORD_UPDATE_SUCCESS = "Password updated successfully!";
    
    // General errors
    private static final String LOAD_ACCOUNT_ERROR = "Failed to load your account information.";
    private static final String INVALID_FORM_SUBMISSION = "Invalid form submission";
    
    // Services and utilities
    private AccountSettingService accountSettingService;
    private ValidationUtil validationUtil;
    
    /**
     * Initializes the servlet by creating service and utility instances.
     * 
     * @throws ServletException if a servlet-specific error occurs during initialization
     */
    @Override
    public void init() throws ServletException {
        this.accountSettingService = new AccountSettingService();
        this.validationUtil = new ValidationUtil();
    }

    /**
     * Handles GET requests to display the account settings page.
     * Verifies the user is logged in and pre-populates form fields with current data.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(request, USERNAME_SESSION_KEY)) {
            response.sendRedirect(request.getContextPath() + LOGIN_PATH);
            return;
        }
        
        try {
            // Use session data to pre-populate form fields
            request.setAttribute("firstName", SessionUtil.getAttribute(request, "firstName", String.class));
            request.setAttribute("lastName", SessionUtil.getAttribute(request, "lastName", String.class));
            request.setAttribute("phoneNumber", SessionUtil.getAttribute(request, "phoneNumber", String.class));
            request.setAttribute("email", SessionUtil.getAttribute(request, "email", String.class));
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", LOAD_ACCOUNT_ERROR);
        }
        
        // Forward to the JSP
        request.getRequestDispatcher(ACCOUNT_SETTINGS_JSP).forward(request, response);
    }

    /**
     * Handles POST requests for updating account settings.
     * Routes requests to the appropriate handler method based on the form submitted.
     * 
     * @param request  The HttpServletRequest object containing form data
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(request, USERNAME_SESSION_KEY)) {
            response.sendRedirect(request.getContextPath() + LOGIN_PATH);
            return;
        }
        
        String userName = SessionUtil.getAttribute(request, USERNAME_SESSION_KEY, String.class);
        
        // Determine which form was submitted and process accordingly
        if (request.getParameter("first-name") != null) {
            processPersonalInfoUpdate(request, response, userName);
            
        } else if (request.getParameter("email") != null) {
            processEmailUpdate(request, response, userName);
            
        } else if (request.getParameter("current-password") != null) {
            processPasswordUpdate(request, response, userName);
            
        } else {
            request.setAttribute("errorMessage", INVALID_FORM_SUBMISSION);
            doGet(request, response);
        }
    }
    
    /**
     * Processes personal information update form submission.
     * Validates form inputs, updates the database, and synchronizes session attributes.
     * 
     * @param request  The HttpServletRequest containing form data
     * @param response The HttpServletResponse for redirecting
     * @param userName The current user's username
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void processPersonalInfoUpdate(HttpServletRequest request, HttpServletResponse response, String userName) 
            throws ServletException, IOException {
        
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("second-name");
        String phoneNumber = request.getParameter("phone-number");
        
        // Validate inputs
        if (validationUtil.isNullOrEmpty(firstName) || validationUtil.isNullOrEmpty(lastName)) {
            request.setAttribute("accountInfoError", PERSONAL_INFO_REQUIRED_FIELDS);
            doGet(request, response);
            return;
        }
        
        // Validate phone number if provided
        if (!validationUtil.isNullOrEmpty(phoneNumber) && !validationUtil.isValidPhoneNumber(phoneNumber)) {
            request.setAttribute("accountInfoError", PERSONAL_INFO_INVALID_PHONE);
            doGet(request, response);
            return;
        }
        
        // Update database
        boolean updated = accountSettingService.updatePersonalInfo(userName, firstName, lastName, phoneNumber);
        
        if (updated) {
            // Update session attributes
            SessionUtil.setAttribute(request, "firstName", firstName);
            SessionUtil.setAttribute(request, "lastName", lastName);
            SessionUtil.setAttribute(request, "phoneNumber", phoneNumber);
            
            request.setAttribute("accountInfoSuccess", PERSONAL_INFO_UPDATE_SUCCESS);
        } else {
            request.setAttribute("accountInfoError", PERSONAL_INFO_UPDATE_FAILED);
        }
        
        doGet(request, response);
    }
    
    /**
     * Processes email update form submission.
     * Validates email format, checks for availability, updates the database,
     * and synchronizes session attributes.
     * 
     * @param request  The HttpServletRequest containing form data
     * @param response The HttpServletResponse for redirecting
     * @param userName The current user's username
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void processEmailUpdate(HttpServletRequest request, HttpServletResponse response, String userName) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String confirmEmail = request.getParameter("confirm-email");
        
        // Validate inputs
        if (validationUtil.isNullOrEmpty(email) || validationUtil.isNullOrEmpty(confirmEmail)) {
            request.setAttribute("emailError", EMAIL_REQUIRED_FIELDS);
            doGet(request, response);
            return;
        }
        
        if (!email.equals(confirmEmail)) {
            request.setAttribute("emailError", EMAIL_MISMATCH);
            doGet(request, response);
            return;
        }
        
        if (!validationUtil.isValidEmail(email)) {
            request.setAttribute("emailError", EMAIL_INVALID_FORMAT);
            doGet(request, response);
            return;
        }
        
        // Check if email is taken by another user
        if (accountSettingService.isEmailTaken(email, userName)) {
            request.setAttribute("emailError", EMAIL_ALREADY_IN_USE);
            doGet(request, response);
            return;
        }
        
        // Update database
        boolean updated = accountSettingService.updateEmail(userName, email);
        
        if (updated) {
            // Update session attribute
            SessionUtil.setAttribute(request, "email", email);
            
            request.setAttribute("emailSuccess", EMAIL_UPDATE_SUCCESS);
        } else {
            request.setAttribute("emailError", EMAIL_UPDATE_FAILED);
        }
        
        doGet(request, response);
    }
    
    /**
     * Processes password update form submission.
     * Validates password complexity, verifies current password,
     * and updates to the new password if all checks pass.
     * 
     * @param request  The HttpServletRequest containing form data
     * @param response The HttpServletResponse for redirecting
     * @param userName The current user's username
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void processPasswordUpdate(HttpServletRequest request, HttpServletResponse response, String userName) 
            throws ServletException, IOException {
        
        String currentPassword = request.getParameter("current-password");
        String newPassword = request.getParameter("new-password");
        String confirmPassword = request.getParameter("confirm-new-password");
        
        // Validate inputs
        if (validationUtil.isNullOrEmpty(currentPassword) || 
            validationUtil.isNullOrEmpty(newPassword) || 
            validationUtil.isNullOrEmpty(confirmPassword)) {
            
            request.setAttribute("passwordError", PASSWORD_REQUIRED_FIELDS);
            doGet(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("passwordError", PASSWORD_MISMATCH);
            doGet(request, response);
            return;
        }
        
        if (!validationUtil.isValidPassword(newPassword)) {
            request.setAttribute("passwordError", PASSWORD_REQUIREMENTS);
            doGet(request, response);
            return;
        }
        
        // Verify current password
        if (!accountSettingService.verifyCurrentPassword(userName, currentPassword)) {
            request.setAttribute("passwordError", PASSWORD_CURRENT_INCORRECT);
            doGet(request, response);
            return;
        }
        
        // Update database
        boolean updated = accountSettingService.updatePassword(userName, newPassword);
        
        if (updated) {
            request.setAttribute("passwordSuccess", PASSWORD_UPDATE_SUCCESS);
        } else {
            request.setAttribute("passwordError", PASSWORD_UPDATE_FAILED);
        }
        
        doGet(request, response);
    }
}