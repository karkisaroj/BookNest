package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.booknest.model.UserModel;
import com.booknest.service.LoginService;
import com.booknest.util.RedirectionUtil;
import com.booknest.util.SessionUtil;
import com.booknest.util.ValidationUtil;

/**
 * Controller for handling user authentication and login functionality.
 * Validates user credentials, manages session creation, and redirects users 
 * to appropriate pages based on their role.
 * 
 * @author 23047591 Noble-Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Error message constants
    private final String emptyMessage = "Empty Fields. Fill all the fields before logging in ";
    private final String alphanumericmessage = "User Name should start from alphabet and can contain only letters and numbers";
    private final String loginFailedMessage = "Invalid credentials. Please try again.";
    private final String connectionErrorMessage = "Connection error. Please try again later.";

    // Path constants
    private final String loginpagepath = "/WEB-INF/pages/login.jsp";
    private final String homePagePath = "/home";
    private final String adminDashboardPath = "/admindashboard";
    
    // Role constants
    private final String ADMIN_ROLE = "Admin";
    
    // Services and utilities
    private RedirectionUtil redirectionUtil;
    private ValidationUtil validationUtil;
    private LoginService loginService;

    /**
     * Initializes the servlet by creating service and utility instances.
     * This method is called once when the servlet is first loaded.
     * 
     * @throws ServletException if a servlet-specific error occurs during initialization
     */
    @Override
    public void init() throws ServletException {
        this.redirectionUtil = new RedirectionUtil();
        this.validationUtil = new ValidationUtil();
        this.loginService = new LoginService();
    }

    /**
     * Handles GET requests for the login page.
     * Simply forwards the request to the login JSP page.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(loginpagepath).forward(request, response);
    }

    /**
     * Handles POST requests for login form submission.
     * Validates credentials, creates user session, and redirects to appropriate page.
     * 
     * @param req  The HttpServletRequest object containing login form data
     * @param resp The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");

        // Validate that both username and password are provided
        if (validationUtil.isNullOrEmpty(userName) || validationUtil.isNullOrEmpty(password)) {
            redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", emptyMessage);
            return;
        }

        // Validate username format (must start with letter and contain only alphanumeric characters)
        if (!validationUtil.isAlphanumericStartingWithLetter(userName)) {
            redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", alphanumericmessage);
            return;
        }

        // Create a UserModel with the provided credentials
        UserModel userModel = new UserModel(userName, password);

        // Authenticate user credentials
        Boolean loginStatus = loginService.loginUser(userModel);

        // Handle database connection errors
        if (loginStatus == null) {
            redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", connectionErrorMessage);
            return;
        }

        // Process successful login
        if (loginStatus) {
            // Retrieve user details including role information
            UserModel userInfo = loginService.getUserInfoWithRole(userName);
            
            // Create session with user information if user details were retrieved
            if (userInfo != null) {
                // Store user details in session for use throughout the application
                SessionUtil.setAttribute(req, "userID", userInfo.getId());
                SessionUtil.setAttribute(req, "firstName", userInfo.getFirstName());
                SessionUtil.setAttribute(req, "lastName", userInfo.getLastName());
                SessionUtil.setAttribute(req, "userName", userInfo.getUserName());
                SessionUtil.setAttribute(req, "email", userInfo.getEmail());
                SessionUtil.setAttribute(req, "phoneNumber", userInfo.getPhoneNumber());
                SessionUtil.setAttribute(req, "address", userInfo.getAddress());
                SessionUtil.setAttribute(req, "rolename", userInfo.getRoleName());
            }
            
            // Redirect based on user role
            if (ADMIN_ROLE.equals(userInfo.getRoleName())) {
                // Admin users go to admin dashboard
                resp.sendRedirect(req.getContextPath() + adminDashboardPath);
            } else {
                // Regular users go to home page
                resp.sendRedirect(req.getContextPath() + homePagePath);
            }
        } else {
            // Handle failed login attempt
            redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", loginFailedMessage);
        }
    }
}