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
		BigDecimal cartTotal = subtotal;

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

			// Calculate totals for display
			BigDecimal orderTotal = checkoutService.calculateOrderTotal(cartItems);

			// If order creation was successful
			if (orderId > 0) {
				// Set success attributes for order confirmation
				request.setAttribute("orderId", orderId);
				request.setAttribute("orderTotal", orderTotal);
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