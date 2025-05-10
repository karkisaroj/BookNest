package com.booknest.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.booknest.model.CartItem;

/**
 * Service interface for handling checkout operations
 */
public interface CheckoutService {

	/**
	 * Processes a checkout operation for a user
	 * 
	 * @param userId        User ID making the purchase
	 * @param cartItems     Items in the user's cart
	 * @param streetAddress Street address for delivery
	 * @param city          City for delivery
	 * @param state         State for delivery
	 * @param zipCode       ZIP/Postal code for delivery
	 * @param phone         Phone number for contact
	 * @return Order ID if successful, -1 if failed
	 * @throws SQLException If a database error occurs
	 */
	int processCheckout(Integer userId, List<CartItem> cartItems, String streetAddress, String city, String state,
			String zipCode, String phone) throws SQLException;

	/**
	 * Validates the address information
	 * 
	 * @param streetAddress Street address for delivery
	 * @param city          City for delivery
	 * @param state         State for delivery
	 * @param zipCode       ZIP/Postal code for delivery
	 * @param phone         Phone number for contact
	 * @return true if valid, false otherwise
	 */
	boolean validateAddressInfo(String streetAddress, String city, String state, String zipCode, String phone);

	/**
	 * Calculates the total cost of the order
	 * 
	 * @param cartItems The items in the cart
	 * @return The total order cost including shipping
	 */
	BigDecimal calculateOrderTotal(List<CartItem> cartItems);

	/**
	 * Calculates the subtotal of all items in cart
	 * 
	 * @param cartItems The items in the cart
	 * @return The subtotal before shipping and discounts
	 */
	BigDecimal calculateSubtotal(List<CartItem> cartItems);

	/**
	 * Returns the standard shipping cost
	 * 
	 * @return The shipping cost
	 */
	BigDecimal getShippingCost();

	/**
	 * Formats the address in a standard format
	 * 
	 * @param streetAddress Street address
	 * @param city          City
	 * @param state         State
	 * @param zipCode       ZIP/Postal code
	 * @return Formatted address string
	 */
	String formatAddress(String streetAddress, String city, String state, String zipCode);

	/**
	 * Creates a payment record for a completed order
	 * 
	 * @param orderId       Order ID to associate the payment with
	 * @param amount        Payment amount
	 * @param paymentMethod Method of payment (e.g., "Cash on Delivery", "Online
	 *                      Payment")
	 * @return Payment ID if successful, -1 if failed
	 * @throws SQLException If a database error occurs
	 */
	int createPaymentRecord(int orderId, BigDecimal amount, String paymentMethod) throws SQLException;

	/**
	 * Formats the payment method from form input to standardized value
	 * 
	 * @param paymentMethodParam Raw payment method from form
	 * @return Formatted payment method string
	 */
	String formatPaymentMethod(String paymentMethodParam);
}