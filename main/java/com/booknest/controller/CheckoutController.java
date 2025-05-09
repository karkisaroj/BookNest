package com.booknest.controller;

import com.booknest.model.CartItem;
import com.booknest.service.CartService;
import com.booknest.service.CartServiceException;
import com.booknest.service.CartServiceImpl;
import com.booknest.service.OrderService;
import com.booknest.service.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final OrderService orderService;
	private final CartService cartService;

	public CheckoutController() {
		super();
		this.orderService = new OrderServiceImpl();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("CartItems size: " + cartItems.size());

		if (cartItems == null || cartItems.isEmpty()) {
			request.setAttribute("errorMessage", "Your cart is empty. Please add items before checkout.");
			request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
			return;
		}

		// Calculate totals
		BigDecimal cartTotal = BigDecimal.ZERO;
		for (CartItem item : cartItems) {
			BigDecimal itemPrice = item.getBookModel().getPrice();
			BigDecimal itemTotal = itemPrice.multiply(new BigDecimal(item.getQuantity()));
			cartTotal = cartTotal.add(itemTotal);
		}

		// Set default shipping cost
		BigDecimal shippingCost = new BigDecimal("100.00");

		// Set attributes for the checkout page
		request.setAttribute("cartItems", cartItems);
		request.setAttribute("cartTotal", cartTotal);
		request.setAttribute("shippingCost", shippingCost);

		// Forward to the checkout page
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
			System.out.println("Cart items found for checkout: " + cartItems.size());

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

			System.out.println(
					"Address parameters received: " + streetAddress + ", " + city + ", " + state + " " + zipCode);

			// Validation
			if (streetAddress == null || city == null || state == null || zipCode == null || phone == null
					|| streetAddress.trim().isEmpty() || city.trim().isEmpty() || state.trim().isEmpty()
					|| zipCode.trim().isEmpty() || phone.trim().isEmpty()) {

				request.setAttribute("errorMessage", "Please fill in all required address fields.");
				doGet(request, response);
				return;
			}

			// Format address in standard format: Street, City, State/Province Postal Code
			OrderServiceImpl orderServiceImpl = (OrderServiceImpl) orderService;
			String formattedAddress = orderServiceImpl.createStandardAddress(streetAddress, city, state, zipCode);

			System.out.println("Formatted address: " + formattedAddress);

			// Calculate totals
			BigDecimal subtotal = BigDecimal.ZERO;
			for (CartItem item : cartItems) {
				BigDecimal itemPrice = item.getBookModel().getPrice();
				BigDecimal itemTotal = itemPrice.multiply(new BigDecimal(item.getQuantity()));
				subtotal = subtotal.add(itemTotal);
			}

			// Set shipping and other costs
			BigDecimal shippingCost = new BigDecimal("100.00");
			BigDecimal discount = BigDecimal.ZERO;
			BigDecimal totalAmount = subtotal.add(shippingCost).subtract(discount);

			System.out.println("Creating order with amount: " + totalAmount);

			// Create the order
			int orderId = orderService.createOrder(userId, cartItems, formattedAddress, // Use formatted address without
																						// phone number
					subtotal, shippingCost, discount, totalAmount);

			// If order creation was successful
			if (orderId > 0) {
				// Set success attributes for order confirmation
				request.setAttribute("orderId", orderId);
				request.setAttribute("orderTotal", totalAmount);
				request.setAttribute("successMessage", "Order placed successfully! Thank you for your purchase.");

				System.out.println("Order created successfully with ID: " + orderId);

				// Forward to order confirmation page
				request.getRequestDispatcher("/WEB-INF/pages/order-confirmation.jsp").forward(request, response);
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
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("General Error: " + e.getMessage());
			request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
			doGet(request, response);
		}
	}
}