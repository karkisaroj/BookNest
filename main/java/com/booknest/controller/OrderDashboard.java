package com.booknest.controller;

import com.booknest.model.OrderModel;
import com.booknest.service.AdminOrderService;
import com.booknest.util.RedirectionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class OrderDashboard
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/adminorder" })
public class OrderDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AdminOrderService orderService;
	private RedirectionUtil redirectionUtil;

	// File paths
	private final String orderPagePath = "/WEB-INF/pages/orderDashboard.jsp";

	// Status constants
	private final String orderStatusCompleted = "completed";
	private final String orderStatusInProgress = "in progress";

	// Message constants
	private final String successMessage = "Order status updated successfully!";
	private final String errorOrderNotFoundMessage = "Order not found.";
	private final String errorInvalidOrderIdMessage = "Invalid order ID.";
	private final String errorOrderIdMissingMessage = "Order ID not provided.";

	@Override
	public void init() throws ServletException {
		orderService = new AdminOrderService();
		this.redirectionUtil = new RedirectionUtil();
	}

	/**
	 * Handles GET requests for the order management page.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<OrderModel> orders = orderService.getAllOrders();

		// Set the orders list as an attribute to be used in the JSP
		request.setAttribute("orders", orders);

		// Use RedirectionUtil to forward to the orderDashboard.jsp page
		redirectionUtil.redirectToPage(request, response, orderPagePath);
	}

	/**
	 * Handles POST requests for updating the status of an order.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderIdParam = request.getParameter("orderId");

		if (orderIdParam != null) {
			try {
				int orderId = Integer.parseInt(orderIdParam);

				// Fetch the order and toggle its status
				OrderModel order = orderService.getOrderById(orderId);
				if (order != null) {
					String newStatus = orderStatusCompleted.equalsIgnoreCase(order.getOrderStatus())
							? orderStatusInProgress
							: orderStatusCompleted;
					orderService.updateOrderStatus(orderId, newStatus);

					// Fetch the updated order list
					List<OrderModel> orders = orderService.getAllOrders();
					request.setAttribute("orders", orders);

					
					redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "success", successMessage);
				} else {
					
					redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "error",
							errorOrderNotFoundMessage);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				
				redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "error",
						errorInvalidOrderIdMessage);
			}
		} else {
			
			redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "error", errorOrderIdMissingMessage);
		}
	}
}