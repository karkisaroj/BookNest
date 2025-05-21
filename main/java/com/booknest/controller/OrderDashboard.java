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
 * Controller for handling order management in the admin dashboard.
 * Provides functionality to view all orders and toggle order status between
 * 'completed' and 'in progress'.
 * 
 * @author 23047591 Noble-Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/adminorder" })
public class OrderDashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Services and utilities
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

    /**
     * Initializes the servlet by creating service and utility instances.
     * This method is called once when the servlet is first loaded.
     * 
     * @throws ServletException if a servlet-specific error occurs during initialization
     */
    @Override
    public void init() throws ServletException {
        orderService = new AdminOrderService();
        this.redirectionUtil = new RedirectionUtil();
    }

    /**
     * Handles GET requests for the order management page.
     * Retrieves all orders from the database and forwards them to the JSP for display.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch all orders from the database
        List<OrderModel> orders = orderService.getAllOrders();

        // Set the orders list as an attribute to be used in the JSP
        request.setAttribute("orders", orders);

        // Forward to the order dashboard JSP page
        redirectionUtil.redirectToPage(request, response, orderPagePath);
    }

    /**
     * Handles POST requests for updating the status of an order.
     * Toggles the order status between 'completed' and 'in progress'.
     * 
     * @param request  The HttpServletRequest object containing form data
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");

        // Validate that order ID is provided
        if (orderIdParam != null) {
            try {
                // Parse and validate the order ID
                int orderId = Integer.parseInt(orderIdParam);

                // Fetch the order and toggle its status
                OrderModel order = orderService.getOrderById(orderId);
                if (order != null) {
                    // Toggle the status between 'completed' and 'in progress'
                    String newStatus = orderStatusCompleted.equalsIgnoreCase(order.getOrderStatus())
                            ? orderStatusInProgress
                            : orderStatusCompleted;
                    
                    // Update the order status in the database
                    orderService.updateOrderStatus(orderId, newStatus);

                    // Fetch the updated order list to display the latest data
                    List<OrderModel> orders = orderService.getAllOrders();
                    request.setAttribute("orders", orders);

                    // Redirect with success message
                    redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "success", successMessage);
                } else {
                    // Handle case where order is not found
                    redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "error",
                            errorOrderNotFoundMessage);
                }
            } catch (NumberFormatException e) {
                // Handle case where order ID is not a valid number
                redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "error",
                        errorInvalidOrderIdMessage);
            }
        } else {
            // Handle case where order ID is not provided
            redirectionUtil.setMsgAndRedirect(request, response, orderPagePath, "error", errorOrderIdMissingMessage);
        }
    }
}