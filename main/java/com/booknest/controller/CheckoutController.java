package com.booknest.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.booknest.config.DbConfiguration;
import com.booknest.model.CartItem;
import com.booknest.service.CartService;
import com.booknest.service.CartServiceImpl;
import com.booknest.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_ID_SESSION_KEY = "userID";
	private static final String USER_NAME_SESSION_KEY = "userName";

	private CartService cartService;

	@Override
	public void init() throws ServletException {
		this.cartService = new CartServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = SessionUtil.getAttribute(request, USER_ID_SESSION_KEY, Integer.class);

		// Check if user is logged in
		if (userId == null) {
			session.setAttribute("flashErrorMessage", "Please log in to checkout.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		try {
			// Get cart items
			List<CartItem> cartItems = cartService.getCartContents(userId);

			// Check if cart is empty
			if (cartItems == null || cartItems.isEmpty()) {
				session.setAttribute("flashErrorMessage", "Your cart is empty. Please add items before checkout.");
				response.sendRedirect(request.getContextPath() + "/cart");
				return;
			}

			// Calculate totals
			BigDecimal subtotal = BigDecimal.ZERO;
			for (CartItem item : cartItems) {
				if (item != null && item.getBookModel() != null && item.getBookModel().getPrice() != null) {
					subtotal = subtotal
							.add(item.getBookModel().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
				}
			}

			// Calculate shipping, tax, etc.
			BigDecimal shipping = new BigDecimal("100.00");
			BigDecimal discount = BigDecimal.ZERO; // No discount by default
			BigDecimal total = subtotal.add(shipping).subtract(discount);

			// Get user info from session
			String userName = (String) session.getAttribute(USER_NAME_SESSION_KEY);

			// Set attributes for JSP
			request.setAttribute("cartItems", cartItems);
			request.setAttribute("subtotal", subtotal);
			request.setAttribute("shipping", shipping);
			request.setAttribute("discount", discount);
			request.setAttribute("total", total);
			request.setAttribute("userName", userName);

			// Forward to checkout page
			request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);

		} catch (Exception e) {
			session.setAttribute("flashErrorMessage", "Error processing checkout: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/cart");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = SessionUtil.getAttribute(request, USER_ID_SESSION_KEY, Integer.class);

		if (userId == null) {
			session.setAttribute("flashErrorMessage", "Please log in to complete checkout.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		Connection conn = null;
		try {
			// Get form data
			String firstName = request.getParameter("firstName");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String country = request.getParameter("country");
			String deliveryType = request.getParameter("deliveryType");
			String paymentMethod = "Credit Card"; // Default payment method

			// Validate required fields
			if (firstName == null || firstName.trim().isEmpty() || phone == null || phone.trim().isEmpty()
					|| country == null || country.trim().isEmpty()) {

				request.setAttribute("formError", "Please fill all required fields.");
				doGet(request, response); // Re-display the checkout form
				return;
			}

			// Check terms agreement
			String termsAgreed = request.getParameter("terms");
			if (termsAgreed == null || !termsAgreed.equals("on")) {
				request.setAttribute("formError", "You must agree to the Terms and Conditions.");
				doGet(request, response); // Re-display the checkout form
				return;
			}

			// Get cart items for order record
			List<CartItem> cartItems = cartService.getCartContents(userId);
			if (cartItems == null || cartItems.isEmpty()) {
				request.setAttribute("formError", "Your cart is empty. Please add items before checkout.");
				doGet(request, response);
				return;
			}

			// Calculate order totals
			BigDecimal subtotal = BigDecimal.ZERO;
			for (CartItem item : cartItems) {
				if (item != null && item.getBookModel() != null && item.getBookModel().getPrice() != null) {
					subtotal = subtotal
							.add(item.getBookModel().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
				}
			}

			// Calculate shipping, discount, and total
			BigDecimal shipping = new BigDecimal("100.00");
			BigDecimal discount = BigDecimal.ZERO;
			BigDecimal total = subtotal.add(shipping).subtract(discount);

			// Prepare shipping address
			String shippingAddress = firstName;
			if (country != null && !country.trim().isEmpty()) {
				shippingAddress += ", " + country;
			}
			if (phone != null && !phone.trim().isEmpty()) {
				shippingAddress += ", Phone: " + phone;
			}

			// Begin transaction
			conn = DbConfiguration.getDbConnection();
			conn.setAutoCommit(false);

			// 1. Create order record
			int orderId = createOrder(conn, userId, shippingAddress, total);

			// 2. Add books to order_book table
			addBooksToOrder(conn, orderId, cartItems);

			// 3. Create payment record
			createPayment(conn, orderId, total, paymentMethod);

			// 4. Clear the user's cart
			clearCart(conn, userId);

			// Commit transaction
			conn.commit();

			// Create order date for display
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate = sdf.format(new Date());

			// Set success message and order details for confirmation page
			session.setAttribute("flashSuccessMessage", "Order placed successfully! Thank you for your purchase.");
			session.setAttribute("orderId", orderId);
			session.setAttribute("orderTotal", total);
			session.setAttribute("orderDate", currentDate);

			// Redirect to confirmation page
			response.sendRedirect(request.getContextPath() + "/order-confirmation");

		} catch (Exception e) {
			// Roll back transaction if an error occurred
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					// Log silently
				}
			}

			request.setAttribute("formError", "Error processing payment: " + e.getMessage());
			doGet(request, response); // Re-display the checkout form
		} finally {
			// Reset auto-commit to true and close connection
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					// Log silently
				}
			}
		}
	}

	/**
	 * Creates a new order record in the database
	 */
	private int createOrder(Connection conn, int userId, String shippingAddress, BigDecimal totalAmount)
			throws SQLException {
		int orderId = -1;
		String sql = "INSERT INTO orders (userID, order_date, shipping_address, total_amount, order_status) "
				+ "VALUES (?, NOW(), ?, ?, 'in progress')";

		try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, userId);
			pstmt.setString(2, shippingAddress);
			pstmt.setBigDecimal(3, totalAmount);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating order failed, no rows affected.");
			}

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					orderId = generatedKeys.getInt(1);
				} else {
					throw new SQLException("Creating order failed, no ID obtained.");
				}
			}
		}

		return orderId;
	}

	/**
	 * Adds books from cart to the order_book table
	 */
	private void addBooksToOrder(Connection conn, int orderId, List<CartItem> cartItems) throws SQLException {
		String sql = "INSERT INTO order_book (orderID, bookID) VALUES (?, ?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (CartItem item : cartItems) {
				if (item != null && item.getBookModel() != null) {
					pstmt.setInt(1, orderId);
					pstmt.setInt(2, item.getBookModel().getBookID());
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
		}
	}

	/**
	 * Creates a payment record in the database
	 */
	private void createPayment(Connection conn, int orderId, BigDecimal amount, String paymentMethod)
			throws SQLException {
		String sql = "INSERT INTO payment (orderID, payment_date, payment_amount, payment_method, payment_status) "
				+ "VALUES (?, NOW(), ?, ?, 'Completed')";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);
			pstmt.setBigDecimal(2, amount);
			pstmt.setString(3, paymentMethod);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating payment record failed, no rows affected.");
			}
		}
	}

	/**
	 * Clears the user's cart after successful order
	 */
	private void clearCart(Connection conn, int userId) throws SQLException {
		String sql = "DELETE FROM cart_item WHERE userID = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		}
	}
}