package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.model.PaymentModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PaymentService interface for handling payment operations.
 * This service manages payment creation, retrieval, and status updates.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class PaymentServiceImpl implements PaymentService {

	// Payment status constants
	public static final String PAYMENT_STATUS_PENDING = "Pending";
	public static final String PAYMENT_STATUS_COMPLETED = "Completed";
	public static final String PAYMENT_STATUS_FAILED = "Failed";
	public static final String PAYMENT_STATUS_CANCELLED = "Cancelled";

	// Error message constants
	private static final String ERROR_AMOUNT_INVALID = "Payment amount must be greater than zero";
	private static final String ERROR_METHOD_EMPTY = "Payment method cannot be empty";
	private static final String ERROR_STATUS_EMPTY = "Payment status cannot be empty";
	private static final String ERROR_CREATE_NO_ROWS = "Creating payment failed, no rows affected.";
	private static final String ERROR_CREATE_NO_ID = "Creating payment failed, no ID obtained.";
	private static final String ERROR_DB_CONNECTION = "Database connection error";

	/**
	 * Creates a new payment record in the database.
	 *
	 * @param orderId The ID of the order associated with this payment
	 * @param amount  The payment amount
	 * @param method  The payment method used
	 * @param status  The payment status
	 * @return The ID of the created payment record
	 * @throws SQLException If a database error occurs
	 */
	@Override
	public int createPayment(int orderId, BigDecimal amount, String method, String status) throws SQLException {
		int generatedPaymentId = -1;
		Connection conn = null;

		try {
			// Validate input parameters
			if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
				System.err.println("Invalid payment amount: " + (amount == null ? "null" : amount.toString()));
				throw new IllegalArgumentException(ERROR_AMOUNT_INVALID);
			}

			if (method == null || method.trim().isEmpty()) {
				System.err.println("Invalid payment method: empty or null");
				throw new IllegalArgumentException(ERROR_METHOD_EMPTY);
			}

			// Set default status as Pending if not provided
			if (status == null || status.trim().isEmpty()) {
				status = PAYMENT_STATUS_PENDING;
				
			} else {
				// Ensure status is properly capitalized
				status = capitalizeStatus(status);
				
			}

			

			// Get connection and disable auto-commit for transaction
			conn = DbConfiguration.getDbConnection();
			conn.setAutoCommit(false);

			String createPaymentSql = "INSERT INTO payment (orderID, payment_date, payment_amount, payment_method, payment_status) "
					+ "VALUES (?, NOW(), ?, ?, ?)";

			try (PreparedStatement ps = conn.prepareStatement(createPaymentSql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, orderId);
				ps.setBigDecimal(2, amount);
				ps.setString(3, method);
				ps.setString(4, status);

				

				int affectedRows = ps.executeUpdate();
				if (affectedRows == 0) {
					conn.rollback();
					System.err.println(ERROR_CREATE_NO_ROWS);
					throw new SQLException(ERROR_CREATE_NO_ROWS);
				}

				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						generatedPaymentId = generatedKeys.getInt(1);
						
					} else {
						conn.rollback();
						System.err.println(ERROR_CREATE_NO_ID);
						throw new SQLException(ERROR_CREATE_NO_ID);
					}
				}

				// Commit transaction
				conn.commit();
				
			}
		} catch (SQLException e) {
			// Rollback transaction in case of error
			if (conn != null) {
				try {
					conn.rollback();
					System.err.println("Transaction rolled back due to error");
				} catch (SQLException ex) {
					System.err.println("Failed to rollback transaction: " + ex.getMessage());
				}
			}
			System.err.println("Database error creating payment for order ID " + orderId + ": " + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			System.err.println("Database connection error: " + e.getMessage());
			e.printStackTrace();
			throw new SQLException(ERROR_DB_CONNECTION, e);
		} finally {
			// Reset auto-commit and close connection
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
					
				} catch (SQLException e) {
					System.err.println("Error closing connection: " + e.getMessage());
				}
			}
		}

		return generatedPaymentId;
	}

	/**
	 * Retrieves a payment record by its ID.
	 *
	 * @param paymentId The ID of the payment to retrieve
	 * @return The payment model if found, null otherwise
	 * @throws SQLException If a database error occurs
	 */
	@Override
	public PaymentModel getPaymentById(int paymentId) throws SQLException {
		PaymentModel payment = null;

		String getPaymentByIdSql = "SELECT * FROM payment WHERE paymentID = ?";
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(getPaymentByIdSql)) {

			ps.setInt(1, paymentId);
			;

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					payment = mapResultSetToPayment(rs);
					
				} 
			}
		} catch (SQLException e) {
			System.err.println("Database error retrieving payment ID: " + paymentId);
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			System.err.println("Database connection error: " + e.getMessage());
			throw new SQLException(ERROR_DB_CONNECTION, e);
		}

		return payment;
	}

	/**
	 * Updates the status of a payment record.
	 *
	 * @param paymentId The ID of the payment to update
	 * @param newStatus The new status to set
	 * @return true if successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	@Override
	public boolean updatePaymentStatus(int paymentId, String newStatus) throws SQLException {
		if (newStatus == null || newStatus.trim().isEmpty()) {
			throw new IllegalArgumentException(ERROR_STATUS_EMPTY);
		}

		// Ensure status is properly capitalized
		newStatus = capitalizeStatus(newStatus);

		int rowsUpdated = 0;
		Connection conn = null;

		try {
			conn = DbConfiguration.getDbConnection();
			conn.setAutoCommit(false);

			String updatePaymentStatusSql = "UPDATE payment SET payment_status = ? WHERE paymentID = ?";
			try (PreparedStatement ps = conn.prepareStatement(updatePaymentStatusSql)) {
				ps.setString(1, newStatus);
				ps.setInt(2, paymentId);

				rowsUpdated = ps.executeUpdate();

				if (rowsUpdated > 0) {
					conn.commit();

				} else {
					conn.rollback();

				}
			}
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					// Ignore
				}
			}
			System.err.println("Database error updating payment status for ID: " + paymentId);
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			System.err.println("Database connection error: " + e.getMessage());
			throw new SQLException(ERROR_DB_CONNECTION, e);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					// Ignore
				}
			}
		}

		return rowsUpdated > 0;
	}

	/**
	 * Updates payment status for all payments associated with an order.
	 * 
	 * @param orderId   The ID of the order to update payment status for
	 * @param newStatus The new payment status
	 * @return true if successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	public boolean updatePaymentStatusByOrderId(int orderId, String newStatus) throws SQLException {
		if (newStatus == null || newStatus.trim().isEmpty()) {
			throw new IllegalArgumentException(ERROR_STATUS_EMPTY);
		}

		// Ensure status is properly capitalized
		newStatus = capitalizeStatus(newStatus);

		int rowsUpdated = 0;
		Connection conn = null;

		try {
			conn = DbConfiguration.getDbConnection();
			conn.setAutoCommit(false);

			String updateOrderPaymentStatusSql = "UPDATE payment SET payment_status = ? WHERE orderID = ?";
			try (PreparedStatement ps = conn.prepareStatement(updateOrderPaymentStatusSql)) {
				ps.setString(1, newStatus);
				ps.setInt(2, orderId);

				rowsUpdated = ps.executeUpdate();

				if (rowsUpdated > 0) {
					conn.commit();

				} else {
					conn.rollback();

				}
			}
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					// Ignore
				}
			}
			System.err.println("Database error updating payment status for order ID: " + orderId);
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			System.err.println("Database connection error: " + e.getMessage());
			throw new SQLException(ERROR_DB_CONNECTION, e);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					// Ignore
				}
			}
		}

		return rowsUpdated > 0;
	}

	/**
	 * Marks a payment as completed by updating its status.
	 * 
	 * @param paymentId The ID of the payment to mark as completed
	 * @return true if successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	public boolean markPaymentAsCompleted(int paymentId) throws SQLException {
		return updatePaymentStatus(paymentId, PAYMENT_STATUS_COMPLETED);
	}

	/**
	 * Marks all payments for an order as completed.
	 * 
	 * @param orderId The ID of the order to mark payments as completed
	 * @return true if successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	public boolean markOrderPaymentsAsCompleted(int orderId) throws SQLException {
		return updatePaymentStatusByOrderId(orderId, PAYMENT_STATUS_COMPLETED);
	}

	/**
	 * Retrieves all payments for a specific order.
	 *
	 * @param orderId The ID of the order to retrieve payments for
	 * @return List of payment models associated with the order
	 * @throws SQLException If a database error occurs
	 */
	@Override
	public List<PaymentModel> getPaymentsByOrderId(int orderId) throws SQLException {
		List<PaymentModel> payments = new ArrayList<>();

		String getPaymentsByOrderIdSql = "SELECT * FROM payment WHERE orderID = ? ORDER BY payment_date DESC";
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(getPaymentsByOrderIdSql)) {

			ps.setInt(1, orderId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					PaymentModel payment = mapResultSetToPayment(rs);
					payments.add(payment);
				}

			}
		} catch (SQLException e) {
			System.err.println("Database error retrieving payments for order ID: " + orderId);
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			System.err.println("Database connection error: " + e.getMessage());
			throw new SQLException(ERROR_DB_CONNECTION, e);
		}

		return payments;
	}

	/**
	 * Maps a database result set row to a PaymentModel object.
	 * 
	 * @param rs The ResultSet containing payment data
	 * @return A populated PaymentModel object
	 * @throws SQLException If a database error occurs
	 */
	private PaymentModel mapResultSetToPayment(ResultSet rs) throws SQLException {
		PaymentModel payment = new PaymentModel();

		payment.setPaymentID(rs.getInt("paymentID"));
		payment.setOrderID(rs.getInt("orderID"));
		payment.setPaymentDate(rs.getTimestamp("payment_date"));
		payment.setPaymentAmount(rs.getBigDecimal("payment_amount"));
		payment.setPaymentMethod(rs.getString("payment_method"));
		payment.setPaymentStatus(rs.getString("payment_status"));

		return payment;
	}

	/**
	 * Standardizes the capitalization of payment status values.
	 * 
	 * @param status The payment status string to format
	 * @return Properly capitalized status string
	 */
	private String capitalizeStatus(String status) {
		if (status == null || status.isEmpty()) {
			return PAYMENT_STATUS_PENDING;
		}

		// Standard status values
		if (status.equalsIgnoreCase("completed"))
			return PAYMENT_STATUS_COMPLETED;
		if (status.equalsIgnoreCase("pending"))
			return PAYMENT_STATUS_PENDING;
		if (status.equalsIgnoreCase("failed"))
			return PAYMENT_STATUS_FAILED;
		if (status.equalsIgnoreCase("cancelled"))
			return PAYMENT_STATUS_CANCELLED;

		// If it's not a standard value, capitalize first letter
		return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
	}
}