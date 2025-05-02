<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BookNest - Our Books</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/books.css">


</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="container">
		<h1>Books available in BookNest</h1>


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

		<c:if test="${not empty viewErrorMessage}">
			<div class="alert alert-danger">
				<c:out value="${viewErrorMessage}" />
			</div>
		</c:if>

		<div class="book-list">
			<c:choose>
				<c:when test="${not empty books}">
					<c:forEach var="book" items="${books}">
						<div class="book-item">
							<div>

								<c:choose>
									<c:when test="${not empty book.book_img_url}">
										<img
											src="${pageContext.request.contextPath}${book.book_img_url}"
											alt="<c:out value='${book.book_title}'/>">
									</c:when>
									<c:otherwise>
										<img
											src="${pageContext.request.contextPath}/resources/images/system/placeholder.png"
											alt="No image available">
									</c:otherwise>
								</c:choose>


								<h3>
									<c:out value="${book.book_title}" />
								</h3>


								<p class="author">
									By:
									<c:out value="${book.authorName}" default="N/A" />
								</p>

								<%-- Price access (seems okay) --%>
								<p class="book-price">
									<fmt:formatNumber value="${book.price}" type="currency"
										currencySymbol="Rs " />

								</p>
							</div>

							<%-- Form for Add To Cart --%>
							<form action="<c:url value='/cart'/>" method="POST">
								<input type="hidden" name="action" value="add">
								<%-- Corrected Book ID access --%>
								<input type="hidden" name="bookId" value="${book.bookID}">
								<input type="hidden" name="sourceUrl"
									value="${pageContext.request.contextPath}/books">

								<button type="submit">Add To Cart</button>
							</form>

						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<c:if test="${empty viewErrorMessage}">
						<p style="text-align: center; width: 100%;">No books available
							at the moment.</p>
					</c:if>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>