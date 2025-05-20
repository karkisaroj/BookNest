<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.booknest.util.SessionUtil"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Search Results - BookNest</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/search-result.css">

</head>
<body>
	<jsp:include page="header.jsp" />

	<!-- Add the hidden iframe here -->
	<iframe name="hidden-iframe" style="display: none;"></iframe>

	<div class="container">
		<!-- Add notification message bar -->
		<div id="notification" class="notification" style="display: none;">Item
			added to cart!</div>

		<h1>Book Search</h1>

		<!-- Flash Messages -->
		<c:if test="${not empty sessionScope.flashSuccessMessage}">
			<div class="alert alert-success">
				<c:out value="${sessionScope.flashSuccessMessage}" />
			</div>
			<%
			session.removeAttribute("flashSuccessMessage");
			%>
		</c:if>
		<c:if test="${not empty sessionScope.flashErrorMessage}">
			<div class="alert alert-danger">
				<c:out value="${sessionScope.flashErrorMessage}" />
			</div>
			<%
			session.removeAttribute("flashErrorMessage");
			%>
		</c:if>
		<c:if test="${not empty error}">
			<div class="alert alert-danger">
				<c:out value="${error}" />
			</div>
		</c:if>
		<c:if test="${not empty param.cartError}">
			<div class="alert alert-danger">Failed to add book to cart.
				Please try again.</div>
		</c:if>

		<!-- Search Form -->
		<form action="${pageContext.request.contextPath}/search" method="GET"
			class="search-form">
			<input type="text" name="query" value="${searchQuery}"
				placeholder="Search for products..." required>
			<button type="submit">Search</button>
		</form>

		<!-- Category Filters -->
		<div class="category-section">
			<h3>Popular Categories</h3>
			<div class="category-list">
				<!-- Use hardcoded category buttons since there's an error with description column -->
				<a href="${pageContext.request.contextPath}/search?categoryId=1"
					class="category-link ${selectedCategoryId == 1 ? 'active' : ''}">Fiction</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=2"
					class="category-link ${selectedCategoryId == 2 ? 'active' : ''}">Non-Fiction</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=3"
					class="category-link ${selectedCategoryId == 3 ? 'active' : ''}">Science</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=4"
					class="category-link ${selectedCategoryId == 4 ? 'active' : ''}">History</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=5"
					class="category-link ${selectedCategoryId == 5 ? 'active' : ''}">Biography</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=6"
					class="category-link ${selectedCategoryId == 6 ? 'active' : ''}">Fantasy</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=7"
					class="category-link ${selectedCategoryId == 7 ? 'active' : ''}">Self-Help</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=8"
					class="category-link ${selectedCategoryId == 8 ? 'active' : ''}">Technology</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=9"
					class="category-link ${selectedCategoryId == 9 ? 'active' : ''}">Business</a>
				<a href="${pageContext.request.contextPath}/search?categoryId=10"
					class="category-link ${selectedCategoryId == 10 ? 'active' : ''}">Education</a>
			</div>
		</div>

		<!-- Search Results -->
		<c:if test="${not empty searchQuery || not empty selectedCategoryId}">
			<div class="search-results">
				<c:choose>
					<c:when test="${not empty selectedCategoryName}">
						<h2>Books in category: "${selectedCategoryName}"</h2>
					</c:when>
					<c:when test="${not empty searchQuery}">
						<h2>Results for: "${searchQuery}"</h2>
					</c:when>
				</c:choose>

				<c:choose>
					<c:when
						test="${not empty searchResults && searchResults.size() > 0}">
						<div class="book-grid">
							<c:forEach var="book" items="${searchResults}">
								<div class="book-card">
									<!-- Book Image -->
									<c:choose>
										<c:when test="${not empty book.bookImgUrl}">
											<c:choose>
												<c:when test="${book.bookImgUrl.startsWith('resources/')}">
													<img
														src="${pageContext.request.contextPath}/${book.bookImgUrl}"
														alt="<c:out value='${book.bookTitle}'/>">
												</c:when>
												<c:otherwise>
													<img
														src="${pageContext.request.contextPath}${book.bookImgUrl}"
														alt="<c:out value='${book.bookTitle}'/>">
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<img
												src="${pageContext.request.contextPath}/resources/images/system/placeholder.png"
												alt="No image available">
										</c:otherwise>
									</c:choose>

									<!-- Book Details -->
									<h3>
										<c:out value="${book.bookTitle}" />
									</h3>
									<p class="book-author">
										By:
										<c:choose>
											<c:when
												test="${not empty book.getAuthorName() && book.getAuthorName() != 'null'}">
												<c:out value="${book.getAuthorName()}" />
											</c:when>
											<c:otherwise>
												<c:out value="Unknown" />
											</c:otherwise>
										</c:choose>
									</p>
									<p class="book-price">
										<fmt:formatNumber value="${book.price}" type="currency"
											currencySymbol="Rs " />
									</p>

									<!-- Check login status using SessionUtil -->
									<%
									boolean isLoggedIn = SessionUtil.isLoggedIn(request, "userName");
									%>

									<!-- Add to Cart Button based on login status -->
									<c:choose>
										<c:when test="<%=isLoggedIn%>">
											<form action="${pageContext.request.contextPath}/cart"
												method="POST" target="hidden-iframe">
												<input type="hidden" name="action" value="add"> <input
													type="hidden" name="bookId" value="${book.bookID}">
												<input type="hidden" name="quantity" value="1">
												<button type="submit" class="cart-button"
													onclick="showNotification(); return false;">Add To
													Cart</button>
											</form>
										</c:when>
										<c:otherwise>
											<a href="${pageContext.request.contextPath}/login"
												class="login-button">Login to Buy</a>
										</c:otherwise>
									</c:choose>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div class="no-results">
							<h3>No books found</h3>
							<c:choose>
								<c:when test="${not empty selectedCategoryName}">
									<p>We couldn't find any books in the selected category.</p>
								</c:when>
								<c:otherwise>
									<p>We couldn't find any books matching your search.</p>
								</c:otherwise>
							</c:choose>
							<p>Try using different keywords or browse our collection.</p>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
	</div>

	<script>
		function showNotification() {
			// Submit the form directly via JavaScript to hidden iframe
			var form = event.target.form;
			var hiddenFrame = window.frames['hidden-iframe'];
			hiddenFrame.document.body.innerHTML = '';
			form.submit();

			// Show notification
			var notification = document.getElementById('notification');
			notification.style.display = 'block';

			// Hide after 3 seconds
			setTimeout(function() {
				notification.style.display = 'none';
			}, 3000);

			// Prevent default form submission
			return false;
		}
	</script>
	<jsp:include page="footer.jsp" />
</body>
</html>