<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest - Order Confirmation</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/order-confirmation.css">

</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="main-container">
		<div class="confirmation-container">
			<h1 class="confirmation-title">Order Confirmation</h1>

			<c:if test="${not empty successMessage}">
				<div class="success-message">${successMessage}</div>
			</c:if>

			<div class="order-details">
				<p>
					<strong>Order Number:</strong> <span id="orderNumber">#${orderId}</span>
				</p>
				<p>
					<strong>Order Date:</strong> <span id="orderDate">${not empty orderDate ? orderDate : 'Today'}</span>
				</p>
				<p>
					<strong>Status:</strong> <span id="orderStatus"
						class="status-confirmed">Confirmed</span>
				</p>

				<!-- Payment information display -->
				<div class="payment-info">
					<p>
						<strong>Payment Method:</strong> <span class="payment-method">${not empty paymentMethod ? paymentMethod : 'Cash on Delivery'}</span>
					</p>
					<p>
						<strong>Payment Status:</strong> <span class="payment-status">Completed</span>
					</p>
				</div>
			</div>

			<!-- Total display section -->
			<div class="total-box">
				<span class="total-label">Total:</span> <span class="total-amount">
					Rs. <c:choose>
						<c:when test="${empty orderTotal}">0.00</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${orderTotal}" pattern="#,##0.00" />
						</c:otherwise>
					</c:choose>
				</span>
			</div>

			<p class="thank-you-text">Thank you for your purchase! We've
				received your order and will begin processing it right away.</p>

			<div class="action-buttons">
				<a href="${pageContext.request.contextPath}/books"
					class="btn btn-primary">Continue Shopping</a> <a
					href="${pageContext.request.contextPath}/home"
					class="btn btn-success">Back to Home</a>
			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp" />
</body>
</html>