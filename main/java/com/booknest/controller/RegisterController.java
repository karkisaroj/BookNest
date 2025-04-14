package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.booknest.util.RedirectionUtil;
import com.booknest.util.ValidationUtil;

/**
 * @author Noble Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String emptyMessage = "Empty Fields. Fill all the fields before logging in ";
	private final String emailvaliditymessage = "Invalid Email Address";
	private final String alphabeticmessage = "First Name and Last Name should be alphabets ";
	private final String alphanumericmessage = "User Name should start from alphabet and can contain only letters and numbers";
	private final String passwordvaliditymessage = "Password should contain alleast a capital letter, a number and a symbol";
	private final String phonenovaliditymessage = "Invalid Phone Number. Should be all numbers";

	private final String registerpagepath = "/WEB-INF/pages/register.jsp";
	private RedirectionUtil redirectionUtil;
	private ValidationUtil validationUtil;

	@Override
	public void init() throws ServletException {
		this.redirectionUtil = new RedirectionUtil();
		this.validationUtil = new ValidationUtil();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String phoneNumber = req.getParameter("phoneNumber");

		// Check for empty fields
		if (validationUtil.isNullOrEmpty(firstName) || validationUtil.isNullOrEmpty(lastName)
				|| validationUtil.isNullOrEmpty(userName) || validationUtil.isNullOrEmpty(email)
				|| validationUtil.isNullOrEmpty(password) || validationUtil.isNullOrEmpty(phoneNumber)) {
			redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", emptyMessage);
			return;
		}

	    
	    //  Checking firstName and lastName format (must start with letter and contain alphabetic characters only)
	    if (!validationUtil.isAlphabetic(firstName)|| !validationUtil.isAlphabetic(lastName)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", alphabeticmessage);
	        return;
	    }
	    //  Checking userName format (must start with letter and contain alphanumeric characters)
	    if (!validationUtil.isAlphanumericStartingWithLetter(userName)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", alphanumericmessage);
	        return;
	    }
	    //  Checking email format 
	    if (!validationUtil.isValidEmail(email)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", emailvaliditymessage);
	        return;
	    }
	    //  Checking phoneNumber format 
	    if (!validationUtil.isValidPhoneNumber(phoneNumber)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", phonenovaliditymessage);
	        return;
	    }
	    
	    //  Checking password format (if you want to enforce the password pattern)
	    if (!validationUtil.isValidPassword(password)) {
	        redirectionUtil.setMsgAndRedirect(req, resp, registerpagepath, "error", passwordvaliditymessage);
	        return;
	    }

	}	
	

}
