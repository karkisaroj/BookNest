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

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final CheckoutService checkoutService;
	private final CartService cartService;

	public CheckoutController() {
		super();
		this.checkoutService = new CheckoutServiceImpl();
		this.cartService = new CartServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("CheckoutController - doGet method called");

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userID");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// Get cart items for the user
		List<CartItem> cartItems = null;
		try {
			cartItems = cartService.getCartContents(userId);
		} catch (CartServiceException e) {
			e.printStackTrace();
		}
		System.out.println("CartItems size: " + (cartItems != null ? cartItems.size() : 0));

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

		// Debug logs to verify values
		System.out.println("Sending to checkout page - Subtotal: " + subtotal);
		System.out.println("Sending to checkout page - Shipping: " + shippingCost);
		System.out.println("Sending to checkout page - Total: " + total);

		request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("CheckoutController - doPost method called");

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userID");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		try {
			// Get cart contents
			List<CartItem> cartItems = cartService.getCartContents(userId);
			System.out.println("Cart items found for checkout: " + (cartItems != null ? cartItems.size() : 0));

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

			System.out.println("Payment method selected: " + paymentMethod);
			System.out.println(
					"Address parameters received: " + streetAddress + ", " + city + ", " + state + " " + zipCode);

			// Validate using service
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

				// Create payment record
				int paymentId = checkoutService.createPaymentRecord(orderId, orderTotal, paymentMethod);
				System.out.println("Payment record created with ID: " + paymentId + " for order ID: " + orderId);

				// Format current date for order display
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDate = sdf.format(new Date());

				// Set success attributes for order confirmation
				session.setAttribute("orderId", orderId);
				session.setAttribute("orderTotal", orderTotal);
				session.setAttribute("orderDate", currentDate);
				session.setAttribute("paymentMethod", paymentMethod); // Store payment method for confirmation page
				session.setAttribute("flashSuccessMessage", "Order placed successfully! Thank you for your purchase.");

				System.out.println("Order created successfully with ID: " + orderId);

				// Redirect to order confirmation page
				response.sendRedirect(request.getContextPath() + "/order-confirmation");
			} else {
				// Handle order creation failure
				request.setAttribute("errorMessage", "Failed to create your order. Please try again.");
				doGet(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("SQL Error: " + e.getMessage());
			request.setAttribute("errorMessage", "Error processing your order: " + e.getMessage());
			doGet(request, response);
		} catch (CartServiceException e) {
			e.printStackTrace();
			System.err.println("Cart Service Error: " + e.getMessage());
			request.setAttribute("errorMessage", "Error retrieving your cart: " + e.getMessage());
			doGet(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("General Error: " + e.getMessage());
			request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
			doGet(request, response);
		}
	}
}