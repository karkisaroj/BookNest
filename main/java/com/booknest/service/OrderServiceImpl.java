package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.model.BookModel;
import com.booknest.model.CartItem;
import com.booknest.model.OrderModel;
import com.booknest.model.OrderItemModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the OrderService interface for managing order operations.
 * Handles creation, retrieval and management of orders and order items.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class OrderServiceImpl implements OrderService {

	// Default order status - based on your database schema
	private static final String DEFAULT_ORDER_STATUS = "in progress";

	// Order status constants
	private static final String STATUS_COMPLETED = "completed";
	private static final String STATUS_CANCELLED = "cancelled";
	private static final String STATUS_IN_PROGRESS = "in progress";

	// Error message constants
	private static final String ERROR_EMPTY_CART = "Cannot create order with empty cart";
	private static final String ERROR_NULL_ADDRESS = "Address cannot be null";
	private static final String ERROR_EMPTY_STATUS = "Order status cannot be empty";
	private static final String ERROR_DB_CONNECTION = "Database connection error";
	private static final String ERROR_CREATE_ORDER = "Creating order failed, no rows affected.";
	private static final String ERROR_NO_ID = "Creating order failed, no ID obtained.";
	private static final String ERROR_NULL_BOOK_MODEL = "Cart item has null book model";

	// Structure information for order_book table
	private static boolean hasQuantityColumn = false;
	private static boolean hasUnitPriceColumn = false;
	private static String quantityColumnName = "quantity";
	private static String unitPriceColumnName = "unit_price";
	private static String SQL_CREATE_ORDER_ITEM = null;
	private static String SQL_GET_ORDER_ITEMS = null;

	/**
	 * Constructor that initializes service and detects database structure
	 */
	public OrderServiceImpl() {
		new BookServiceImpl();
		// Initialize SQL statements with actual column names
		initializeOrderBookStructure();
	}

	/**
	 * Dynamically determines the structure of the order_book table and constructs
	 * appropriate SQL statements
	 */
	private void initializeOrderBookStructure() {
		try (Connection conn = DbConfiguration.getDbConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("DESCRIBE order_book")) {

			List<String> columnNames = new ArrayList<>();
			while (rs.next()) {
				String columnName = rs.getString("Field");
				columnNames.add(columnName.toLowerCase());

				// Check for quantity-like columns
				if (columnName.equalsIgnoreCase("quantity")) {
					hasQuantityColumn = true;
					quantityColumnName = columnName;
				} else if (columnName.equalsIgnoreCase("count")) {
					hasQuantityColumn = true;
					quantityColumnName = columnName;
				}

				// Check for price-like columns
				if (columnName.equalsIgnoreCase("unit_price")) {
					hasUnitPriceColumn = true;
					unitPriceColumnName = columnName;
				} else if (columnName.equalsIgnoreCase("price")) {
					hasUnitPriceColumn = true;
					unitPriceColumnName = columnName;
				}
			}

			// Build SQL statements based on actual columns
			StringBuilder insertSql = new StringBuilder("INSERT INTO order_book (orderID, bookID");
			StringBuilder valuesSql = new StringBuilder("VALUES (?, ?");

			if (hasQuantityColumn) {
				insertSql.append(", ").append(quantityColumnName);
				valuesSql.append(", ?");
			}

			if (hasUnitPriceColumn) {
				insertSql.append(", ").append(unitPriceColumnName);
				valuesSql.append(", ?");
			}

			insertSql.append(") ").append(valuesSql).append(")");
			SQL_CREATE_ORDER_ITEM = insertSql.toString();

			// Build select query for order items
			SQL_GET_ORDER_ITEMS = "SELECT ob.*, b.book_title, b.book_img_url FROM order_book ob "
					+ "JOIN book b ON ob.bookID = b.bookID WHERE ob.orderID = ?";

		} catch (SQLException | ClassNotFoundException e) {
			// Set defaults if we couldn't determine structure
			SQL_CREATE_ORDER_ITEM = "INSERT INTO order_book (orderID, bookID) VALUES (?, ?)";
			SQL_GET_ORDER_ITEMS = "SELECT ob.*, b.book_title, b.book_img_url FROM order_book ob "
					+ "JOIN book b ON ob.bookID = b.bookID WHERE ob.orderID = ?";
		}
	}

	/**
	 * Format the shipping address to standard format Removes any date/time, user
	 * login, and phone information
	 * 
	 * @param address Original address string
	 * @return Formatted address
	 */
	private String formatShippingAddress(String address) {
		if (address == null || address.isEmpty()) {
			return "";
		}

		// Handle the special format with login info
		if (address.contains("Current Date and Time") && address.contains("Current User's Login")) {
			Pattern pattern = Pattern.compile("Current User's Login:\\s*([^\\s]+)(.*)");
			Matcher matcher = pattern.matcher(address);

			if (matcher.find()) {
				// Check for address part after the username
				String remaining = matcher.group(2);
				if (remaining != null && !remaining.trim().isEmpty()) {
					return remaining.trim();
				}
			}

			// Couldn't extract meaningful address, return empty
			return "";
		}

		// Check if the address contains a phone number
		if (address.contains("Phone:")) {
			// Extract the location part before the phone
			String locationPart = address.split("\\s*,\\s*Phone:")[0].trim();
			return locationPart;
		}

		// If we couldn't parse the format, return the original without any processing
		return address;
	}

	/**
	 * Creates a standardized address string from components
	 * 
	 * @param streetAddress Street address
	 * @param city          City
	 * @param stateProvince State or province
	 * @param postalCode    Postal code
	 * @return Formatted address string
	 */
	public String createStandardAddress(String streetAddress, String city, String stateProvince, String postalCode) {
		StringBuilder formattedAddress = new StringBuilder();

		if (streetAddress != null && !streetAddress.trim().isEmpty()) {
			formattedAddress.append(streetAddress.trim());
		}

		if (city != null && !city.trim().isEmpty()) {
			if (formattedAddress.length() > 0) {
				formattedAddress.append(", ");
			}
			formattedAddress.append(city.trim());
		}

		if (stateProvince != null && !stateProvince.trim().isEmpty()) {
			if (formattedAddress.length() > 0) {
				formattedAddress.append(", ");
			}
			formattedAddress.append(stateProvince.trim());
		}

		if (postalCode != null && !postalCode.trim().isEmpty()) {
			if (formattedAddress.length() > 0) {
				formattedAddress.append(" ");
			}
			formattedAddress.append(postalCode.trim());
		}

		return formattedAddress.toString();
	}

	/**
	 * Creates a new order in the database
	 * 
	 * @param userId          User ID for the order
	 * @param cartItems       Items to be included in the order
	 * @param shippingAddress Address for delivery
	 * @param subtotal        Subtotal amount
	 * @param shippingCost    Shipping cost
	 * @param discount        Discount amount
	 * @param totalAmount     Total order amount
	 * @return Created order ID, or -1 if failed
	 */
	@Override
	public int createOrder(int userId, List<CartItem> cartItems, String shippingAddress, BigDecimal subtotal,
			BigDecimal shippingCost, BigDecimal discount, BigDecimal totalAmount) throws SQLException {

		if (cartItems == null || cartItems.isEmpty()) {
			throw new IllegalArgumentException(ERROR_EMPTY_CART);
		}

		// Format the shipping address to store in standardized format
		String formattedAddress = formatShippingAddress(shippingAddress);

		Connection conn = null;
		int generatedOrderId = -1;

		try {
			conn = DbConfiguration.getDbConnection();
			conn.setAutoCommit(false); // Start transaction

			// Determine what order status to use from the database schema
			String statusToUse = DEFAULT_ORDER_STATUS;

			try (Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("DESCRIBE orders order_status")) {

				if (rs.next()) {
					// Get the column type
					String columnType = rs.getString("Type");

					// Parse enum values if it's an enum
					if (columnType.startsWith("enum(")) {
						// Extract values between the parentheses, remove quotes
						String valuesPart = columnType.substring(5, columnType.length() - 1);
						List<String> enumValues = new ArrayList<>();

						// Handle quotes properly in enum definition
						Pattern p = Pattern.compile("'([^']*)'");
						Matcher m = p.matcher(valuesPart);
						while (m.find()) {
							enumValues.add(m.group(1));
						}

						// Use "in progress" if available, otherwise use the first value
						if (enumValues.contains(STATUS_IN_PROGRESS)) {
							statusToUse = STATUS_IN_PROGRESS;
						} else if (!enumValues.isEmpty()) {
							statusToUse = enumValues.get(0);
						}
					}
				}
			} catch (Exception e) {
				// Continue with default status
			}

			String createOrderSql = "INSERT INTO orders (userID, order_date, shipping_address, total_amount, order_status) "
					+ "VALUES (?, NOW(), ?, ?, ?)";

			try (PreparedStatement psOrder = conn.prepareStatement(createOrderSql, Statement.RETURN_GENERATED_KEYS)) {
				psOrder.setInt(1, userId);
				psOrder.setString(2, formattedAddress); // Use formatted address
				psOrder.setBigDecimal(3, totalAmount); // Only total_amount is stored
				psOrder.setString(4, statusToUse);

				int affectedRows = psOrder.executeUpdate();
				if (affectedRows == 0) {
					throw new SQLException(ERROR_CREATE_ORDER);
				}

				try (ResultSet generatedKeys = psOrder.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						generatedOrderId = generatedKeys.getInt(1);
					} else {
						throw new SQLException(ERROR_NO_ID);
					}
				}
			}

			// Verify the structure of the order_book table
			if (SQL_CREATE_ORDER_ITEM == null) {
				initializeOrderBookStructure();
			}

			// Add order items to order_book table - handle different column structures
			try (PreparedStatement psItem = conn.prepareStatement(SQL_CREATE_ORDER_ITEM)) {
				for (CartItem item : cartItems) {
					if (item.getBookModel() == null) {
						throw new IllegalArgumentException(ERROR_NULL_BOOK_MODEL);
					}

					// All queries will have at least orderID and bookID
					psItem.setInt(1, generatedOrderId);
					psItem.setInt(2, item.getBookModel().getBookID());

					// Set additional fields if they exist
					int paramIndex = 3;

					if (hasQuantityColumn) {
						psItem.setInt(paramIndex++, item.getQuantity());
					}

					if (hasUnitPriceColumn) {
						psItem.setBigDecimal(paramIndex++, item.getBookModel().getPrice());
					}

					psItem.addBatch();
				}
				psItem.executeBatch();
			}

			// Clear the user's cart
			String clearCartSql = "DELETE FROM cart_item WHERE userID = ?";
			try (PreparedStatement psClearCart = conn.prepareStatement(clearCartSql)) {
				psClearCart.setInt(1, userId);
				psClearCart.executeUpdate();
			}

			// Commit the transaction
			conn.commit();

		} catch (SQLException | ClassNotFoundException e) {
			// Attempt to roll back
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					// Silent rollback error
				}
			}
			if (e instanceof SQLException) {
				throw (SQLException) e;
			} else {
				throw new SQLException(ERROR_DB_CONNECTION, e);
			}
		} finally {
			// Reset auto-commit and close connection
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					// Silent close error
				}
			}
		}

		return generatedOrderId;
	}

	/**
	 * Updates shipping address for an existing order
	 * 
	 * @param orderId    Order ID to update
	 * @param newAddress New shipping address
	 * @return true if successful, false otherwise
	 */
	public boolean updateShippingAddress(int orderId, String newAddress) throws SQLException {
		if (newAddress == null) {
			throw new IllegalArgumentException(ERROR_NULL_ADDRESS);
		}

		// Format the address before updating
		String formattedAddress = formatShippingAddress(newAddress);
		int rowsUpdated = 0;

		String updateAddressSql = "UPDATE orders SET shipping_address = ? WHERE orderID = ?";
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(updateAddressSql)) {

			ps.setString(1, formattedAddress);
			ps.setInt(2, orderId);

			rowsUpdated = ps.executeUpdate();

		} catch (SQLException | ClassNotFoundException e) {
			if (e instanceof SQLException) {
				throw (SQLException) e;
			} else {
				throw new SQLException(ERROR_DB_CONNECTION, e);
			}
		}

		return rowsUpdated > 0;
	}

	/**
	 * Retrieves an order by its ID, including all details
	 * 
	 * @param orderId Order ID to retrieve
	 * @return Order model or null if not found
	 */
	@Override
	public OrderModel getOrderById(int orderId) throws SQLException {
		OrderModel order = null;

		String getOrderSql = "SELECT * FROM orders WHERE orderID = ?";
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(getOrderSql)) {

			ps.setInt(1, orderId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					order = mapResultSetToOrder(rs);

					// Also retrieve the order items
					List<OrderItemModel> orderItems = getOrderItems(orderId);
					order.setOrderItems(orderItems);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			if (e instanceof SQLException) {
				throw (SQLException) e;
			} else {
				throw new SQLException(ERROR_DB_CONNECTION, e);
			}
		}

		return order;
	}

	/**
	 * Retrieves all order items for a specific order
	 * 
	 * @param orderId Order ID to retrieve items for
	 * @return List of order items
	 */
	@Override
	public List<OrderItemModel> getOrderItems(int orderId) throws SQLException {
		// Initialize SQL if needed
		if (SQL_GET_ORDER_ITEMS == null) {
			initializeOrderBookStructure();
		}

		List<OrderItemModel> orderItems = new ArrayList<>();

		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(SQL_GET_ORDER_ITEMS)) {

			ps.setInt(1, orderId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					OrderItemModel item = new OrderItemModel();

					// Try different ID field names that might be in the schema
					try {
						item.setOrderItemId(rs.getInt("id"));
					} catch (SQLException e1) {
						try {
							item.setOrderItemId(rs.getInt("orderBookID"));
						} catch (SQLException e2) {
							// If no ID field exists, set default
							item.setOrderItemId(0);
						}
					}

					item.setOrderId(rs.getInt("orderID"));
					item.setBookId(rs.getInt("bookID"));

					// Try to get quantity using the column name we found
					if (hasQuantityColumn) {
						try {
							item.setQuantity(rs.getInt(quantityColumnName));
						} catch (SQLException e) {
							item.setQuantity(1); // Default to 1 if not found
						}
					} else {
						item.setQuantity(1); // Default to 1 if column doesn't exist
					}

					// Try to get price using the column name we found
					if (hasUnitPriceColumn) {
						try {
							item.setUnitPrice(rs.getBigDecimal(unitPriceColumnName));
						} catch (SQLException e) {
							item.setUnitPrice(BigDecimal.ZERO); // Default if not found
						}
					} else {
						item.setUnitPrice(BigDecimal.ZERO); // Default if column doesn't exist
					}

					// Create a minimal book model with available data
					BookModel book = new BookModel();
					book.setBookID(rs.getInt("bookID"));
					book.setBookTitle(rs.getString("book_title"));
					book.setBookImgUrl(rs.getString("book_img_url"));

					item.setBook(book);
					orderItems.add(item);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			if (e instanceof SQLException) {
				throw (SQLException) e;
			} else {
				throw new SQLException(ERROR_DB_CONNECTION, e);
			}
		}

		return orderItems;
	}

	/**
	 * Updates the status of an order
	 * 
	 * @param orderId   Order ID to update
	 * @param newStatus New status to set
	 * @return true if successful, false otherwise
	 */
	@Override
	public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
		if (newStatus == null || newStatus.trim().isEmpty()) {
			throw new IllegalArgumentException(ERROR_EMPTY_STATUS);
		}

		// Make sure we're using a valid status from the enum
		// Valid values: 'completed','cancelled','in progress'
		String validStatus = newStatus.trim().toLowerCase();
		if (!validStatus.equals(STATUS_COMPLETED) && !validStatus.equals(STATUS_CANCELLED)
				&& !validStatus.equals(STATUS_IN_PROGRESS)) {

			// Default to "in progress" if the provided status is invalid
			validStatus = STATUS_IN_PROGRESS;
		}

		int rowsUpdated = 0;

		String updateStatusSql = "UPDATE orders SET order_status = ? WHERE orderID = ?";
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(updateStatusSql)) {

			ps.setString(1, validStatus);
			ps.setInt(2, orderId);

			rowsUpdated = ps.executeUpdate();

		} catch (SQLException | ClassNotFoundException e) {
			if (e instanceof SQLException) {
				throw (SQLException) e;
			} else {
				throw new SQLException(ERROR_DB_CONNECTION, e);
			}
		}

		return rowsUpdated > 0;
	}

	/**
	 * Retrieves all orders for a specific user
	 * 
	 * @param userId User ID to retrieve orders for
	 * @return List of orders for the user
	 */
	@Override
	public List<OrderModel> getOrdersByUserId(int userId) throws SQLException {
		List<OrderModel> orders = new ArrayList<>();

		String getUserOrdersSql = "SELECT * FROM orders WHERE userID = ? ORDER BY order_date DESC";
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(getUserOrdersSql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					OrderModel order = mapResultSetToOrder(rs);
					orders.add(order);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			if (e instanceof SQLException) {
				throw (SQLException) e;
			} else {
				throw new SQLException(ERROR_DB_CONNECTION, e);
			}
		}

		return orders;
	}

	/**
     * Maps a database result set to an order model
     * 
     * @param rs Result set to map
     * @return Populated OrderModel
     */
    private OrderModel mapResultSetToOrder(ResultSet rs) throws SQLException {
        OrderModel order = new OrderModel();

        order.setOrderID(rs.getInt("orderID"));
        order.setUserID(rs.getInt("userID"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setShippingAddress(rs.getString("shipping_address"));

        // Set the required totalAmount
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setOrderStatus(rs.getString("order_status"));

        // Set defaults for columns that might not exist
        order.setSubtotal(BigDecimal.ZERO);
        order.setShippingCost(BigDecimal.ZERO);
        order.setDiscount(BigDecimal.ZERO);

        return order;
    }
}