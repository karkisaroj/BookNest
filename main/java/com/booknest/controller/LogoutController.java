package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.booknest.util.SessionUtil;

/**
 * Servlet implementation class LogoutController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/logout" })
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     * @author Noble Nepal 23047591
     */
	



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processLogout(request, response);
	}
	   /**
     * Common method to process logout for both GET and POST requests.
     * Invalidates the current session and redirects to the login page.
     */
    private void processLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {

    	
        
        // Invalidate the session, which removes all session attributes
        SessionUtil.invalidateSession(request);
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }

}
