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
 * Controller for handling order confirmation after checkout. Displays order
 * summary and payment status to the user.
 * 
 * @author Saroj Karki 23047612
 */

@WebServlet("/order-confirmation")
public class OrderConfirmationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Session attribute keys
	private static final String ORDER_ID_SESSION_KEY = "orderId";
	private static final String ORDER_TOTAL_SESSION_KEY = "orderTotal";
	private static final String ORDER_DATE_SESSION_KEY = "orderDate";
	private static final String FLASH_SUCCESS_SESSION_KEY = "flashSuccessMessage";
	private static final String PAYMENT_METHOD_SESSION_KEY = "paymentMethod";

	// Request attribute keys
	private static final String ORDER_ID_ATTR = "orderId";
	private static final String ORDER_TOTAL_ATTR = "orderTotal";
	private static final String ORDER_DATE_ATTR = "orderDate";
	private static final String SUCCESS_MESSAGE_ATTR = "successMessage";
	private static final String PAYMENT_METHOD_ATTR = "paymentMethod";
	private static final String PAYMENT_STATUS_ATTR = "paymentStatus";

	// Path constants
	private static final String CONFIRMATION_PAGE_PATH = "/WEB-INF/pages/order-confirmation.jsp";

	// Status constants
	private static final String PAYMENT_STATUS_COMPLETED = "Completed";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		// Get order details from session
		Integer orderId = (Integer) session.getAttribute(ORDER_ID_SESSION_KEY);
		BigDecimal orderTotal = (BigDecimal) session.getAttribute(ORDER_TOTAL_SESSION_KEY);
		String orderDate = (String) session.getAttribute(ORDER_DATE_SESSION_KEY);
		String successMessage = (String) session.getAttribute(FLASH_SUCCESS_SESSION_KEY);
		String paymentMethod = (String) session.getAttribute(PAYMENT_METHOD_SESSION_KEY);

		// If no order ID in session, generate a random one for demo purposes
		if (orderId == null) {
			orderId = 10000 + (int) (Math.random() * 90000);
		}

		// Always set payment status to "Completed"
		String paymentStatus = PAYMENT_STATUS_COMPLETED;

		// Set attributes for JSP
		request.setAttribute(ORDER_ID_ATTR, orderId);
		request.setAttribute(ORDER_TOTAL_ATTR, orderTotal);
		request.setAttribute(ORDER_DATE_ATTR, orderDate);
		request.setAttribute(SUCCESS_MESSAGE_ATTR, successMessage);
		request.setAttribute(PAYMENT_METHOD_ATTR, paymentMethod);
		request.setAttribute(PAYMENT_STATUS_ATTR, paymentStatus);

		try {
			// Forward to confirmation JSP
			request.getRequestDispatcher(CONFIRMATION_PAGE_PATH).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Clean up session attributes
		session.removeAttribute(ORDER_ID_SESSION_KEY);
		session.removeAttribute(ORDER_TOTAL_SESSION_KEY);
		session.removeAttribute(ORDER_DATE_SESSION_KEY);
		session.removeAttribute(FLASH_SUCCESS_SESSION_KEY);
		session.removeAttribute(PAYMENT_METHOD_SESSION_KEY);
	}
}