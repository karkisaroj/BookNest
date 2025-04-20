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
import com.booknest.util.ValidationUtil;

/**
 * @author Noble Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String emptyMessage = "Empty Fields. Fill all the fields before logging in ";
	private final String alphanumericmessage = "User Name should start from alphabet and can contain only letters and numbers";
	private final String passwordvaliditymessage = "Password should contain alleast a capital letter, a number and a symbol";
	private final String loginFailedMessage = "Invalid credentials. Please try again.";
    private final String connectionErrorMessage = "Connection error. Please try again later.";


	private final String loginpagepath = "/WEB-INF/pages/login.jsp";
	private final String homepagepath = "/WEB-INF/pages/home.jsp";
//	private final String adminpagepath = "/WEB-INF/pages/admindashboard.jsp";
	private RedirectionUtil redirectionUtil;
	private ValidationUtil validationUtil;
	private LoginService loginService;
	@Override
	public void init() throws ServletException {
		this.redirectionUtil = new RedirectionUtil();
		this.validationUtil = new ValidationUtil();
		this.loginService=new LoginService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher(loginpagepath).forward(request, response);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String userName = req.getParameter("userName");
	    String password = req.getParameter("password");

	    if (validationUtil.isNullOrEmpty(userName) || validationUtil.isNullOrEmpty(password)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", emptyMessage);
	        return;
	    }

	    // Checking userName format (must start with letter and contain alphanumeric characters)
	    if (!validationUtil.isAlphanumericStartingWithLetter(userName)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", alphanumericmessage);
	        return;
	    }

//	    // Checking password format (if you want to enforce the password pattern)
//	    if (!validationUtil.isValidPassword(password)) {
//	        redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", passwordvaliditymessage);
//	        return;
//	    }

	    // Create a UserModel with the user-provided username and password
	    UserModel userModel = new UserModel(userName, password);

	    // Validate user credentials using LoginService
	    Boolean loginStatus = loginService.loginUser(userModel);

	    if (loginStatus == null) {
	        // Connection error occurred
	        redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", connectionErrorMessage);
	        return;
	    }

	    
	    if (!validationUtil.isValidPassword(password)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", passwordvaliditymessage);
	        return;
	    }

		


	    if (loginStatus) {

	        req.getRequestDispatcher(homepagepath).forward(req, resp);
	    } else {
	        // Redirect back to login page with failure message
	        redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", loginFailedMessage);
	    }

	}
}