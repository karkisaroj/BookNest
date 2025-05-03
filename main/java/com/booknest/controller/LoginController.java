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
 * @author Noble Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String emptyMessage = "Empty Fields. Fill all the fields before logging in ";
	private final String alphanumericmessage = "User Name should start from alphabet and can contain only letters and numbers";





//	private final String passwordvaliditymessage = "Password should contain alleast a capital letter, a number and a symbol";


//private final String passwordvaliditymessage = "Password should contain minimum a capital letter, a number and a symbol";


	private final String loginFailedMessage = "Invalid credentials. Please try again.";
	private final String connectionErrorMessage = "Connection error. Please try again later.";

	private final String loginpagepath = "/WEB-INF/pages/login.jsp";
	private final String homepagepath = "/WEB-INF/pages/home.jsp";
	private final String adminpagepath = "/WEB-INF/pages/admindashboard.jsp";
	private RedirectionUtil redirectionUtil;
	private ValidationUtil validationUtil;
	private LoginService loginService;

	@Override
	public void init() throws ServletException {
		this.redirectionUtil = new RedirectionUtil();
		this.validationUtil = new ValidationUtil();
		this.loginService = new LoginService();
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

		// Checking userName format (must start with letter and contain alphanumeric
		// characters)
		if (!validationUtil.isAlphanumericStartingWithLetter(userName)) {
			redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", alphanumericmessage);
			return;
		}

		// Creating a UserModel with the user-provided user name and password
		UserModel userModel = new UserModel(userName, password);

		// Validating user credentials using LoginService
		Boolean loginStatus = loginService.loginUser(userModel);

		if (loginStatus == null) {
			// Connecting error occurred
			redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", connectionErrorMessage);
			return;
		}

		if (loginStatus) {
			// Fetching user information with role using the service method
			UserModel userInfo = loginService.getUserInfoWithRole(userName);
			System.out.println(userInfo.getRoleName());


			if (userInfo != null) {
				// Adding user details to the session created 
				SessionUtil.setAttribute(req, "userID", userInfo.getId());
				SessionUtil.setAttribute(req, "firstName", userInfo.getFirst_name());
				SessionUtil.setAttribute(req, "lastName", userInfo.getLast_name());
				SessionUtil.setAttribute(req, "userName", userInfo.getUser_name());
				SessionUtil.setAttribute(req, "email", userInfo.getEmail());
				SessionUtil.setAttribute(req, "phoneNumber", userInfo.getPhone_number());
				SessionUtil.setAttribute(req, "address", userInfo.getAddress());

				SessionUtil.setAttribute(req, "rolename", userInfo.getRoleName());
				System.out.println(userInfo.getRoleName());
			}
			if ("Admin".equals(userInfo.getRoleName())) {
				resp.sendRedirect(req.getContextPath()+"/admindashboard");
			} else {
				resp.sendRedirect(req.getContextPath()+"/home");
			}

		} else {
			// Redirecting back to login page with failure message
			redirectionUtil.setMsgAndRedirect(req, resp, loginpagepath, "error", loginFailedMessage);
		}

	}
}