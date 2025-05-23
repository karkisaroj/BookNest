package com.booknest.controller;

import com.booknest.model.CartItem;
import com.booknest.service.CartService;
import com.booknest.service.CartServiceException;
import com.booknest.service.CartServiceImpl;
import com.booknest.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;

/**
 * @author Saroj Karki 23047612
 */

/**
 * CartController handles shopping cart operations including viewing, adding,
 * updating, and removing items from a user's cart.
 */
@WebServlet("/cart")
public class CartController extends HttpServlet {
	private static final long serialVersionUID = 3L;
	private CartService cartService;
	private static final String USER_ID_SESSION_KEY = "userID";
	
	// Path constants
	private final String loginPagePath = "/login";
	private final String cartPagePath = "/WEB-INF/pages/cart.jsp";
	private final String homePagePath = "/home";
	
	// Message constants
	private final String loginToViewCartMessage = "Please log in to view your cart";
	private final String loginToModifyCartMessage = "Please log in to modify your cart.";
	private final String missingActionMessage = "Invalid request: Missing action.";
	private final String invalidCartOperationMessage = "Invalid cart operation.";
	private final String invalidInputMessage = "Invalid input provided (e.g., non-numeric quantity).";
	private final String unexpectedErrorMessage = "An unexpected server error occurred.";
	private final String itemAddedMessage = "Item added to cart!";
	private final String quantityUpdatedMessage = "Cart quantity updated.";
	private final String quantityUpdateFailedMessage = "Could not update cart quantity.";
	private final String missingCartItemIdMessage = "Missing cartItemId parameter for remove action.";
	private final String itemRemovedMessage = "Item removed from cart.";
	private final String itemRemoveFailedMessage = "Could not remove item from cart.";
	private final String cartLoadErrorPrefix = "Could not load cart: ";
	private final String unexpectedCartLoadErrorMessage = "Unexpected error loading cart.";

	/**
	 * Initializes the cart service.
	 */
	@Override
	public void init() {
		this.cartService = new CartServiceImpl();
	}

	/**
	 * Handles GET requests to display the user's cart contents.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer userId = SessionUtil.getAttribute(req, USER_ID_SESSION_KEY, Integer.class);

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + loginPagePath + "?message=" + loginToViewCartMessage);
			return;
		}

		List<CartItem> cartItems = null;
		BigDecimal total = BigDecimal.ZERO;

		try {
			cartItems = cartService.getCartContents(userId);
			req.setAttribute("cartItems", cartItems);

			if (cartItems != null) {
				for (CartItem item : cartItems) {
					if (item != null && item.getBookModel() != null && item.getBookModel().getPrice() != null) {
						total = total
								.add(item.getBookModel().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
					}
				}
			}
			req.setAttribute("cartTotal", total);
		} catch (CartServiceException e) {
			req.setAttribute("errorMessage", cartLoadErrorPrefix + e.getMessage());
		} catch (Exception e) {
			req.setAttribute("errorMessage", unexpectedCartLoadErrorMessage);
		}

		req.getRequestDispatcher(cartPagePath).forward(req, resp);
	}

	/**
	 * Handles POST requests for cart operations (add, update, remove).
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String redirectUrl = null;

		Integer userId = SessionUtil.getAttribute(req, USER_ID_SESSION_KEY, Integer.class);
		String action = req.getParameter("action");

		// User authentication check
		if (userId == null) {
			session.setAttribute("flashErrorMessage", loginToModifyCartMessage);
			resp.sendRedirect(req.getContextPath() + loginPagePath);
			return;
		}

		// Validate action parameter
		if (action == null || action.trim().isEmpty()) {
			session.setAttribute("flashErrorMessage", missingActionMessage);
			resp.sendRedirect(req.getContextPath() + homePagePath);
			return;
		}

		String contextPath = req.getContextPath();

		try {
			switch (action.toLowerCase()) {
			case "add":
				handleAddAction(req, userId, session);

				// Determine redirect URL after adding item
				String sourceUrl = req.getParameter("sourceUrl");
				if (sourceUrl != null && !sourceUrl.trim().isEmpty() && sourceUrl.startsWith(contextPath)
						&& !sourceUrl.contains("/WEB-INF/")) {
					redirectUrl = sourceUrl;
				} else {
					redirectUrl = contextPath + homePagePath;
				}
				break;

			case "update":
				handleUpdateAction(req, userId, session);
				redirectUrl = contextPath + "/cart";
				break;

			case "remove":
				handleRemoveAction(req, userId, session);
				redirectUrl = contextPath + "/cart";
				break;

			default:
				session.setAttribute("flashErrorMessage", invalidCartOperationMessage);
				redirectUrl = contextPath + homePagePath;
			}
		} catch (CartServiceException e) {
			session.setAttribute("flashErrorMessage", "Error: " + e.getMessage());

			// Try to use sourceUrl as fallback for redirect
			redirectUrl = req.getParameter("sourceUrl");
			if (redirectUrl == null || redirectUrl.trim().isEmpty() || !redirectUrl.startsWith(contextPath)
					|| redirectUrl.contains("/WEB-INF/")) {
				redirectUrl = contextPath + homePagePath;
			}
		} catch (NumberFormatException e) {
			session.setAttribute("flashErrorMessage", invalidInputMessage);

			// Try to use sourceUrl as fallback for redirect
			redirectUrl = req.getParameter("sourceUrl");
			if (redirectUrl == null || redirectUrl.trim().isEmpty() || !redirectUrl.startsWith(contextPath)
					|| redirectUrl.contains("/WEB-INF/")) {
				redirectUrl = contextPath + homePagePath;
			}
		} catch (Exception e) {
			session.setAttribute("flashErrorMessage", unexpectedErrorMessage);
			redirectUrl = contextPath + homePagePath;
		}

		resp.sendRedirect(redirectUrl);
	}

	/**
	 * Handles adding an item to the cart.
	 *
	 * @param request The HTTP request
	 * @param userId  The ID of the current user
	 * @param session The HTTP session
	 * @throws CartServiceException  If there is an error adding the item to the
	 *                               cart
	 * @throws NumberFormatException If the provided parameters are not valid
	 *                               numbers
	 */
	private void handleAddAction(HttpServletRequest request, int userId, HttpSession session)
			throws CartServiceException, NumberFormatException {
		int bookId = Integer.parseInt(request.getParameter("bookId"));
		int quantity = 1; // Default quantity

		String quantityParam = request.getParameter("quantity");
		if (quantityParam != null && !quantityParam.trim().isEmpty()) {
			try {
				quantity = Integer.parseInt(quantityParam);
				if (quantity < 1) {
					quantity = 1; // Ensure quantity is at least 1
				}
			} catch (NumberFormatException e) {
				quantity = 1; // Default if parameter is not a valid number
			}
		}

		cartService.addItemToCart(userId, bookId, quantity);
		session.setAttribute("flashSuccessMessage", itemAddedMessage);
	}

