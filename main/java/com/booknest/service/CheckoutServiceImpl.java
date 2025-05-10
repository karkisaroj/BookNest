package com.booknest.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.booknest.model.CartItem;

/**
 * Implementation of the CheckoutService interface
 */
public class CheckoutServiceImpl implements CheckoutService {

	private static final BigDecimal DEFAULT_SHIPPING_COST = new BigDecimal("100.00");
	private final OrderService orderService;
	private final CartService cartService;

	public CheckoutServiceImpl() {
		this.orderService = new OrderServiceImpl();
		this.cartService = new CartServiceImpl();
	}

	/**
	 * Constructor with dependency injection for testing
	 */
	public CheckoutServiceImpl(OrderService orderService, CartService cartService) {
		this.orderService = orderService;
		this.cartService = cartService;
	}

	@Override
	public int processCheckout(Integer userId, List<CartItem> cartItems, String streetAddress, String city,
			String state, String zipCode, String phone) throws SQLException {

		// Validate inputs
		if (userId == null || cartItems == null || cartItems.isEmpty()
				|| !validateAddressInfo(streetAddress, city, state, zipCode, phone)) {
			return -1;
		}

		// Format address
		String formattedAddress = formatAddress(streetAddress, city, state, zipCode);

		// Calculate costs
		BigDecimal subtotal = calculateSubtotal(cartItems);
		BigDecimal shippingCost = getShippingCost();
		BigDecimal discount = BigDecimal.ZERO; // Implement discount logic if needed
		BigDecimal totalAmount = subtotal.add(shippingCost).subtract(discount);

		// Create the order
		return orderService.createOrder(userId, cartItems, formattedAddress, subtotal, shippingCost, discount,
				totalAmount);
	}

	@Override
	public boolean validateAddressInfo(String streetAddress, String city, String state, String zipCode, String phone) {
		return streetAddress != null && city != null && state != null && zipCode != null && phone != null
				&& !streetAddress.trim().isEmpty() && !city.trim().isEmpty() && !state.trim().isEmpty()
				&& !zipCode.trim().isEmpty() && !phone.trim().isEmpty();
	}

	@Override
	public BigDecimal calculateOrderTotal(List<CartItem> cartItems) {
		BigDecimal subtotal = calculateSubtotal(cartItems);
		BigDecimal shippingCost = getShippingCost();
		// Currently no discount logic, but could be added here
		BigDecimal discount = BigDecimal.ZERO;

		return subtotal.add(shippingCost).subtract(discount);
	}

	@Override
	public BigDecimal calculateSubtotal(List<CartItem> cartItems) {
		BigDecimal subtotal = BigDecimal.ZERO;

		for (CartItem item : cartItems) {
			BigDecimal itemPrice = item.getBookModel().getPrice();
			BigDecimal itemTotal = itemPrice.multiply(new BigDecimal(item.getQuantity()));
			subtotal = subtotal.add(itemTotal);
		}

		return subtotal;
	}

	@Override
	public BigDecimal getShippingCost() {
		return DEFAULT_SHIPPING_COST;
	}

	@Override
	public String formatAddress(String streetAddress, String city, String state, String zipCode) {
		// Format: Street, City, State/Province Postal Code
		return streetAddress + ", " + city + ", " + state + " " + zipCode;
	}
}