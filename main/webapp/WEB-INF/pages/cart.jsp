<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>Your Cart</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/cart.css" />

</head>
<body>
	<jsp:include page="header.jsp" />
	<%
	// Retrieve user information from session
	String username = (String) session.getAttribute("userName");

	// Checking if user is logged in
	if (username == null) {
		// If not logged in, redirecting to login page
		response.sendRedirect(request.getContextPath() + "/login");
		return; // Important to stop further processing
	}
	%>
	<div class="container1">
		<div class="cart-heading">
			<h1>Your cart</h1>
		</div>

		<div id="message-container">
			<!-- Display messages from session - Use correct attribute names -->
			<c:if test="${not empty sessionScope.flashSuccessMessage}">
				<div class="success-message">
					<%-- Use appropriate CSS class --%>
					${sessionScope.flashSuccessMessage}
				</div>
				<%
				session.removeAttribute("flashSuccessMessage");
				%>
			</c:if>
			<c:if test="${not empty sessionScope.flashErrorMessage}">
				<div class="error-message">
					<%-- Use appropriate CSS class --%>
					${sessionScope.flashErrorMessage}
				</div>
				<%
				session.removeAttribute("flashErrorMessage");
				%>
			</c:if>
			<%-- Display error message set directly on request by doGet --%>
			<c:if test="${not empty errorMessage}">
				<div class="error-message">${errorMessage}</div>
			</c:if>
		</div>

		<table class="cart-table">
			<thead>
				<tr>
					<th>Product</th>
					<th>Quantity</th>
					<th>Total</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody id="cart-items">
				<c:choose>
					<c:when test="${empty cartItems}">
						<tr>
							<td colspan="4" class="empty-cart">Your cart is empty</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${cartItems}" var="item">
							<%-- Check if bookModel is not null before accessing its properties --%>
							<c:if test="${not empty item.bookModel}">
								<tr id="item-${item.cartItemId}">
									<td>
										<div class="book-item">
											<%-- UPDATED: Use the same image path handling logic as in home.jsp --%>
											<c:choose>
												<c:when test="${not empty item.bookModel.bookImgUrl}">
													<c:choose>
														<c:when
															test="${item.bookModel.bookImgUrl.startsWith('resources/')}">
															<img
																src="${pageContext.request.contextPath}/${item.bookModel.bookImgUrl}"
																alt="<c:out value='${item.bookModel.bookTitle}'/>"
																class="book-image">
														</c:when>
														<c:otherwise>
															<img
																src="${pageContext.request.contextPath}${item.bookModel.bookImgUrl}"
																alt="<c:out value='${item.bookModel.bookTitle}'/>"
																class="book-image">
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<img
														src="${pageContext.request.contextPath}/resources/images/system/placeholder.png"
														alt="No image available" class="book-image">
												</c:otherwise>
											</c:choose>
											<div class="book-details">
												<%-- Access properties via item.bookModel --%>
												<%-- Display Author Name fetched by the service --%>
												<h3>${item.bookModel.authorName}</h3>
												<p>${item.bookModel.bookTitle}</p>
												<div class="price">
													Rs.
													<fmt:formatNumber value="${item.bookModel.price}"
														pattern="#,##0.00" />
												</div>
											</div>
										</div>
									</td>
									<td>
										<%-- Display quantity - Add update form later if needed --%>
										${item.quantity}
									</td>
									<td>Rs.<fmt:formatNumber
											value="${item.bookModel.price * item.quantity}"
											pattern="#,##0.00" /></td>
									<td>
										<%-- Corrected Remove Form --%>
										<form action="${pageContext.request.contextPath}/cart"
											method="post" style="display: inline;">
											<input type="hidden" name="action" value="remove"> <input
												type="hidden" name="cartItemId" value="${item.cartItemId}">
											<button type="submit" class="remove-btn">REMOVE</button>
										</form>
									</td>
								</tr>
							</c:if>
							<%-- Optional: Add an else block here if item.bookModel is null, indicating a data issue --%>
							<c:if test="${empty item.bookModel}">
								<tr>
									<td colspan="4" class="error-message">Error loading book
										details for an item.</td>
								</tr>
							</c:if>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

		<a href="${pageContext.request.contextPath}/books"
			class="continue-shopping">Continue Shopping →</a>

		<div class="cart-summary">
			<div class="total" id="cart-total">
				Estimated total: <strong>Rs.<fmt:formatNumber
						value="${cartTotal}" pattern="#,##0.00" /></strong>
			</div>
			<div class="taxes">Taxes, discounts and shipping calculated at
				checkout.</div>
			<c:if test="${!empty cartItems}">
				<a href="${pageContext.request.contextPath}/checkout"><button
						class="checkout-btn">Check Out</button></a>
			</c:if>
		</div>
	</div>

	<jsp:include page="footer.jsp" />

	<!-- Added debugging JavaScript to help identify image loading issues -->
	<script>
		document.addEventListener('DOMContentLoaded', function() {
			// Debug image paths
			console.log('--- Cart Image Path Debugging ---');
			document.querySelectorAll('.book-item img').forEach(
					function(img, index) {
						console.log('Cart item ' + index + ' image path: '
								+ img.src);
						// Add error handler
						img.onerror = function() {
							console.error('Failed to load image: ' + this.src);
						};
					});
		});
	</script>
</body>
</html>