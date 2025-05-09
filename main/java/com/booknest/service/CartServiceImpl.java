package com.booknest.service;

import java.sql.*;
import java.util.*;
import com.booknest.config.DbConfiguration;
import com.booknest.model.BookCartModel;
import com.booknest.model.CartItem;

public class CartServiceImpl implements CartService {

	private final BookService bookService;

	public CartServiceImpl() {
		// Consider dependency injection later, but this works for now
		this.bookService = new BookServiceImpl();
	}

	// Constructor for potential dependency injection later
	public CartServiceImpl(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public CartItem addItemToCart(int uId, int bId, int q) throws CartServiceException {
		try {
			BookCartModel book;
			try {
				// Ensure getBookById fetches necessary details if not already done
				book = bookService.getBookById(bId);
				if (book == null) {
					throw new CartServiceException("Book with ID " + bId + " not found.");
				}
			} catch (Exception e) {
				throw new CartServiceException("Cannot add item: " + e.getMessage(), e);
			}
			Optional<CartItem> existing = findCartItemByUserIdAndBookId(uId, bId);
			CartItem result;
			if (existing.isPresent()) {
				int nq = existing.get().getQuantity() + q;
				boolean upd = updateCartItemQuantityInternal(existing.get().getCartItemId(), nq);
				if (!upd)
					throw new CartServiceException("Failed to update quantity for existing cart item.");
				existing.get().setQuantity(nq);
				result = existing.get();
			} else {
				result = insertCartItem(uId, bId);
				if (result == null)
					throw new CartServiceException("Failed to insert new cart item.");
				if (q > 1) { // Only update quantity if it's more than the default 1 added during insert
					boolean upd = updateCartItemQuantityInternal(result.getCartItemId(), q);
					if (!upd)
						throw new CartServiceException("Failed to update quantity after inserting new cart item.");
					result.setQuantity(q);
				}
			}
			result.setBookModel(book);
			return result;
		} catch (SQLException e) {
			System.err.println("SQL Error in addItemToCart: " + e.getMessage());
			e.printStackTrace(); // Consider more robust logging
			throw new CartServiceException("Database error while adding item to cart.", e);
		} catch (CartServiceException e) {
			// Re-throw CartServiceExceptions directly
			throw e;
		} catch (Exception e) {
			e.printStackTrace(); // Consider more robust logging
			throw new CartServiceException("An unexpected error occurred while adding item to cart.", e);
		}
	}

	@Override
	public boolean updateCartItemQuantity(int uId, int cId, int nq) throws CartServiceException {
		if (nq <= 0) {
			// Instead of throwing error, handle removal if quantity is 0 or less
			System.out.println("Quantity is zero or less, removing item: " + cId);
			return this.removeItemFromCart(uId, cId);
			// throw new CartServiceException("Quantity must be positive.");
		}
		try {
			// Optional: Add a check to ensure the cart item belongs to the user 'uId'
			// before updating
			return updateCartItemQuantityInternal(cId, nq);
		} catch (SQLException e) {
			System.err.println("SQL Error in updateCartItemQuantity: " + e.getMessage());
			e.printStackTrace();
			throw new CartServiceException("Database error while updating cart item quantity.", e);
		}
	}

	@Override
	public boolean removeItemFromCart(int uId, int cId) throws CartServiceException {
		try {
			// Optional: Add a check to ensure the cart item 'cId' belongs to the user 'uId'
			// before deleting
			return deleteCartItemInternal(cId);
		} catch (SQLException e) {
			System.err.println("SQL Error in removeItemFromCart: " + e.getMessage());
			e.printStackTrace();
			throw new CartServiceException("Database error while removing item from cart.", e);
		}
	}

	// --- NEW IMPLEMENTATION for getCartContents ---
	@Override
	public List<CartItem> getCartContents(int userId) throws CartServiceException {
		List<CartItem> items = new ArrayList<>();

		String sql = "SELECT ci.cart_itemID, ci.userID, ci.quantity, ci.updated_at, "
				+ "b.bookID, b.book_title, b.price, b.book_img_url, "
				+ "GROUP_CONCAT(a.author_name SEPARATOR ', ') AS authors " + // Aggregate author names
				"FROM cart_item ci " + "JOIN book b ON ci.bookID = b.bookID "
				+ "LEFT JOIN book_author ba ON b.bookID = ba.bookID " + // LEFT JOIN in case a book has no author linked
				"LEFT JOIN author a ON ba.authorID = a.authorID " + // LEFT JOIN for the same reason
				"WHERE ci.userID = ? " + "GROUP BY ci.cart_itemID, b.bookID " + "ORDER BY ci.updated_at DESC";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbConfiguration.getDbConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				CartItem cartItem = new CartItem();
				cartItem.setCartItemId(rs.getInt("cart_itemID"));
				cartItem.setUserId(rs.getInt("userID"));
				cartItem.setQuantity(rs.getInt("quantity"));
				cartItem.setUpdatedAt(rs.getTimestamp("updated_at"));
				cartItem.setBookId(rs.getInt("bookID")); // Set bookId on CartItem as well

				BookCartModel book = new BookCartModel();
				book.setBookID(rs.getInt("bookID"));
				book.setBook_title(rs.getString("book_title")); // Ensure BookModel has setBook_title
				book.setPrice(rs.getBigDecimal("price")); // Ensure BookModel has setPrice
				book.setBook_img_url(rs.getString("book_img_url")); // Ensure BookModel has setBook_img_url
				book.setAuthorName(rs.getString("authors")); // Set the aggregated author names

				cartItem.setBookModel(book); // Associate the populated BookModel with the CartItem
				items.add(cartItem);
			}
			System.out.println(
					"CartServiceImpl [getCartContents]: Found " + items.size() + " items for UserID = " + userId); // Debug
																													// log
		} catch (SQLException e) {
			System.err.println("SQL Error in getCartContents for UserID = " + userId + ": " + e.getMessage()); // Log
																												// error
			e.printStackTrace(); // Print stack trace
			throw new CartServiceException("Database error retrieving cart contents.", e);
		} catch (ClassNotFoundException e) {
			System.err.println("Database Driver Error in getCartContents: " + e.getMessage()); // Log error
			throw new CartServiceException("Database driver configuration error.", e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}
		return items;
	}

