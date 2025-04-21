package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ProductPage
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/productpage" })
public class ProductPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String productPagePath = "/WEB-INF/pages/product-page.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher(productPagePath).forward(request, response);
	}

}
