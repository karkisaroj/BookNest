package com.booknest.service;

import java.sql.*;
import java.util.*;
import com.booknest.config.DbConfiguration;
import com.booknest.model.BookCartModel;
import com.booknest.model.CartItem;

/**
 * Implementation of CartService interface that handles shopping cart operations
 * with the database.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class CartServiceImpl implements CartService {

	private final BookService bookService;

	// Error message constants
	private static final String ERROR_BOOK_NOT_FOUND = "Book with ID %d not found.";
	private static final String ERROR_CANNOT_ADD_ITEM = "Cannot add item: %s";
	private static final String ERROR_UPDATE_EXISTING_QUANTITY = "Failed to update quantity for existing cart item.";
	private static final String ERROR_INSERT_CART_ITEM = "Failed to insert new cart item.";
	private static final String ERROR_UPDATE_NEW_QUANTITY = "Failed to update quantity after inserting new cart item.";
	private static final String ERROR_DB_ADDING_ITEM = "Database error while adding item to cart.";
	private static final String ERROR_UNEXPECTED_ADDING_ITEM = "An unexpected error occurred while adding item to cart.";
	private static final String ERROR_DB_UPDATING_QUANTITY = "Database error while updating cart item quantity.";
	private static final String ERROR_DB_REMOVING_ITEM = "Database error while removing item from cart.";
	private static final String ERROR_DB_RETRIEVING_CONTENTS = "Database error retrieving cart contents.";
	private static final String ERROR_DB_DRIVER_CONFIG = "Database driver configuration error.";
	private static final String ERROR_CLEAR_CART_NULL_USER = "Cannot clear cart: User ID is null";
	private static final String ERROR_DB_CLEARING_CART = "Database error while clearing cart.";
	private static final String ERROR_DB_DRIVER = "DB Driver error";
	private static final String ERROR_INSERT_NO_ID = "Insert failed, no ID obtained.";
	private static final String ERROR_INSERT_NO_ROWS = "Insert failed, no rows affected.";

	/**
	 * Default constructor that initializes with a new BookServiceImpl.
	 */
	public CartServiceImpl() {
		this.bookService = new BookServiceImpl();
	}

	/**
	 * Constructor for dependency injection.
	 * 
	 * @param bookService The BookService implementation to use
	 */
	public CartServiceImpl(BookService bookService) {
		this.bookService = bookService;
	}

	/**
	 * Adds an item to a user's cart.
	 *
	 * @param uId User ID
	 * @param bId Book ID to add to cart
	 * @param q   Quantity to add
	 * @return The CartItem that was added or updated
	 * @throws CartServiceException If there's an error adding the item
	 */
	@Override
	public CartItem addItemToCart(int uId, int bId, int q) throws CartServiceException {
		try {
			// Get book details
			BookCartModel book;
			try {
				book = bookService.getBookById(bId);
				if (book == null) {
					throw new CartServiceException(String.format(ERROR_BOOK_NOT_FOUND, bId));
				}
			} catch (Exception e) {
				throw new CartServiceException(String.format(ERROR_CANNOT_ADD_ITEM, e.getMessage()), e);
			}

			// Check if item already exists in cart
			Optional<CartItem> existing = findCartItemByUserIdAndBookId(uId, bId);
			CartItem result;

			if (existing.isPresent()) {
				// Update existing item quantity
				int nq = existing.get().getQuantity() + q;
				boolean upd = updateCartItemQuantityInternal(existing.get().getCartItemId(), nq);
				if (!upd) {
					throw new CartServiceException(ERROR_UPDATE_EXISTING_QUANTITY);
				}
				existing.get().setQuantity(nq);
				result = existing.get();
			} else {
				// Insert new cart item
				result = insertCartItem(uId, bId);
				if (result == null) {
					throw new CartServiceException(ERROR_INSERT_CART_ITEM);
				}
				if (q > 1) {
					boolean upd = updateCartItemQuantityInternal(result.getCartItemId(), q);
					if (!upd) {
						throw new CartServiceException(ERROR_UPDATE_NEW_QUANTITY);
					}
					result.setQuantity(q);
				}
			}

			result.setBookModel(book);
			return result;
		} catch (SQLException e) {
			throw new CartServiceException(ERROR_DB_ADDING_ITEM, e);
		} catch (CartServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new CartServiceException(ERROR_UNEXPECTED_ADDING_ITEM, e);
		}
	}

	/**
	 * Updates the quantity of an item in the cart.
	 *
	 * @param uId User ID
	 * @param cId Cart item ID
	 * @param nq  New quantity
	 * @return true if successful, false otherwise
	 * @throws CartServiceException If there's an error updating the item
	 */
	@Override
	public boolean updateCartItemQuantity(int uId, int cId, int nq) throws CartServiceException {
		if (nq <= 0) {
			// If quantity is zero or less, remove the item
			return this.removeItemFromCart(uId, cId);
		}

		try {
			return updateCartItemQuantityInternal(cId, nq);
		} catch (SQLException e) {
			throw new CartServiceException(ERROR_DB_UPDATING_QUANTITY, e);
		}
	}

	/**
	 * Removes an item from the cart.
	 *
	 * @param uId User ID
	 * @param cId Cart item ID
	 * @return true if successful, false otherwise
	 * @throws CartServiceException If there's an error removing the item
	 */
	@Override
	public boolean removeItemFromCart(int uId, int cId) throws CartServiceException {
		try {
			return deleteCartItemInternal(cId);
		} catch (SQLException e) {
			throw new CartServiceException(ERROR_DB_REMOVING_ITEM, e);
		}
	}

	/**
	 * Gets all items in a user's cart.
	 *
	 * @param userId The ID of the user
	 * @return List of CartItem objects representing items in the cart
	 * @throws CartServiceException If there's an error retrieving cart contents
	 */
	@Override
	public List<CartItem> getCartContents(int userId) throws CartServiceException {
		List<CartItem> items = new ArrayList<>();

		String sql = "SELECT ci.cart_itemID, ci.userID, ci.quantity, ci.updated_at, "
				+ "b.bookID, b.book_title, b.price, b.book_img_url, "
				+ "GROUP_CONCAT(a.author_name SEPARATOR ', ') AS authors " + "FROM cart_item ci "
				+ "JOIN book b ON ci.bookID = b.bookID " + "LEFT JOIN book_author ba ON b.bookID = ba.bookID "
				+ "LEFT JOIN author a ON ba.authorID = a.authorID " + "WHERE ci.userID = ? "
				+ "GROUP BY ci.cart_itemID, b.bookID " + "ORDER BY ci.updated_at DESC";

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
				cartItem.setBookId(rs.getInt("bookID"));

				BookCartModel book = new BookCartModel();
				book.setBookID(rs.getInt("bookID"));
				book.setBookTitle(rs.getString("book_title"));
				book.setPrice(rs.getBigDecimal("price"));
				book.setBookImgUrl(rs.getString("book_img_url"));
				book.setAuthorName(rs.getString("authors"));

				cartItem.setBookModel(book);
				items.add(cartItem);
			}
		} catch (SQLException e) {
			throw new CartServiceException(ERROR_DB_RETRIEVING_CONTENTS, e);
		} catch (ClassNotFoundException e) {
			throw new CartServiceException(ERROR_DB_DRIVER_CONFIG, e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}
		return items;
	}

	/**
	 * Clears all items from a user's cart.
	 *
	 * @param userId The ID of the user whose cart to clear
	 * @throws CartServiceException If there's an error clearing the cart
	 */
	@Override
	public void clearCart(Integer userId) throws CartServiceException {
		if (userId == null) {
			throw new CartServiceException(ERROR_CLEAR_CART_NULL_USER);
		}

		String sql = "DELETE FROM cart_item WHERE userID = ?";
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbConfiguration.getDbConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new CartServiceException(ERROR_DB_CLEARING_CART, e);
		} catch (ClassNotFoundException e) {
			throw new CartServiceException(ERROR_DB_DRIVER_CONFIG, e);
		} finally {
			close(ps);
			close(conn);
		}
	}

	/**
	 * Finds a cart item by user ID and book ID.
	 * 
	 * @param userId User ID
	 * @param bookId Book ID
	 * @return Optional containing the CartItem if found, empty otherwise
	 * @throws SQLException If a database error occurs
	 */
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
			throw new SQLException(ERROR_DB_DRIVER, e);
		}
		return Optional.empty();
	}

	/**
	 * Inserts a new cart item.
	 * 
	 * @param userId User ID
	 * @param bookId Book ID
	 * @return The newly created CartItem
	 * @throws SQLException If a database error occurs
	 */
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
					throw new SQLException(ERROR_INSERT_NO_ID);
			} else
				throw new SQLException(ERROR_INSERT_NO_ROWS);
		} catch (ClassNotFoundException e) {
			throw new SQLException(ERROR_DB_DRIVER, e);
		} finally {
			close(keys);
			close(ps);
			close(conn);
		}
		return newItem;
	}

	/**
	 * Updates the quantity of a cart item.
	 * 
	 * @param cartItemId  Cart item ID
	 * @param newQuantity New quantity
	 * @return true if successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	private boolean updateCartItemQuantityInternal(int cartItemId, int newQuantity) throws SQLException {
		String sql = "UPDATE cart_item SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE cart_itemID = ?";
		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, newQuantity);
			ps.setInt(2, cartItemId);
			return ps.executeUpdate() > 0;
		} catch (ClassNotFoundException e) {
			throw new SQLException(ERROR_DB_DRIVER, e);
		}
	}

	/**
	 * Deletes a cart item.
	 * 
	 * @param cartItemId Cart item ID
	 * @return true if successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	private boolean deleteCartItemInternal(int cartItemId) throws SQLException {
		String sql = "DELETE FROM cart_item WHERE cart_itemID = ?";
		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, cartItemId);
			return ps.executeUpdate() > 0;
		} catch (ClassNotFoundException e) {
			throw new SQLException(ERROR_DB_DRIVER, e);
		}
	}

	/**
	 * Maps a ResultSet row to a CartItem object.
	 * 
	 * @param rs ResultSet containing cart item data
	 * @return CartItem populated with data from the ResultSet
	 * @throws SQLException If a database error occurs
	 */
	private CartItem mapRowToCartItemBasic(ResultSet rs) throws SQLException {
		CartItem item = new CartItem();
		item.setCartItemId(rs.getInt("cart_itemID"));
		item.setUserId(rs.getInt("userID"));
		item.setBookId(rs.getInt("bookID"));
		item.setQuantity(rs.getInt("quantity"));
		item.setUpdatedAt(rs.getTimestamp("updated_at"));
		return item;
	}

	/**
	 * Safely closes a resource.
	 * 
	 * @param resource The resource to close
	 */
	private void close(AutoCloseable resource) {
		if (resource != null)
			try {
				resource.close();
			} catch (Exception e) {
				// Silent close
			}
	}
}