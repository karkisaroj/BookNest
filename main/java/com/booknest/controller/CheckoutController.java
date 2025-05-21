package com.booknest.controller;

import com.booknest.model.CartItem;
import com.booknest.service.CartService;
import com.booknest.service.CartServiceException;
import com.booknest.service.CartServiceImpl;
import com.booknest.service.CheckoutService;
import com.booknest.service.CheckoutServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Saroj Karki 23047612
 */

/**
 * CheckoutController handles the checkout process for orders. Manages
 * displaying the checkout page and processing order submissions.
 */
@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final CheckoutService checkoutService;
	private final CartService cartService;

	/**
	 * Constructor initializes required services.
	 */
	public CheckoutController() {
		super();
		this.checkoutService = new CheckoutServiceImpl();
		this.cartService = new CartServiceImpl();
	}

	/**
	 * Handles GET requests to display the checkout page. Retrieves the user's cart
	 * items, calculates totals, and forwards to the checkout JSP page.
	 * 
	 * @param request  The HttpServletRequest object containing client request
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during response handling
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userID");

		// Redirect to login if user is not logged in
		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// Get cart items for the user
		List<CartItem> cartItems = null;
		try {
			cartItems = cartService.getCartContents(userId);
		} catch (CartServiceException e) {
			request.setAttribute("errorMessage", "Error retrieving cart: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
			return;
		}

		// Verify cart has items
		if (cartItems == null || cartItems.isEmpty()) {
			request.setAttribute("errorMessage", "Your cart is empty. Please add items before checkout.");
			request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
			return;
		}

		// Calculate totals using service
		BigDecimal subtotal = checkoutService.calculateSubtotal(cartItems);
		BigDecimal shippingCost = checkoutService.getShippingCost();
		BigDecimal total = subtotal.add(shippingCost);

		// Set attributes for the checkout page with proper precision
		request.setAttribute("cartItems", cartItems);
		request.setAttribute("cartTotal", subtotal);
		request.setAttribute("shippingCost", shippingCost);
		request.setAttribute("orderTotal", total);

		request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests to process order submissions. Validates user
	 * authentication, cart contents, and address information before processing the
	 * checkout. Creates an order record and redirects to order confirmation on
	 * success.
	 * 
	 * @param request  The HttpServletRequest object containing order details and
	 *                 address information
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during response handling
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userID");

		// Redirect to login if user is not logged in
		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		try {
			// Get cart contents
			List<CartItem> cartItems = cartService.getCartContents(userId);

			if (cartItems == null || cartItems.isEmpty()) {
				request.setAttribute("errorMessage", "Your cart is empty. Please add items before checkout.");
				request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
				return;
			}

			// Get address components from form
			String streetAddress = request.getParameter("streetAddress");
			String city = request.getParameter("city");
			String state = request.getParameter("state");
			String zipCode = request.getParameter("zipCode");
			String phone = request.getParameter("phone");

			// Get payment method parameter
			String paymentMethodParam = request.getParameter("paymentMethod");
			String paymentMethod = checkoutService.formatPaymentMethod(paymentMethodParam);

			// Validate address information
			if (!checkoutService.validateAddressInfo(streetAddress, city, state, zipCode, phone)) {
				request.setAttribute("errorMessage", "Please fill in all required address fields.");
				doGet(request, response);
				return;
			}

			// Process checkout using service
			int orderId = checkoutService.processCheckout(userId, cartItems, streetAddress, city, state, zipCode,
					phone);

			// If order creation was successful, create payment record
			if (orderId > 0) {
				// Calculate totals for display and payment
				BigDecimal orderTotal = checkoutService.calculateOrderTotal(cartItems);

				// Format current date for order display
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDate = sdf.format(new Date());

				// Set success attributes for order confirmation
				session.setAttribute("orderId", orderId);
				session.setAttribute("orderTotal", orderTotal);
				session.setAttribute("orderDate", currentDate);
				session.setAttribute("paymentMethod", paymentMethod);
				session.setAttribute("flashSuccessMessage", "Order placed successfully! Thank you for your purchase.");

				// Redirect to order confirmation page
				response.sendRedirect(request.getContextPath() + "/order-confirmation");
			} else {
				// Handle order creation failure
				request.setAttribute("errorMessage", "Failed to create your order. Please try again.");
				doGet(request, response);
			}

		} catch (SQLException e) {
			request.setAttribute("errorMessage", "Error processing your order: " + e.getMessage());
			doGet(request, response);
		} catch (CartServiceException e) {
			request.setAttribute("errorMessage", "Error retrieving your cart: " + e.getMessage());
			doGet(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
			doGet(request, response);
		}
	}
}