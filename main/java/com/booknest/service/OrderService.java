package com.booknest.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import com.booknest.model.CartItem;
import com.booknest.model.OrderModel;
import com.booknest.model.OrderItemModel;

public interface OrderService {
	/**
	 * Creates a new order from cart items
	 * 
	 * @param userId          The ID of the user placing the order
	 * @param cartItems       The list of cart items to be included in the order
	 * @param shippingAddress The shipping address for the order
	 * @param subtotal        The subtotal of the order
	 * @param shippingCost    The shipping cost
	 * @param discount        The discount amount
	 * @param totalAmount     The total order amount after adding shipping and
	 *                        subtracting discounts
	 * @return The ID of the newly created order
	 * @throws SQLException If a database error occurs
	 */
	int createOrder(int userId, List<CartItem> cartItems, String shippingAddress, BigDecimal subtotal,
			BigDecimal shippingCost, BigDecimal discount, BigDecimal totalAmount) throws SQLException;

	/**
	 * Retrieves an order by its ID
	 * 
	 * @param orderId The ID of the order to retrieve
	 * @return The order model, or null if not found
	 * @throws SQLException If a database error occurs
	 */
	OrderModel getOrderById(int orderId) throws SQLException;

	/**
	 * Retrieves all items for a specific order
	 * 
	 * @param orderId The ID of the order
	 * @return A list of order items
	 * @throws SQLException If a database error occurs
	 */
	List<OrderItemModel> getOrderItems(int orderId) throws SQLException;

	/**
	 * Updates the status of an order
	 * 
	 * @param orderId   The ID of the order to update
	 * @param newStatus The new order status
	 * @return true if the update was successful, false otherwise
	 * @throws SQLException If a database error occurs
	 */
	boolean updateOrderStatus(int orderId, String newStatus) throws SQLException;

	/**
	 * Retrieves all orders for a specific user
	 * 
	 * @param userId The ID of the user
	 * @return A list of orders placed by the user
	 * @throws SQLException If a database error occurs
	 */
	List<OrderModel> getOrdersByUserId(int userId) throws SQLException;
}