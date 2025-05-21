package com.booknest.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.booknest.model.CartItem;

/**
 * Implementation of the CheckoutService interface that handles the checkout
 * process including order creation, payment processing, and cart management.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class CheckoutServiceImpl implements CheckoutService {

	// Service instances
	private final OrderService orderService;
	private final CartService cartService;
	private final PaymentService paymentService;

	// Cost constants
	private static final BigDecimal DEFAULT_SHIPPING_COST = new BigDecimal("100.00");
	private static final BigDecimal ZERO_DISCOUNT = BigDecimal.ZERO;

	// Payment method constants
	private static final String PAYMENT_METHOD_COD = "Cash on Delivery";
	private static final String PAYMENT_METHOD_ONLINE = "Online Payment";

	// Payment parameters
	private static final String PARAM_COD = "cod";
	private static final String PARAM_ONLINE = "online";

	// Payment status constant
	private static final String PAYMENT_STATUS_COMPLETED = "Completed";

	// Error message constants
	private static final String ERROR_CHECKOUT_FAILED = "Checkout failed: Invalid input parameters.";
	private static final String ERROR_CART_CLEAR_FAILED = "Failed to clear cart: %s";
	private static final String ERROR_PAYMENT_CREATION_FAILED = "Failed to create payment record: %s";

	/**
	 * Default constructor that initializes required services.
	 */
	public CheckoutServiceImpl() {
		this.orderService = new OrderServiceImpl();
		this.cartService = new CartServiceImpl();
		this.paymentService = new PaymentServiceImpl();
	}

	/**
	 * Constructor with dependency injection for testing.
	 * 
	 * @param orderService   The order service to use
	 * @param cartService    The cart service to use
	 * @param paymentService The payment service to use
	 */
	public CheckoutServiceImpl(OrderService orderService, CartService cartService, PaymentService paymentService) {
		this.orderService = orderService;
		this.cartService = cartService;
		this.paymentService = paymentService;
	}

	/**
	 * Processes a checkout by creating an order and clearing the cart.
	 * 
	 * @param userId        The ID of the user checking out
	 * @param cartItems     The items in the cart
	 * @param streetAddress The street address for delivery
	 * @param city          The city for delivery
	 * @param state         The state for delivery
	 * @param zipCode       The zip code for delivery
	 * @param phone         The contact phone number
	 * @param paymentMethod The payment method to use (e.g., "cod", "online")
	 * @return The ID of the created order, or -1 if checkout failed
	 * @throws SQLException If there's a database error
	 */
	@Override
	public int processCheckout(Integer userId, List<CartItem> cartItems, String streetAddress, String city,
			String state, String zipCode, String phone) throws SQLException {
		return processCheckout(userId, cartItems, streetAddress, city, state, zipCode, phone, PARAM_COD);
	}

	/**
	 * Processes a checkout with specified payment method.
	 * 
	 * @param userId        The ID of the user checking out
	 * @param cartItems     The items in the cart
	 * @param streetAddress The street address for delivery
	 * @param city          The city for delivery
	 * @param state         The state for delivery
	 * @param zipCode       The zip code for delivery
	 * @param phone         The contact phone number
	 * @param paymentMethod The payment method to use (e.g., "cod", "online")
	 * @return The ID of the created order, or -1 if checkout failed
	 * @throws SQLException If there's a database error
	 */
	public int processCheckout(Integer userId, List<CartItem> cartItems, String streetAddress, String city,
			String state, String zipCode, String phone, String paymentMethod) throws SQLException {

		// Validate inputs
		if (userId == null || cartItems == null || cartItems.isEmpty()
				|| !validateAddressInfo(streetAddress, city, state, zipCode, phone)) {
			System.err.println(ERROR_CHECKOUT_FAILED);
			return -1;
		}

		// Format address
		String formattedAddress = formatAddress(streetAddress, city, state, zipCode);

		// Calculate costs
		BigDecimal subtotal = calculateSubtotal(cartItems);
		BigDecimal shippingCost = DEFAULT_SHIPPING_COST;
		BigDecimal discount = ZERO_DISCOUNT; // Implement discount logic if needed
		BigDecimal totalAmount = subtotal.add(shippingCost).subtract(discount);

		int orderId = -1;
		try {
			// Create the order
			orderId = orderService.createOrder(userId, cartItems, formattedAddress, subtotal, shippingCost, discount,
					totalAmount);

			// If order was created successfully
			if (orderId > 0) {
				// Clear the cart
				try {
					cartService.clearCart(userId);

				} catch (CartServiceException e) {
					System.err.println(String.format(ERROR_CART_CLEAR_FAILED, e.getMessage()));
					// Don't fail the checkout process if cart clearing fails
				}
			}
		} catch (SQLException e) {
			System.err.println("Error during checkout process: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return orderId;
	}

	/**
	 * Validates that all address information is present and non-empty.
	 * 
	 * @param streetAddress The street address
	 * @param city          The city
	 * @param state         The state or province
	 * @param zipCode       The zip or postal code
	 * @param phone         The phone number
	 * @return true if all information is valid, false otherwise
	 */
	@Override
	public boolean validateAddressInfo(String streetAddress, String city, String state, String zipCode, String phone) {
		return streetAddress != null && city != null && state != null && zipCode != null && phone != null
				&& !streetAddress.trim().isEmpty() && !city.trim().isEmpty() && !state.trim().isEmpty()
				&& !zipCode.trim().isEmpty() && !phone.trim().isEmpty();
	}

	/**
	 * Calculates the total cost of the order including shipping and discounts.
	 * 
	 * @param cartItems The items in the cart
	 * @return The total order cost
	 */
	@Override
	public BigDecimal calculateOrderTotal(List<CartItem> cartItems) {
		BigDecimal subtotal = calculateSubtotal(cartItems);
		BigDecimal shippingCost = DEFAULT_SHIPPING_COST;
		BigDecimal discount = ZERO_DISCOUNT;

		return subtotal.add(shippingCost).subtract(discount);
	}

	/**
	 * Calculates the subtotal of all items in the cart.
	 * 
	 * @param cartItems The items in the cart
	 * @return The subtotal cost
	 */
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

	/**
	 * Returns the standard shipping cost.
	 * 
	 * @return The shipping cost
	 */
	@Override
	public BigDecimal getShippingCost() {
		return DEFAULT_SHIPPING_COST;
	}

	/**
	 * Formats the address components into a single string.
	 * 
	 * @param streetAddress The street address
	 * @param city          The city
	 * @param state         The state or province
	 * @param zipCode       The zip or postal code
	 * @return The formatted address string
	 */
	@Override
	public String formatAddress(String streetAddress, String city, String state, String zipCode) {
		// Format: Street, City, State/Province Postal Code
		return streetAddress + ", " + city + ", " + state + " " + zipCode;
	}

	/**
	 * Creates a payment record for an order.
	 * 
	 * @param orderId       The ID of the order
	 * @param amount        The payment amount
	 * @param paymentMethod The payment method used
	 * @return The ID of the created payment record
	 * @throws SQLException If there's a database error
	 */
	@Override
	public int createPaymentRecord(int orderId, BigDecimal amount, String paymentMethod) throws SQLException {
		// Always use Completed status
		String paymentStatus = PAYMENT_STATUS_COMPLETED;

		try {
			// Create payment using the payment service
			int paymentId = paymentService.createPayment(orderId, amount, paymentMethod, paymentStatus);

			return paymentId;
		} catch (SQLException e) {
			System.err.println(String.format(ERROR_PAYMENT_CREATION_FAILED, e.getMessage()));
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Formats the payment method parameter to a standard format.
	 * 
	 * @param paymentMethodParam The raw payment method string
	 * @return The formatted payment method string
	 */
	@Override
	public String formatPaymentMethod(String paymentMethodParam) {
		if (paymentMethodParam == null) {
			return PAYMENT_METHOD_COD; // Default
		}

		switch (paymentMethodParam.toLowerCase()) {
		case PARAM_COD:
			return PAYMENT_METHOD_COD;
		case PARAM_ONLINE:
			return PAYMENT_METHOD_ONLINE;
		default:
			return paymentMethodParam;
		}
	}
}