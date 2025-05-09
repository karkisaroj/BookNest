package com.booknest.service;

import com.booknest.model.OrderModel;
import com.booknest.config.DbConfiguration;

import java.math.BigDecimal; // Added missing import
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderService {

	private Connection dbConn;
	private boolean isConnectionError = false;

	public AdminOrderService() {
		try {
			dbConn = DbConfiguration.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}

	public List<OrderModel> getAllOrders() {
		List<OrderModel> orders = new ArrayList<>();
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to fetch orders.");
			return orders;
		}

		String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders`";

		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				OrderModel order = new OrderModel();
				order.setOrderID(rs.getInt("orderID")); // Using setOrderID to match JSP expectations
				order.setUserID(rs.getInt("userID")); // Using setUserID to match JSP expectations
				order.setOrderDate(rs.getTimestamp("order_date"));
				order.setShippingAddress(rs.getString("shipping_address"));
				order.setTotalAmount(rs.getBigDecimal("total_amount"));
				order.setOrderStatus(rs.getString("order_status"));

				orders.add(order);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return orders;
	}

	public OrderModel getOrderById(int orderID) {
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to fetch order.");
			return null;
		}

		String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` WHERE orderID = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, orderID);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					OrderModel order = new OrderModel();
					order.setOrderID(rs.getInt("orderID")); // Using setOrderID to match JSP expectations
					order.setUserID(rs.getInt("userID")); // Using setUserID to match JSP expectations
					order.setOrderDate(rs.getTimestamp("order_date"));
					order.setShippingAddress(rs.getString("shipping_address"));
					order.setTotalAmount(rs.getBigDecimal("total_amount"));
					order.setOrderStatus(rs.getString("order_status"));

					return order;
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public boolean updateOrderStatus(int orderID, String status) {
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to update order status.");
			return false;
		}

		String query = "UPDATE `orders` SET order_status = ? WHERE orderID = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, status);
			stmt.setInt(2, orderID);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public List<OrderModel> getOrdersByStatus(String status) {
		List<OrderModel> orders = new ArrayList<>();
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to fetch orders.");
			return orders;
		}

		String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` WHERE order_status = ?";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, status);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					OrderModel order = new OrderModel();
					order.setOrderID(rs.getInt("orderID")); // Using setOrderID to match JSP expectations
					order.setUserID(rs.getInt("userID")); // Using setUserID to match JSP expectations
					order.setOrderDate(rs.getTimestamp("order_date"));
					order.setShippingAddress(rs.getString("shipping_address"));
					order.setTotalAmount(rs.getBigDecimal("total_amount"));
					order.setOrderStatus(rs.getString("order_status"));

					orders.add(order);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return orders;
	}

	public List<OrderModel> getOrdersByUserId(int userId) {
		List<OrderModel> orders = new ArrayList<>();
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to fetch orders.");
			return orders;
		}

		String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` WHERE userID = ? ORDER BY order_date DESC";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					OrderModel order = new OrderModel();
					order.setOrderID(rs.getInt("orderID")); // Using setOrderID to match JSP expectations
					order.setUserID(rs.getInt("userID")); // Using setUserID to match JSP expectations
					order.setOrderDate(rs.getTimestamp("order_date"));
					order.setShippingAddress(rs.getString("shipping_address"));
					order.setTotalAmount(rs.getBigDecimal("total_amount"));
					order.setOrderStatus(rs.getString("order_status"));

					orders.add(order);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return orders;
	}

	public int getOrderCount() {
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to count orders.");
			return 0;
		}

		String query = "SELECT COUNT(*) as count FROM `orders`";
		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public List<OrderModel> getRecentOrders(int limit) {
		List<OrderModel> orders = new ArrayList<>();
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to fetch recent orders.");
			return orders;
		}

		String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` ORDER BY order_date DESC LIMIT ?";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, limit);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					OrderModel order = new OrderModel();
					order.setOrderID(rs.getInt("orderID")); // Using setOrderID to match JSP expectations
					order.setUserID(rs.getInt("userID")); // Using setUserID to match JSP expectations
					order.setOrderDate(rs.getTimestamp("order_date"));
					order.setShippingAddress(rs.getString("shipping_address"));
					order.setTotalAmount(rs.getBigDecimal("total_amount"));
					order.setOrderStatus(rs.getString("order_status"));

					orders.add(order);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return orders;
	}

	public BigDecimal getTotalSales() {
		if (isConnectionError) {
			System.err.println("Database connection error. Unable to calculate total sales.");
			return BigDecimal.ZERO;
		}

		String query = "SELECT SUM(total_amount) as total FROM `orders` WHERE order_status = 'completed'";
		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				BigDecimal total = rs.getBigDecimal("total");
				return total != null ? total : BigDecimal.ZERO;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return BigDecimal.ZERO;
	}

	public void closeConnection() {
		if (dbConn != null) {
			try {
				dbConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}