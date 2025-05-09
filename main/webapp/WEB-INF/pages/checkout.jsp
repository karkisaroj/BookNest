<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest - Checkout</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/checkout.css">
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="checkout-container">
		<h1>Checkout</h1>

		<!-- Display messages -->
		<c:if test="${not empty errorMessage}">
			<div class="error-message">${errorMessage}</div>
		</c:if>

		<div class="checkout-content">
			<div class="checkout-form">
				<h2>Shipping Information</h2>
				<form action="${pageContext.request.contextPath}/checkout"
					method="post">
					<div class="form-group">
						<label for="streetAddress">Street Address*</label> <input
							type="text" id="streetAddress" name="streetAddress" required>
					</div>

					<div class="form-group">
						<label for="city">City*</label> <input type="text" id="city"
							name="city" required>
					</div>

					<div class="form-row">
						<div class="form-group half">
							<label for="state">State/Province*</label> <input type="text"
								id="state" name="state" required>
						</div>

						<div class="form-group half">
							<label for="zipCode">Postal Code*</label> <input type="text"
								id="zipCode" name="zipCode" required>
						</div>
					</div>

					<div class="form-group">
						<label for="phone">Phone Number*</label> <input type="tel"
							id="phone" name="phone" required> <small>For
							delivery purposes only</small>
					</div>

					<h2 class="payment-title">Payment Information</h2>
					<div class="payment-methods">
						<div class="payment-method">
							<input type="radio" id="cashOnDelivery" name="paymentMethod"
								value="cod" checked> <label for="cashOnDelivery">Cash
								on Delivery</label>
						</div>
						<div class="payment-method">
							<input type="radio" id="onlinePayment" name="paymentMethod"
								value="online"> <label for="onlinePayment">Online
								Payment</label>
						</div>
					</div>

					<div class="order-summary">
						<h2>Order Summary</h2>
						<div class="summary-row">
							<span>Subtotal</span> <span>Rs. <fmt:formatNumber
									value="${cartTotal}" pattern="#,##0.00" /></span>
						</div>
						<div class="summary-row">
							<span>Shipping</span> <span>Rs. <fmt:formatNumber
									value="${shippingCost}" pattern="#,##0.00" /></span>
						</div>
						<div class="summary-row total">
							<span>Total</span> <span>Rs. <fmt:formatNumber
									value="${cartTotal + shippingCost}" pattern="#,##0.00" /></span>
						</div>
					</div>

					<div class="checkout-actions">
						<a href="${pageContext.request.contextPath}/cart"
							class="btn btn-secondary">Back to Cart</a>
						<button type="submit" class="btn btn-primary">Place Order</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp" />

	<script>
		// Form validation
		document
				.addEventListener(
						'DOMContentLoaded',
						function() {
							const checkoutForm = document.querySelector('form');
							checkoutForm
									.addEventListener(
											'submit',
											function(event) {
												const streetAddress = document
														.getElementById('streetAddress').value
														.trim();
												const city = document
														.getElementById('city').value
														.trim();
												const state = document
														.getElementById('state').value
														.trim();
												const zipCode = document
														.getElementById('zipCode').value
														.trim();
												const phone = document
														.getElementById('phone').value
														.trim();

												if (!streetAddress || !city
														|| !state || !zipCode
														|| !phone) {
													event.preventDefault();
													alert('Please fill in all required fields.');
													return false;
												}

												console
														.log("Form is valid, submitting...");
											});
						});
	</script>
</body>
</html>