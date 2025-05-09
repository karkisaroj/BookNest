package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.model.PaymentModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

	// --- SQL Query Constants ---
	private static final String SQL_CREATE_PAYMENT = "INSERT INTO payment (orderID, payment_date, payment_amount, payment_method, payment_status) "
			+ "VALUES (?, NOW(), ?, ?, ?)";

	private static final String SQL_GET_PAYMENT_BY_ID = "SELECT * FROM payment WHERE paymentID = ?";

	private static final String SQL_UPDATE_PAYMENT_STATUS = "UPDATE payment SET payment_status = ? WHERE paymentID = ?";

	private static final String SQL_GET_PAYMENTS_BY_ORDER_ID = "SELECT * FROM payment WHERE orderID = ? ORDER BY payment_date DESC";

	@Override
	public int createPayment(int orderId, BigDecimal amount, String method, String status) throws SQLException {
		int generatedPaymentId = -1;

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Payment amount must be greater than zero");
		}

		if (method == null || method.trim().isEmpty()) {
			throw new IllegalArgumentException("Payment method cannot be empty");
		}

		if (status == null || status.trim().isEmpty()) {
			status = "Pending"; // Default status if not provided
		}

		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(SQL_CREATE_PAYMENT, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, orderId);
			ps.setBigDecimal(2, amount);
			ps.setString(3, method);
			ps.setString(4, status);

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating payment failed, no rows affected.");
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					generatedPaymentId = generatedKeys.getInt(1);
					System.out.println("PaymentServiceImpl: Created payment with ID: " + generatedPaymentId);
				} else {
					throw new SQLException("Creating payment failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			System.err.println("Database error creating payment for order ID: " + orderId);
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return generatedPaymentId;
	}

	@Override
	public PaymentModel getPaymentById(int paymentId) throws SQLException {
		PaymentModel payment = null;

		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(SQL_GET_PAYMENT_BY_ID)) {

			ps.setInt(1, paymentId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					payment = mapResultSetToPayment(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("Database error retrieving payment ID: " + paymentId);
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return payment;
	}

	@Override
	public boolean updatePaymentStatus(int paymentId, String newStatus) throws SQLException {
		if (newStatus == null || newStatus.trim().isEmpty()) {
			throw new IllegalArgumentException("Payment status cannot be empty");
		}

		int rowsUpdated = 0;

		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PAYMENT_STATUS)) {

			ps.setString(1, newStatus);
			ps.setInt(2, paymentId);

			rowsUpdated = ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println("Database error updating payment status for ID: " + paymentId);
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return rowsUpdated > 0;
	}

	@Override
	public List<PaymentModel> getPaymentsByOrderId(int orderId) throws SQLException {
		List<PaymentModel> payments = new ArrayList<>();

		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(SQL_GET_PAYMENTS_BY_ORDER_ID)) {

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
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return payments;
	}

	// Helper method to map a ResultSet to a PaymentModel
	private PaymentModel mapResultSetToPayment(ResultSet rs) throws SQLException {
		PaymentModel payment = new PaymentModel();

		payment.setPaymentID(rs.getInt("paymentID"));
		payment.setOrderID(rs.getInt("orderID"));
		payment.setPayment_date(rs.getTimestamp("payment_date"));
		payment.setPayment_amount(rs.getBigDecimal("payment_amount"));
		payment.setPayment_method(rs.getString("payment_method"));
		payment.setPayment_status(rs.getString("payment_status"));

		return payment;
	}
}