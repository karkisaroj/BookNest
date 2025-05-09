<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BookNest - Order Confirmed</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/checkout.css">
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f8f9fa;
}

.confirmation-container {
	max-width: 800px;
	margin: 50px auto;
	padding: 30px;
	background-color: #fff;
	box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
	border-radius: 5px;
	text-align: center;
}

.confirmation-title {
	color: #4CAF50;
	margin-bottom: 20px;
}

.order-number {
	font-size: 1.2em;
	margin-bottom: 30px;
	padding: 15px;
	background-color: #f8f9fa;
	border-radius: 5px;
}

.btn {
	display: inline-block;
	padding: 10px 20px;
	margin: 0 10px;
	border-radius: 4px;
	text-decoration: none;
	font-weight: bold;
	color: white;
	background-color: #4CAF50;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/pages/header.jsp" />

	<div class="confirmation-container">
		<h1 class="confirmation-title">Order Confirmed!</h1>

		<div class="success-message">Order placed successfully! Thank
			you for your purchase.</div>

		<div class="order-number">
			<p>
				Order #<%=request.getParameter("orderId") != null ? request.getParameter("orderId") : "12345"%>
				has been placed successfully.
			</p>
		</div>

		<p>
			Total Amount: Rs.
			<%=request.getParameter("total") != null ? request.getParameter("total") : "0.00"%></p>
		<p>
			Date:
			<%=request.getParameter("date") != null ? request.getParameter("date") : "Today"%></p>

		<p>Your books will be delivered within 48 hours.</p>

		<div class="action-buttons">
			<a href="${pageContext.request.contextPath}/books" class="btn">Continue
				Shopping</a>
		</div>
	</div>

	<jsp:include page="/WEB-INF/pages/footer.jsp" />
</body>
</html>