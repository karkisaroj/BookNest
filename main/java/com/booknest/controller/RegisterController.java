package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.booknest.model.UserModel;
import com.booknest.service.RegistrationSerivce;
import com.booknest.util.RedirectionUtil;
import com.booknest.util.ValidationUtil;

/**
 * Controller for handling user registration functionality.
 * Validates user input, processes registration requests, and redirects
 * users to appropriate pages based on registration outcome.
 * 
 * @author 23047591 Noble-Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Validation error message constants
    private final String emptyMessage = "Empty Fields. Fill all the fields before logging in ";
    private final String emailvaliditymessage = "Invalid Email Address";
    private final String alphabeticmessage = "First Name and Last Name should be alphabets ";
    private final String alphanumericmessage = "User Name should start from alphabet and can contain only letters and numbers";
    private final String passwordvaliditymessage = "Password should contain alleast a capital letter, a number and a symbol";
    private final String phonenovaliditymessage = "Invalid Phone Number. Should be all numbers";
    private final String processingErrorMessage = "Error processing registration data.";
    private final String registrationFailedMessage = "Registration failed. Please try again.";
    private final String registrationSuccessMessage = "Registration successful! Please log in.";

    // Path constants
    private final String registerpagepath = "/WEB-INF/pages/register.jsp";
    private final String loginPagePath = "/login";
    
    // Services and utilities
    private RedirectionUtil redirectionUtil;
    private ValidationUtil validationUtil;
    private RegistrationSerivce registrationService;
    private UserModel userModel;

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
        this.registrationService = new RegistrationSerivce();
    }

    /**
     * Handles GET requests for the registration page.
     * Simply forwards the request to the registration JSP page.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(registerpagepath).forward(request, response);
    }

    /**
     * Handles POST requests for registration form submission.
     * Validates user input, creates user account, and handles the registration process.
     * 
     * @param req  The HttpServletRequest object containing registration form data
     * @param resp The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");

        // Validate all required fields are filled
        if (validationUtil.isNullOrEmpty(firstName) || validationUtil.isNullOrEmpty(lastName)
                || validationUtil.isNullOrEmpty(userName) || validationUtil.isNullOrEmpty(email)
                || validationUtil.isNullOrEmpty(password) || validationUtil.isNullOrEmpty(phoneNumber)
                || validationUtil.isNullOrEmpty(address)) {
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", emptyMessage);
            return;
        }

        // Validate first name and last name contain only alphabetic characters
        if (!validationUtil.isAlphabetic(firstName) || !validationUtil.isAlphabetic(lastName)) {
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", alphabeticmessage);
            return;
        }
        
        // Validate username format (alphanumeric starting with a letter)
        if (!validationUtil.isAlphanumericStartingWithLetter(userName)) {
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", alphanumericmessage);
            return;
        }
        
        // Validate email format
        if (!validationUtil.isValidEmail(email)) {
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", emailvaliditymessage);
            return;
        }
        
        // Validate phone number format
        if (!validationUtil.isValidPhoneNumber(phoneNumber)) {
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", phonenovaliditymessage);
            return;
        }

        // Validate password complexity
        if (!validationUtil.isValidPassword(password)) {
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", passwordvaliditymessage);
            return;
        }

        // Create user model from validated inputs
        try {
            userModel = extractUserModel(req);
        } catch (Exception e) {
            e.printStackTrace();
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", processingErrorMessage);
            return;
        }

        // Attempt to register the user in the database
        try {
            Boolean isRegistered = registrationService.addUser(userModel);
            
            // Handle registration result
            if (isRegistered != null && isRegistered) {
                // Registration successful, redirect to login page with success message
                redirectionUtil.setMsgAndRedirect(req, resp, loginPagePath, "success", registrationSuccessMessage);
            } else {
                // Registration failed, redirect back to registration page with error message
                redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", registrationFailedMessage);
            }
        } catch (Exception e) {
            // Handle specific registration errors (e.g., duplicate username)
            redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", e.getMessage());
        }
    }

    /**
     * Extracts user data from the request parameters and creates a UserModel object.
     * 
     * @param req The HttpServletRequest containing form parameters
     * @return A UserModel object populated with user registration data
     * @throws Exception if there's an error in extracting or creating the user model
     */
    private UserModel extractUserModel(HttpServletRequest req) throws Exception {
        String userName = req.getParameter("userName");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");
        
        return new UserModel(firstName, lastName, userName, password, email, phoneNumber, address);
    }
}