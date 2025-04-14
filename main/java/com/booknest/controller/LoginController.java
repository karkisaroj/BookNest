package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Noble Nepal
 */
@WebServlet(asyncSupported=true, urlPatterns= { "/login"})
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String customermodel;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
	}
	
	public LoginController(String customermodel){
		this.customermodel=customermodel;
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		String username=request.getParameter("Username");
		String password=request.getParameter("Password");
		
		
		
		if(username.equals("customer")) {
			 response.sendRedirect(request.getContextPath() + "/home");
		}
	}

};
