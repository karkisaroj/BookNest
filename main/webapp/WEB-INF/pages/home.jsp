<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%-- Make sure your HomeController sets 'randomBooks' and 'popularBooks' attributes --%>
<%-- Also ensure your login process sets 'loggedInUser' in the session --%>
<%-- Ensure your CartServlet exists and handles POST requests to /cart --%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest - Your Book Heaven</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/home.css">

</head>
<body>
	<jsp:include page="header.jsp" />

	<!-- Hero Section (sec-1 as provided by you) -->
	<section class="sec-1">
		<div class="container flex">
			<div class="sec-right">
				<h4>BookNest</h4>
				<h2>Your Ultimate Book Heaven</h2>
				<p>Discover a world of stories, knowledge, and adventures. Dive
					into our vast collection of books for every reader and interest.</p>
				<button class="exp-btn">
					<a href="${pageContext.request.contextPath}/books"> Explore Our
						Books</a>
				</button>
				<img
					src="${pageContext.request.contextPath}/resources/images/system/bookstack.png"
					alt="Book stack decoration" class="img-right">
			</div>
			<div class="sec-left">
				<img
					src="${pageContext.request.contextPath}/resources/images/system/book-1.jpg"
					alt="Featured book" class="img-1"> <img
					src="${pageContext.request.contextPath}/resources/images/system/book-2.jpg"
					alt="Featured book" class="img-2"> <img
					src="${pageContext.request.contextPath}/resources/images/system/book-3.jpg"
					alt="Featured book" class="img-3"> <img
					src="${pageContext.request.contextPath}/resources/images/system/book-4.jpg"
					alt="Featured book" class="img-4">
			</div>
		</div>
	</section>



	<%-- Section 2 - Books (Uses randomBooks from HomeController) --%>
	<section class="sec-2">
		<div class="container">
			<h3 class="topic">Books</h3>
			<%-- Title changed from "Featured Books" --%>
			<div class="divider"></div>
			<span class="see_more"><a
				href="${pageContext.request.contextPath}/books">See More</a></span>
			<div class="cards">
				<c:choose>
					<%-- Check if the randomBooks list is not empty --%>
					<c:when test="${not empty randomBooks}">
						<%-- Loop through each book in the randomBooks list --%>
						<c:forEach var="book" items="${randomBooks}">
							<div class="card">
								<div class="card-img">
									<%-- Link to a potential product page (adjust URL as needed) --%>
									<a
										href="${pageContext.request.contextPath}/product?id=${book.bookID}">
										<c:choose>
											<c:when test="${not empty book.book_img_url}">
												<%-- Use context path for image source --%>
												<img
													src="${pageContext.request.contextPath}${book.book_img_url}"
													alt="<c:out value='${book.book_title}'/>">
											</c:when>
											<c:otherwise>
												<%-- Placeholder image if book_img_url is empty --%>
												<img
													src="${pageContext.request.contextPath}/resources/images/system/placeholder.png"
													alt="No image available">
											</c:otherwise>
										</c:choose>
									</a>
								</div>
								<%-- Display Book Title --%>
								<h3>
									<c:out value="${book.book_title}" />
								</h3>
								<div class="price-name">
									<%-- Display Formatted Price --%>
									<span><fmt:formatNumber value="${book.price}"
											type="currency" currencySymbol="Rs. " /></span>
								</div>
								<div class="cart">
									<%-- Form submits to CartServlet --%>
									<form action="<c:url value='/cart'/>" method="POST"
										style="display: inline;">
										<input type="hidden" name="action" value="add"> <input
											type="hidden" name="bookId" value="${book.bookID}">
										<%-- Redirect back to the current page (home) after adding --%>
										<input type="hidden" name="sourceUrl"
											value="${pageContext.request.requestURI}">
										<button type="submit">Add To Cart</button>
									</form>
								</div>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<%-- Message shown only if list is empty AND no general loading error occurred --%>
						<c:if test="${empty homeErrorMessage}">
							<p style="width: 100%; text-align: center;">No books
								available right now.</p>
						</c:if>
					</c:otherwise>
				</c:choose>
				<%-- General loading error message already handled above flash messages --%>
			</div>
		</div>
	</section>

	<%-- Section 3 - Popular (Uses popularBooks from HomeController) --%>
	<section class="sec-3">
		<div class="container">
			<h3 class="topic">Popular</h3>
			<div class="divider"></div>
			<span class="see_more"><a
				href="${pageContext.request.contextPath}/books">See More</a></span>
			<div class="cards">
				<c:choose>
					<c:when test="${not empty popularBooks}">
						<c:forEach var="book" items="${popularBooks}">
							<div class="card">
								<div class="card-img">
									<a
										href="${pageContext.request.contextPath}/product?id=${book.bookID}">
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
									</a>
								</div>
								<h3>
									<c:out value="${book.book_title}" />
								</h3>
								<div class="price-name">
									<span><fmt:formatNumber value="${book.price}"
											type="currency" currencySymbol="Rs. " /></span>
								</div>
								<div class="cart">
									<%-- Form submits to CartServlet --%>
									<form action="<c:url value='/cart'/>" method="POST"
										style="display: inline;">
										<%-- **** ENSURE THIS LINE IS PRESENT AND CORRECT **** --%>
										<input type="hidden" name="action" value="add">
										<%-- **** ---------------------------------------- **** --%>
										<input type="hidden" name="bookId" value="${book.bookID}">
										<input type="hidden" name="sourceUrl"
											value="${pageContext.request.contextPath}/home">
										<%-- Use mapped URL --%>
										<button type="submit">Add To Cart</button>
									</form>
								</div>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:if test="${empty homeErrorMessage}">
							<p style="width: 100%; text-align: center;">No popular books
								to show right now.</p>
						</c:if>
					</c:otherwise>
				</c:choose>
				<%-- Error message already shown above if needed --%>
			</div>
		</div>
	</section>

	<!-- Why Choose Us Section (sec-4 as provided by you) -->
	<section class="sec-4">
		<div class="container">
			<h2 class="choose-us">Why Choose Us</h2>
			<div class="flex-abt">
				<div class="abt-fit">
					<h4 class="abt-head">Lowest Price Guarantee</h4>
					<p>We continuously search and match all our prices with the
						lowest prices available in Nepal, ensuring you always get the best
						deal on your favorite books.</p>
				</div>
				<div class="abt-fit">
					<h4 class="abt-head">Express 48-Hour Delivery</h4>
					<p>All orders placed will be delivered within 48 hours,
						bringing your favorite books to your doorstep faster than anyone
						else!</p>
				</div>
				<div class="abt-fit">
					<h4 class="abt-head">Outstanding Customer Service</h4>
					<p>Our customer service agents are authorized to go above and
						beyond to solve customer issues. We're not satisfied until you
						are.</p>
				</div>
				<div class="abt-fit">
					<h4 class="abt-head">30-Day Exchange Policy</h4>
					<p>Customers can make an exchange up to 30 days from the day of
						purchase, giving you peace of mind with every purchase.</p>
				</div>
				<img
					src="${pageContext.request.contextPath}/resources/images/system/bookstack.png"
					alt="Book stack decoration" class="img-abt">
			</div>
		</div>
	</section>

	<%-- Script section (as provided by you) --%>
	<script>
        // Your existing JavaScript for animations
        document.addEventListener('DOMContentLoaded', function() {
          heroImages.forEach((img, index) => {
            img.style.animation = `float ${3 + index * 0.5}s ease-in-out infinite`;
            img.style.animationDelay = `${index * 0.2}s`;
          });
          const cards = document.querySelectorAll('.card');
          const abtFits = document.querySelectorAll('.abt-fit');
          function revealOnScroll() {
            const elements = [...cards, ...abtFits];
            elements.forEach(el => {
              const elementTop = el.getBoundingClientRect().top;
              const elementVisible = 150;
              if (elementTop < window.innerHeight - elementVisible) {
                el.style.animation = 'fadeIn 0.6s forwards';
              }
            });
          }
          window.addEventListener('scroll', revealOnScroll);
          revealOnScroll();
        });
    </script>

	<jsp:include page="footer.jsp" />
</body>
</html>