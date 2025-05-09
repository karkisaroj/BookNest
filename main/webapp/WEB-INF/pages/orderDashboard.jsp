<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
				<%
				if (request.getAttribute("success") != null) {
				%>
				<div class="success-message">
					<%=request.getAttribute("success")%>
				</div>
				<%
				} else if (request.getAttribute("error") != null) {
				%>
				<div class="error-message">
					<%=request.getAttribute("error")%>
				</div>
				<%
				}
				%>
			</div>

			<div class="orders-table-container">
				<h2>Order List</h2>
				<table>
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
										<td><input type="checkbox"></td>
										<td>${order.orderID}</td>
										<td>${order.userID}</td>
										<td>${order.orderDate}</td>
										<td class="shipping-address">${order.shippingAddress}</td>
										<td>${order.totalAmount}</td>
										<td class="order-status">${order.orderStatus}</td>
										<td>
											<form method="post"
												action="${pageContext.request.contextPath}/adminorder"
												style="display: inline;">
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
									<td colspan="8" style="text-align: center;">No orders
										found</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp" />

	<script>
		/**
		 * Apply colors to the order status dynamically
		 */
		document.addEventListener("DOMContentLoaded", () => {
			// Get all elements with the "order-status" class
			const statusElements = document.querySelectorAll(".order-status");

			// Loop through each status element and apply the corresponding class
			statusElements.forEach((element) => {
				const status = element.textContent.trim().toLowerCase();

				if (status === "in progress") {
					element.classList.add("status-in-progress");
				} else if (status === "completed") {
					element.classList.add("status-completed");
				} else if (status === "pending") {
					element.classList.add("status-pending");
				}
			});
		});
	</script>
</body>

</html>