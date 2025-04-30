package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Piyush Chand
 */
<<<<<<< HEAD
<<<<<<< HEAD
@WebServlet(asyncSupported = true, urlPatterns = { "/customer" })
public class Customer extends HttpServlet {
=======
@WebServlet(asyncSupported = true, urlPatterns = { "/admincustomer" })
public class customer extends HttpServlet {
>>>>>>> 8d4643d10df7e36983fb3d8b7f94064895fc0102
=======

@WebServlet(asyncSupported = true, urlPatterns = { "/customer" })
public class Customer extends HttpServlet {

@WebServlet(asyncSupported = true, urlPatterns = { "/admincustomer" })
public class customer extends HttpServlet {

>>>>>>> 885d1dc3eb3e72562596d19da0526cbdf10e4f62
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("/WEB-INF/pages/customer.jsp").forward(request, response);
	}

}