	/**
	 * Handles updating the quantity of a cart item.
	 *
	 * @param request The HTTP request
	 * @param userId  The ID of the current user
	 * @param session The HTTP session
	 * @throws CartServiceException  If there is an error updating the cart
	 * @throws NumberFormatException If the provided parameters are not valid
	 *                               numbers
	 */
	private void handleUpdateAction(HttpServletRequest request, int userId, HttpSession session)
			throws CartServiceException, NumberFormatException {
		int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
		int newQuantity = Integer.parseInt(request.getParameter("newQuantity"));

		// If quantity is zero or less, treat it as a removal
		if (newQuantity <= 0) {
			handleRemoveAction(request, userId, session);
			return;
		}

		boolean success = cartService.updateCartItemQuantity(userId, cartItemId, newQuantity);
		if (success) {
			session.setAttribute("flashSuccessMessage", quantityUpdatedMessage);
		} else {
			session.setAttribute("flashErrorMessage", quantityUpdateFailedMessage);
		}
	}

	/**
	 * Handles removing an item from the cart.
	 *
	 * @param request The HTTP request
	 * @param userId  The ID of the current user
	 * @param session The HTTP session
	 * @throws CartServiceException  If there is an error removing the item
	 * @throws NumberFormatException If the cartItemId is missing or not a valid
	 *                               number
	 */
	private void handleRemoveAction(HttpServletRequest request, int userId, HttpSession session)
			throws CartServiceException, NumberFormatException {
		// Ensure cartItemId parameter is present
		String cartItemIdParam = request.getParameter("cartItemId");
		if (cartItemIdParam == null || cartItemIdParam.trim().isEmpty()) {
			throw new NumberFormatException(missingCartItemIdMessage);
		}

		int cartItemId = Integer.parseInt(cartItemIdParam);
		boolean success = cartService.removeItemFromCart(userId, cartItemId);

		if (success) {
			session.setAttribute("flashSuccessMessage", itemRemovedMessage);
		} else {
			session.setAttribute("flashErrorMessage", itemRemoveFailedMessage);
		}
	}
}