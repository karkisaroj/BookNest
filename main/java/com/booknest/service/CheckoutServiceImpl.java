package com.booknest.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.booknest.model.CartItem;

/**
 * Implementation of the CheckoutService interface
 */
public class CheckoutServiceImpl implements CheckoutService {

	// Define this as a static final constant
	private static final BigDecimal DEFAULT_SHIPPING_COST = new BigDecimal("100.00");
	private final OrderService orderService;
	private final CartService cartService;
	private final PaymentService paymentService;

	// Payment method constants
	private static final String PAYMENT_METHOD_COD = "Cash on Delivery";
	private static final String PAYMENT_METHOD_ONLINE = "Online Payment";

	// Payment status constant
	private static final String PAYMENT_STATUS_COMPLETED = "Completed";

	public CheckoutServiceImpl() {
		this.orderService = new OrderServiceImpl();
		this.cartService = new CartServiceImpl();
		this.paymentService = new PaymentServiceImpl();
	}

	/**
	 * Constructor with dependency injection for testing
	 */
	public CheckoutServiceImpl(OrderService orderService, CartService cartService, PaymentService paymentService) {
		this.orderService = orderService;
		this.cartService = cartService;
		this.paymentService = paymentService;
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
		BigDecimal shippingCost = DEFAULT_SHIPPING_COST; // Use constant directly to avoid potential recursion
		BigDecimal discount = BigDecimal.ZERO; // Implement discount logic if needed
		BigDecimal totalAmount = subtotal.add(shippingCost).subtract(discount);

		// Create the order
		int orderId = orderService.createOrder(userId, cartItems, formattedAddress, subtotal, shippingCost, discount,
				totalAmount);

		// If order was created successfully, clear the cart
		if (orderId > 0) {
			try {
				cartService.clearCart(userId);
				System.out.println("Cart cleared for user ID: " + userId);
			} catch (CartServiceException e) {
				System.err.println("Failed to clear cart: " + e.getMessage());
				// Don't fail the checkout process if cart clearing fails
			}
		}

		return orderId;
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
		// Use the constant directly instead of calling getShippingCost() to avoid
		// potential recursion
		BigDecimal shippingCost = DEFAULT_SHIPPING_COST;
		BigDecimal discount = BigDecimal.ZERO;

		return subtotal.add(shippingCost).subtract(discount);
	}

	@Override
	public BigDecimal calculateSubtotal(List<CartItem> cartItems) {
		BigDecimal subtotal = BigDecimal.ZERO;

		for (CartItem item : cartItems) {
			if (item.getBookModel() != null && item.getBookModel().getPrice() != null) {
				BigDecimal itemPrice = item.getBookModel().getPrice();
				BigDecimal itemTotal = itemPrice.multiply(new BigDecimal(item.getQuantity()));
				subtotal = subtotal.add(itemTotal);
			}
		}

		return subtotal;
	}

	@Override
	public BigDecimal getShippingCost() {
		// Simply return the constant, do not call this method recursively!
		return DEFAULT_SHIPPING_COST;
	}

	@Override
	public String formatAddress(String streetAddress, String city, String state, String zipCode) {
		// Format: Street, City, State/Province Postal Code
		return streetAddress + ", " + city + ", " + state + " " + zipCode;
	}

	@Override
	public int createPaymentRecord(int orderId, BigDecimal amount, String paymentMethod) throws SQLException {
		// Always use Completed status
		String paymentStatus = PAYMENT_STATUS_COMPLETED;

		System.out.println("Creating payment record for order ID: " + orderId + " with amount: " + amount + ", method: "
				+ paymentMethod + ", status: " + paymentStatus);

		try {
			// Create payment using the payment service
			int paymentId = paymentService.createPayment(orderId, amount, paymentMethod, paymentStatus);

			System.out.println("Payment record created with ID: " + paymentId);
			return paymentId;
		} catch (SQLException e) {
			System.err.println("Failed to create payment record: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public String formatPaymentMethod(String paymentMethodParam) {
		if (paymentMethodParam == null) {
			return PAYMENT_METHOD_COD; // Default
		}

		switch (paymentMethodParam.toLowerCase()) {
		case "cod":
			return PAYMENT_METHOD_COD;
		case "online":
			return PAYMENT_METHOD_ONLINE;
		default:
			return paymentMethodParam;
		}
	}
}