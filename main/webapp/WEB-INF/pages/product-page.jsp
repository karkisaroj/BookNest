<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest - ${book.bookTitle}</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/product-page.css">
</head>

<body>
	<jsp:include page="header.jsp" />

	<div class="container1">
		<div class="product">
			<div class="gallery">
				<c:choose>
					<c:when test="${not empty book.bookImgUrl}">
						<c:choose>
							<c:when test="${book.bookImgUrl.startsWith('resources/')}">
								<img src="${pageContext.request.contextPath}/${book.bookImgUrl}"
									alt="${book.bookTitle}">
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/${book.bookImgUrl}"
									alt="${book.bookTitle}">
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
				<h1>${book.bookTitle}</h1>

				<c:if test="${not empty book.authorName}">
					<p class="author">By: ${book.authorName}</p>
				</c:if>

				<c:if test="${not empty book.isbn}">
					<p class="isbn">ISBN: ${book.isbn}</p>
				</c:if>

				<c:if test="${not empty book.publicationDate}">
					<p class="publication-date">
						Publication Date:
						<fmt:formatDate value="${book.publicationDate}"
							pattern="yyyy-MM-dd" />
					</p>
				</c:if>

				<c:if test="${not empty book.description}">
					<div class="description">
						<h3>Description</h3>
						<p>${book.description}</p>
					</div>
				</c:if>

				<c:if test="${book.pageCount > 0}">
					<p class="page-count">Pages: ${book.pageCount}</p>
				</c:if>

				<h2>
					<fmt:formatNumber value="${book.price}" type="currency"
						currencySymbol="Rs " />
				</h2>

				<c:if test="${book.stockQuantity > 0}">
					<p class="stock">In Stock (${book.stockQuantity} available)</p>
				</c:if>
				<c:if test="${book.stockQuantity <= 0}">
					<p class="out-of-stock">Out of Stock</p>
				</c:if>

				<form action="<c:url value='/cart'/>" method="POST">
					<input type="hidden" name="action" value="add"> <input
						type="hidden" name="bookId" value="${book.bookID}"> <input
						type="hidden" name="sourceUrl"
						value="${pageContext.request.contextPath}/product?bookId=${book.bookID}">
					<button type="submit" ${book.stockQuantity <= 0 ? 'disabled' : ''}>Add
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