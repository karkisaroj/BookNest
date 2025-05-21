package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AboutUs controller servlet that handles requests to the about us page.
 * Provides information about the company, vision, mission, and team.
 * 
 * @author Noble-Nepal
 * 
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/aboutus" })
public class AboutUs extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Path constant
    private final String aboutUsPagePath = "/WEB-INF/pages/about-us.jsp";
    
    /**
     * Handles GET requests to the about us page.
     * Forwards the request to the about-us.jsp view for rendering.
     * This method does not require any data processing as the page
     * displays static content.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher(aboutUsPagePath).forward(request, response);
    }
}