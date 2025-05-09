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
	href="${pageContext.request.contextPath}/css/styles.css">
<style>
.confirmation-container {
	max-width: 800px;
	margin: 50px auto;
	padding: 30px;
	background-color: #fff;
	box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
	border-radius: 5px;
}

.confirmation-title {
	color: #28a745;
	text-align: center;
	margin-bottom: 30px;
}

.success-message {
	background-color: #d4edda;
	color: #155724;
	border: 1px solid #c3e6cb;
	border-radius: 4px;
	padding: 15px;
	margin-bottom: 20px;
}

.order-details {
	background-color: #f8f9fa;
	border-left: 5px solid #28a745;
	padding: 20px;
	margin: 20px 0;
}

.total-box {
	font-size: 18px;
	font-weight: bold;
	text-align: right;
	margin-top: 20px;
}

.action-buttons {
	text-align: center;
	margin-top: 30px;
}

.btn {
	display: inline-block;
	padding: 10px 20px;
	margin: 0 10px;
	text-decoration: none;
	color: white;
	border-radius: 4px;
	font-weight: bold;
}

.btn-primary {
	background-color: #007bff;
}

.btn-success {
	background-color: #28a745;
}
</style>
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
					<strong>Order Number:</strong> #${orderId}
				</p>
				<p>
					<strong>Order Date:</strong> ${not empty orderDate ? orderDate : 'Today'}
				</p>
				<p>
					<strong>Status:</strong> Confirmed
				</p>
			</div>

			<div class="total-box">
				<p>
					Total: Rs.
					<fmt:formatNumber value="${orderTotal}" pattern="#,##0.00" />
				</p>
			</div>

			<p>Thank you for your purchase! We've received your order and
				will begin processing it right away.</p>

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