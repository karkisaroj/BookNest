package com.booknest.controller;

import java.io.IOException;
import java.math.BigDecimal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order-confirmation")
public class OrderConfirmationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		// Get order details from session
		Integer orderId = (Integer) session.getAttribute("orderId");
		BigDecimal orderTotal = (BigDecimal) session.getAttribute("orderTotal");
		String orderDate = (String) session.getAttribute("orderDate");
		String successMessage = (String) session.getAttribute("flashSuccessMessage");

		// If no order ID in session, generate a random one for demo purposes
		if (orderId == null) {
			orderId = 10000 + (int) (Math.random() * 90000);
		}

		// Set attributes for JSP
		request.setAttribute("orderId", orderId);
		request.setAttribute("orderTotal", orderTotal);
		request.setAttribute("orderDate", orderDate);
		request.setAttribute("successMessage", successMessage);

		try {
			// Forward to confirmation JSP
			request.getRequestDispatcher("/WEB-INF/pages/order-confirmation.jsp").forward(request, response);
		} catch (Exception e) {
//			// Fallback to simple HTML response if JSP is not found
//			response.setContentType("text/html");
//			response.getWriter().println("<html><body>");
//			response.getWriter().println("<h1>Order Confirmed!</h1>");
//			response.getWriter().println("<p>Your order #" + orderId + " has been placed successfully.</p>");
//			response.getWriter().println("<p>Thank you for your purchase!</p>");
//			response.getWriter().println("<a href='" + request.getContextPath() + "/home'>Return to Home</a>");
//			response.getWriter().println("</body></html>");
		}

		// Clean up session attributes
		session.removeAttribute("orderId");
		session.removeAttribute("orderTotal");
		session.removeAttribute("orderDate");
		session.removeAttribute("flashSuccessMessage");
	}
}