	// --- Helper Methods (findCartItemByUserIdAndBookId, insertCartItem, etc.) ---
	private Optional<CartItem> findCartItemByUserIdAndBookId(int userId, int bookId) throws SQLException {
		String sql = "SELECT cart_itemID, userID, bookID, quantity, updated_at FROM cart_item WHERE userID = ? AND bookID = ?";
		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setInt(2, bookId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return Optional.of(mapRowToCartItemBasic(rs));
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException("DB Driver error", e);
		}
		return Optional.empty();
	}

	private CartItem insertCartItem(int userId, int bookId) throws SQLException {
		String sql = "INSERT INTO cart_item (userID, bookID, quantity) VALUES (?, ?, 1)";
		CartItem newItem = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet keys = null;
		try {
			conn = DbConfiguration.getDbConnection();
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setInt(2, bookId);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				keys = ps.getGeneratedKeys();
				if (keys.next()) {
					newItem = new CartItem();
					newItem.setCartItemId(keys.getInt(1));
					newItem.setUserId(userId);
					newItem.setBookId(bookId);
					newItem.setQuantity(1);
				} else
					throw new SQLException("Insert failed, no ID obtained.");
			} else
				throw new SQLException("Insert failed, no rows affected.");
		} catch (ClassNotFoundException e) {
			throw new SQLException("DB Driver error", e);
		} finally {
			close(keys);
			close(ps);
			close(conn);
		}
		return newItem;
	}

	private boolean updateCartItemQuantityInternal(int cartItemId, int newQuantity) throws SQLException {
		String sql = "UPDATE cart_item SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE cart_itemID = ?";
		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, newQuantity);
			ps.setInt(2, cartItemId);
			return ps.executeUpdate() > 0;
		} catch (ClassNotFoundException e) {
			throw new SQLException("DB Driver error", e);
		}
	}

	private boolean deleteCartItemInternal(int cartItemId) throws SQLException {
		String sql = "DELETE FROM cart_item WHERE cart_itemID = ?";
		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, cartItemId);
			return ps.executeUpdate() > 0;
		} catch (ClassNotFoundException e) {
			throw new SQLException("DB Driver error", e);
		}
	}

	private CartItem mapRowToCartItemBasic(ResultSet rs) throws SQLException {
		CartItem item = new CartItem();
		item.setCartItemId(rs.getInt("cart_itemID"));
		item.setUserId(rs.getInt("userID"));
		item.setBookId(rs.getInt("bookID"));
		item.setQuantity(rs.getInt("quantity"));
		item.setUpdatedAt(rs.getTimestamp("updated_at"));
		return item;
	}

	private void close(AutoCloseable resource) {
		if (resource != null)
			try {
				resource.close();
			} catch (Exception e) {
				// Consider logging this exception
				System.err.println("Error closing resource: " + e.getMessage());
				// e.printStackTrace();
			}
	}

}