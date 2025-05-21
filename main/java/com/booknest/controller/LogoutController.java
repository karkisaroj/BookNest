package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.booknest.util.SessionUtil;

/**
 * Controller for handling user logout functionality.
 * Invalidates the user's session and redirects to the login page.
 * Supports POST requests for secure logout operations.
 * 
 * @author 23047591 Noble-Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/logout" })
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Path constants
    private final String LOGIN_PATH = "/login";
       
    /**
     * Handles POST requests for logout operations.
     * This method is used for secure logout with form submission.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processLogout(request, response);
    }
    
    /**
     * Common method to process logout for both GET and POST requests.
     * Invalidates the current session and redirects to the login page.
     * 
     * @param request  The HttpServletRequest object from which to get the session
     * @param response The HttpServletResponse object for redirection
     * @throws IOException if an I/O error occurs during redirection
     */
    private void processLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Invalidate the session, which removes all session attributes
        SessionUtil.invalidateSession(request);
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + LOGIN_PATH);
    }
}