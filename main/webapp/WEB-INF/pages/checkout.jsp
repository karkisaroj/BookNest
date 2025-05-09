<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest - Checkout</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/checkout.css">
</head>
<body>
	<jsp:include page="header.jsp" />

	<%
	// Ensure user is logged in
	if (session.getAttribute("userID") == null) {
		response.sendRedirect(request.getContextPath() + "/login");
		return;
	}
	%>

	<div class="main-content">
		<!-- Display any error messages -->
		<c:if test="${not empty formError}">
			<div class="error-message">${formError}</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/checkout"
			method="post">
			<div class="shipping-section">
				<h1>Checkout</h1>

				<h2>Shipping Information</h2>

				<div class="delivery-options">
					<label class="delivery-option"> <input type="radio"
						name="deliveryType" value="delivery" checked> <span>Delivery</span>
					</label> <label class="delivery-option"> <input type="radio"
						name="deliveryType" value="pickup"> <span>Pick Up</span>
					</label>
				</div>

				<div class="form-group">
					<label>First name *</label> <input type="text" name="firstName"
						class="form-control" placeholder="Enter your first name"
						value="${userName}" required>
				</div>

				<div class="form-group">
					<label>Email Address</label> <input type="email" name="email"
						class="form-control" placeholder="Enter your email address"
						value="${userEmail}">
				</div>

				<div class="form-group">
					<label>Phone Number *</label> <input type="tel" name="phone"
						class="form-control" placeholder="Enter your phone number"
						required>
				</div>

				<div class="form-group">
					<label>Country *</label> <input type="text" name="country"
						class="form-control" placeholder="Enter your country name"
						value="Nepal" required>
				</div>

				<div class="terms-checkbox">
					<input type="checkbox" id="terms" name="terms" required> <label
						for="terms">I have read and agree to the Terms and
						Conditions.</label>
				</div>
			</div>

			<div class="review-section">
				<h2>Review Your Cart</h2>

				<c:forEach var="item" items="${cartItems}">
					<div class="cart-item">
						<c:choose>
							<c:when test="${not empty item.bookModel.book_img_url}">
								<c:choose>
									<c:when
										test="${item.bookModel.book_img_url.startsWith('resources/')}">
										<img
											src="${pageContext.request.contextPath}/${item.bookModel.book_img_url}"
											alt="${item.bookModel.book_title}">
									</c:when>
									<c:otherwise>
										<img
											src="${pageContext.request.contextPath}/${item.bookModel.book_img_url}"
											alt="${item.bookModel.book_title}">
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<img
									src="${pageContext.request.contextPath}/resources/images/system/placeholder.png"
									alt="No image available">
							</c:otherwise>
						</c:choose>

						<div class="cart-item-details">
							<p>${item.bookModel.book_title}</p>
							<p>${item.quantity}x</p>
							<p>
								Rs.
								<fmt:formatNumber value="${item.bookModel.price}"
									pattern="#,##0.00" />
							</p>
						</div>
					</div>
				</c:forEach>

				<div class="price-summary">
					<div class="price-row">
						<div>Subtotal</div>
						<div>
							Rs.
							<fmt:formatNumber value="${subtotal}" pattern="#,##0.00" />
						</div>
					</div>
					<div class="price-row">
						<div>Shipping</div>
						<div>
							Rs.
							<fmt:formatNumber value="${shipping}" pattern="#,##0.00" />
						</div>
					</div>
					<div class="price-row">
						<div>Discount</div>
						<div>
							Rs.
							<fmt:formatNumber value="${discount}" pattern="#,##0.00" />
						</div>
					</div>
					<div class="price-row total-row">
						<div>Total</div>
						<div>
							Rs.
							<fmt:formatNumber value="${total}" pattern="#,##0.00" />
						</div>
					</div>
				</div>

				<button type="submit" class="btn btn-primary">Pay Now</button>
				<a href="${pageContext.request.contextPath}/cart"
					class="btn btn-secondary">Back to Cart</a>
			</div>
		</form>
	</div>

	<jsp:include page="footer.jsp" />

	<script>
		document
				.addEventListener(
						'DOMContentLoaded',
						function() {
							// Debug information
							console.log('Checkout page loaded');
							console.log('Cart items: ${cartItems.size()}');
							console.log('Total: ${total}');

							// Form validation enhancement
							const form = document.querySelector('form');
							form
									.addEventListener(
											'submit',
											function(e) {
												const phone = document
														.querySelector('input[name="phone"]').value;
												if (phone.trim().length < 6) {
													e.preventDefault();
													alert('Please enter a valid phone number');
												}

												const terms = document
														.querySelector('#terms').checked;
												if (!terms) {
													e.preventDefault();
													alert('You must agree to the Terms and Conditions');
												}
											});
						});
	</script>
</body>
</html>