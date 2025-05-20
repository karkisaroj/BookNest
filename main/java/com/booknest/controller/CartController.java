package com.booknest.controller;

import com.booknest.model.CartItem;
import com.booknest.service.CartService;
import com.booknest.service.CartServiceException;
import com.booknest.service.CartServiceImpl;
import com.booknest.util.SessionUtil; // Make sure this is imported

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;

@WebServlet("/cart")
public class CartController extends HttpServlet {
	private static final long serialVersionUID = 3L;
	private CartService cartService;
	private static final String USER_ID_SESSION_KEY = "userID"; // Ensure this matches LoginController

	@Override
	public void init() {
		this.cartService = new CartServiceImpl();
		System.out.println("CartController Initialized.");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// === DEBUG LOGGING [START] ===
		Integer userId = SessionUtil.getAttribute(req, USER_ID_SESSION_KEY, Integer.class);
		System.out.println("CartController [doGet]: Attempting to display cart for UserID = " + userId);
		// === DEBUG LOGGING [END] ===

		if (userId == null) {
			System.out.println("CartController [doGet]: User not logged in. Redirecting to login.");
			resp.sendRedirect(req.getContextPath() + "/login?message=Please+log+in+to+view+your+cart");
			return;
		}

		List<CartItem> cartItems = null;
		String errMsg = null;
		BigDecimal total = BigDecimal.ZERO;
		try {
			cartItems = cartService.getCartContents(userId);
			System.out.println("CartController [doGet]: cartService.getCartContents returned "
					+ (cartItems != null ? cartItems.size() : "null") + " items for UserID = " + userId);

			req.setAttribute("cartItems", cartItems);
			if (cartItems != null) {
				for (CartItem item : cartItems) {
					// Ensure item.getBookModel() and item.getBookModel().getPrice() are not null
					if (item != null && item.getBookModel() != null && item.getBookModel().getPrice() != null) {
						total = total
								.add(item.getBookModel().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
					} else {
						System.err.println(
								"CartController [doGet]: Found cart item or associated book/price is null for UserID="
										+ userId + ", CartItemID=" + (item != null ? item.getCartItemId() : "N/A"));
					}
				}
			}
			req.setAttribute("cartTotal", total);
		} catch (CartServiceException e) {
			errMsg = "Could not load cart: " + e.getMessage();
			System.err.println(
					"CartController [doGet]: Error loading cart for UserID = " + userId + ": " + e.getMessage());
			req.setAttribute("errorMessage", errMsg);
		} catch (Exception e) {
			errMsg = "Unexpected error loading cart.";
			System.err.println("CartController [doGet]: UNEXPECTED error loading cart for UserID = " + userId);
			e.printStackTrace();
			req.setAttribute("errorMessage", errMsg);
		}
		req.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String redirectUrl = null; // Initialize redirectUrl

		// --- Get userId and action *before* the try block ---
		Integer userId = SessionUtil.getAttribute(req, USER_ID_SESSION_KEY, Integer.class);
		String action = req.getParameter("action");
		System.out.println("CartController [doPost]: Received action '" + action + "' for UserID = " + userId);

		// --- Login Check ---
		if (userId == null) {
			// Use session for flash message before redirect
			session.setAttribute("flashErrorMessage", "Please log in to modify your cart.");
			resp.sendRedirect(req.getContextPath() + "/login");
			return; // Stop processing if not logged in
		}

		// --- Action Null/Empty Check ---
		if (action == null || action.trim().isEmpty()) {
			System.err.println("CartController [doPost]: Action parameter is missing or empty.");
			session.setAttribute("flashErrorMessage", "Invalid request: Missing action.");
			resp.sendRedirect(req.getContextPath() + "/home"); // Redirect home on bad action
			return; // Stop processing
		}

		String contextPath = req.getContextPath(); // Get context path once

		try {
			switch (action.toLowerCase()) {
			case "add":
				System.out.println("CartController [doPost/add]: Calling handleAddAction...");
				handleAddAction(req, userId, session); // Add item

				// --- Revised Redirect Logic for 'add' ---
				String sourceUrl = req.getParameter("sourceUrl");
				// Check if sourceUrl is valid
				if (sourceUrl != null && !sourceUrl.trim().isEmpty() && sourceUrl.startsWith(contextPath)
						&& !sourceUrl.contains("/WEB-INF/")) {
					redirectUrl = sourceUrl; // Use the valid sourceUrl
					System.out
							.println("CartController [doPost/add]: Using valid sourceUrl for redirect: " + redirectUrl);
				} else {
					redirectUrl = contextPath + "/home"; // Fallback to home
					System.out.println("CartController [doPost/add]: Invalid or missing sourceUrl ('" + sourceUrl
							+ "'). Falling back to redirect: " + redirectUrl);
				}
				break; // End case "add"

			case "update":
				System.out.println("CartController [doPost/update]: Calling handleUpdateAction...");
				handleUpdateAction(req, userId, session);
				redirectUrl = contextPath + "/cart"; // Redirect back to cart view
				break;

			case "remove":
				System.out.println("CartController [doPost/remove]: Calling handleRemoveAction...");
				handleRemoveAction(req, userId, session);
				redirectUrl = contextPath + "/cart"; // Redirect back to cart view
				break;

			default:
				System.err.println("CartController [doPost]: Unknown action '" + action + "'");
				session.setAttribute("flashErrorMessage", "Invalid cart operation.");
				redirectUrl = contextPath + "/home"; // Default redirect
			}
		} catch (CartServiceException e) {
			session.setAttribute("flashErrorMessage", "Error: " + e.getMessage());
			System.err.println("CartController [doPost]: CartServiceException during action '" + action
					+ "' for UserID = " + userId + ": " + e.getMessage());
			// --- Error Redirect Logic (Try sourceUrl, fallback home) ---
			redirectUrl = req.getParameter("sourceUrl");
			if (redirectUrl == null || redirectUrl.trim().isEmpty() || !redirectUrl.startsWith(contextPath)
					|| redirectUrl.contains("/WEB-INF/")) {
				redirectUrl = contextPath + "/home"; // Fallback on error
			}
			// --- End Error Redirect Logic ---
		} catch (NumberFormatException e) {
			session.setAttribute("flashErrorMessage", "Invalid input provided (e.g., non-numeric quantity).");
			System.err.println("CartController [doPost]: NumberFormatException during action '" + action
					+ "' for UserID = " + userId + ": " + e.getMessage());
			// --- Error Redirect Logic (Try sourceUrl, fallback home) ---
			redirectUrl = req.getParameter("sourceUrl");
			if (redirectUrl == null || redirectUrl.trim().isEmpty() || !redirectUrl.startsWith(contextPath)
					|| redirectUrl.contains("/WEB-INF/")) {
				redirectUrl = contextPath + "/home"; // Fallback on error
			}
			// --- End Error Redirect Logic ---
		} catch (Exception e) { // Catch any other unexpected runtime errors
			session.setAttribute("flashErrorMessage", "An unexpected server error occurred.");
			System.err.println("CartController [doPost]: UNEXPECTED Exception during action '" + action
					+ "' for UserID = " + userId);
			e.printStackTrace(); // Log the full stack trace
			redirectUrl = contextPath + "/home"; // Fallback to home on severe error
		}

		System.out.println("CartController [doPost]: Redirecting to: " + redirectUrl);
		resp.sendRedirect(redirectUrl);
	}

	// --- Action Handlers (handleAddAction, handleUpdateAction, handleRemoveAction)
	// ---
	// These methods remain the same as in your previous version. Add logging as
	// needed.

	private void handleAddAction(HttpServletRequest r, int userId, HttpSession s)
			throws CartServiceException, NumberFormatException {
		int bookId = Integer.parseInt(r.getParameter("bookId"));
		int quantity = 1; // Default quantity
		String qP = r.getParameter("quantity"); // Optional: allow adding specific quantity
		if (qP != null && !qP.trim().isEmpty()) {
			try {
				quantity = Integer.parseInt(qP);
				if (quantity < 1) {
					System.out.println("CartController [handleAddAction]: Provided quantity " + quantity
							+ " invalid, defaulting to 1.");
					quantity = 1; // Ensure quantity is at least 1
				}
			} catch (NumberFormatException e) {
				System.err.println(
						"CartController [handleAddAction]: Invalid quantity parameter '" + qP + "', defaulting to 1.");
				quantity = 1; // Default if parameter is not a valid number
			}
		}
		System.out.println("CartController [handleAddAction]: Calling cartService.addItemToCart for UserID = " + userId
				+ ", BookID = " + bookId + ", Quantity = " + quantity);
		cartService.addItemToCart(userId, bookId, quantity); // The actual call
		s.setAttribute("flashSuccessMessage", "Item added to cart!"); // Success message
		System.out.println("CartController [handleAddAction]: addItemToCart completed successfully.");
	}

	private void handleUpdateAction(HttpServletRequest r, int uId, HttpSession s)
			throws CartServiceException, NumberFormatException {
		int cId = Integer.parseInt(r.getParameter("cartItemId"));
		int nQty = Integer.parseInt(r.getParameter("newQuantity"));

		// If quantity is zero or less, treat it as a removal
		if (nQty <= 0) {
			System.out.println(
					"CartController [handleUpdateAction]: Quantity <= 0, calling handleRemoveAction for CartItemID = "
							+ cId);
			handleRemoveAction(r, uId, s); // Pass request object to get cartItemId again if needed by remove handler
			return; // Stop further processing in update action
		}

		System.out
				.println("CartController [handleUpdateAction]: Calling cartService.updateCartItemQuantity for UserID = "
						+ uId + ", CartItemID = " + cId + ", NewQuantity = " + nQty);
		boolean ok = cartService.updateCartItemQuantity(uId, cId, nQty);
		if (ok)
			s.setAttribute("flashSuccessMessage", "Cart quantity updated.");
		else
			s.setAttribute("flashErrorMessage", "Could not update cart quantity.");
	}

	private void handleRemoveAction(HttpServletRequest r, int uId, HttpSession s)
			throws CartServiceException, NumberFormatException {
		// Ensure cartItemId parameter is present
		String cartItemIdParam = r.getParameter("cartItemId");
		if (cartItemIdParam == null || cartItemIdParam.trim().isEmpty()) {
			throw new NumberFormatException("Missing cartItemId parameter for remove action.");
		}
		int cId = Integer.parseInt(cartItemIdParam);

		System.out.println("CartController [handleRemoveAction]: Calling cartService.removeItemFromCart for UserID = "
				+ uId + ", CartItemID = " + cId);
		boolean ok = cartService.removeItemFromCart(uId, cId);
		if (ok)
			s.setAttribute("flashSuccessMessage", "Item removed from cart.");
		else
			s.setAttribute("flashErrorMessage", "Could not remove item from cart.");
	}

} // End of CartController class