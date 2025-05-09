<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest - ${book.book_title}</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/product-page.css">
</head>

<body>
	<jsp:include page="header.jsp" />

	<div class="container1">
		<div class="product">
			<div class="gallery">
				<c:choose>
					<c:when test="${not empty book.book_img_url}">
						<c:choose>
							<c:when test="${book.book_img_url.startsWith('resources/')}">
								<img
									src="${pageContext.request.contextPath}/${book.book_img_url}"
									alt="${book.book_title}">
							</c:when>
							<c:otherwise>
								<img
									src="${pageContext.request.contextPath}/${book.book_img_url}"
									alt="${book.book_title}">
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<img
							src="${pageContext.request.contextPath}/resources/images/system/placeholder.png"
							alt="No image available">
					</c:otherwise>
				</c:choose>
			</div>
			<div class="details">
				<h1>${book.book_title}</h1>

				<c:if test="${not empty book.authorName}">
					<p class="author">By: ${book.authorName}</p>
				</c:if>

				<c:if test="${not empty book.isbn}">
					<p class="isbn">ISBN: ${book.isbn}</p>
				</c:if>

				<c:if test="${not empty book.publication_date}">
					<p class="publication-date">
						Publication Date:
						<fmt:formatDate value="${book.publication_date}"
							pattern="yyyy-MM-dd" />
					</p>
				</c:if>

				<c:if test="${not empty book.description}">
					<div class="description">
						<h3>Description</h3>
						<p>${book.description}</p>
					</div>
				</c:if>

				<c:if test="${book.page_count > 0}">
					<p class="page-count">Pages: ${book.page_count}</p>
				</c:if>

				<h2>
					<fmt:formatNumber value="${book.price}" type="currency"
						currencySymbol="Rs " />
				</h2>

				<c:if test="${book.stock_quantity > 0}">
					<p class="stock">In Stock (${book.stock_quantity} available)</p>
				</c:if>
				<c:if test="${book.stock_quantity <= 0}">
					<p class="out-of-stock">Out of Stock</p>
				</c:if>

				<form action="<c:url value='/cart'/>" method="POST">
					<input type="hidden" name="action" value="add"> <input
						type="hidden" name="bookId" value="${book.bookID}"> <input
						type="hidden" name="sourceUrl"
						value="${pageContext.request.contextPath}/product?bookId=${book.bookID}">
					<button type="submit" ${book.stock_quantity <= 0 ? 'disabled' : ''}>Add
						to Cart</button>
				</form>

				<a href="${pageContext.request.contextPath}/books"
					class="back-button">Back to Books</a>
			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp" />

	<script>
		document
				.addEventListener(
						'DOMContentLoaded',
						function() {
							console
									.log('Product page loaded for book ID: ${book.bookID}');

							// Check if image loads correctly
							const bookImage = document
									.querySelector('.gallery img');
							if (bookImage) {
								bookImage.onerror = function() {
									console.error('Failed to load book image: '
											+ this.src);
									this.src = '${pageContext.request.contextPath}/resources/images/system/placeholder.png';
								};
							}
						});
	</script>
</body>
</html>