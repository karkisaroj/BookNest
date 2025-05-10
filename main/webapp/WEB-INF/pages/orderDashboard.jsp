<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest Admin - Orders</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/orderDashboard.css">
</head>

<body>
	<jsp:include page="header.jsp" />

	<div class="admin-dashboard-container">
		<jsp:include page="dashboard.jsp" />
		<div class="main-content">
			<div class="header">
				<h1>Orders</h1>
			</div>

			<!-- Display Success or Error Messages -->
			<div class="message-container">
				<c:if test="${not empty success}">
					<div class="success-message">${success}</div>
				</c:if>
				<c:if test="${not empty error}">
					<div class="error-message">${error}</div>
				</c:if>
			</div>

			<div class="orders-table-container">
				<h2>Order List</h2>
				<table class="orders-table">
					<thead>
						<tr>
							<th class="checkbox-column"><input type="checkbox"></th>
							<th>Order ID</th>
							<th>User ID</th>
							<th>Order Date</th>
							<th>Shipping Address</th>
							<th>Total Amount</th>
							<th>Order Status</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty orders}">
								<c:forEach var="order" items="${orders}">
									<tr>
										<td class="checkbox-cell"><input type="checkbox"></td>
										<td class="order-id">${order.orderID}</td>
										<td class="user-id">${order.userID}</td>
										<td class="order-date"><fmt:formatDate
												value="${order.orderDate}" pattern="yyyy-MM-dd" /></td>
										<td class="shipping-address">${order.shippingAddress}</td>
										<td class="total-amount">${order.totalAmount}</td>
										<td class="status-cell"><span
											class="order-status 
												${order.orderStatus eq 'completed' ? 'status-completed' : 
												order.orderStatus eq 'in progress' ? 'status-in-progress' : 'status-pending'}">
												${order.orderStatus} </span></td>
										<td class="action-cell">
											<form method="post"
												action="${pageContext.request.contextPath}/adminorder">
												<input type="hidden" name="orderId" value="${order.orderID}" />
												<button type="submit" name="action" value="changeStatus"
													class="status-btn">Change Status</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="8" class="no-orders">No orders found</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp" />
</body>
</html>