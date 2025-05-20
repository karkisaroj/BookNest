package com.booknest.controller;

import java.io.IOException;
import java.math.BigDecimal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Saroj Karki 23047612
 */

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
		String paymentMethod = (String) session.getAttribute("paymentMethod");

		// If no order ID in session, generate a random one for demo purposes
		if (orderId == null) {
			orderId = 10000 + (int) (Math.random() * 90000);
		}

		// Always set payment status to "Completed"
		String paymentStatus = "Completed";

		// Set attributes for JSP
		request.setAttribute("orderId", orderId);
		request.setAttribute("orderTotal", orderTotal);
		request.setAttribute("orderDate", orderDate);
		request.setAttribute("successMessage", successMessage);
		request.setAttribute("paymentMethod", paymentMethod);
		request.setAttribute("paymentStatus", paymentStatus);

		try {
			// Forward to confirmation JSP
			request.getRequestDispatcher("/WEB-INF/pages/order-confirmation.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Clean up session attributes
		session.removeAttribute("orderId");
		session.removeAttribute("orderTotal");
		session.removeAttribute("orderDate");
		session.removeAttribute("flashSuccessMessage");
		session.removeAttribute("paymentMethod");
	}
}