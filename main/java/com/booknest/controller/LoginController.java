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
 * Noble Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String emptyMessage= "Empty Fields. Fill all the fields before logging in ";
	private RedirectionUtil redirectionUtil;
	
	
	
	public LoginController(RedirectionUtil redirectionUtil) {
		this.redirectionUtil=redirectionUtil;
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName=req.getParameter("userName");
		String password=req.getParameter("password");
		
		if(ValidationUtil.isNullOrEmpty(userName) ||ValidationUtil.isNullOrEmpty(password) ) {
			redirectionUtil.setMsgAndRedirect(req, resp, "/WEB-INF/pages/login.jsp","error" , emptyMessage);
            return;
            
		}
		
	}
};
