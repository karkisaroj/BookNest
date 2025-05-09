package com.booknest.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import com.booknest.model.PaymentModel;

public interface PaymentService {
	/**
	 * Records a new payment in the database
	 * 
	 * @param orderId The ID of the order associated with this payment
	 * @param amount  The payment amount
	 * @param method  The payment method (e.g., "Credit Card", "Cash")
	 * @param status  The payment status (e.g., "Completed", "Pending")
	 * @return The ID of the newly created payment record
	 * @throws SQLException If a database error occurs
	 */
	int createPayment(int orderId, BigDecimal amount, String method, String status) throws SQLException;

	/**
	 * Retrieves a payment record by its ID
	 * 
	 * @param paymentId The ID of the payment to retrieve
	 * @return The payment model, or null if not found
	 * @throws SQLException If a database error occurs
	 */
	PaymentModel getPaymentById(int paymentId) throws SQLException;

	/**
	 * Updates the status of a payment
	 * 
	 * @param paymentId The ID of the payment to update
	 * @param newStatus The new payment status
	 * @return true if the update was successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	boolean updatePaymentStatus(int paymentId, String newStatus) throws SQLException;

	/**
	 * Retrieves all payments for a specific order
	 * 
	 * @param orderId The ID of the order
	 * @return A list of payment records for the order
	 * @throws SQLException If a database error occurs
	 */
	List<PaymentModel> getPaymentsByOrderId(int orderId) throws SQLException;
}