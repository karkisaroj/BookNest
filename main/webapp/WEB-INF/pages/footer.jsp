<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest Footer</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>
	<footer class="footer">
		<div class="footer-container">
			<div class="footer-logo">
				<div class="logo">
					<span
						style="font-family: serif; font-style: italic; font-weight: bold; font-size: 22px;">BookNest</span>
				</div>
				<div class="copyright">Â© 2025 - 2026 BookNest</div>
			</div>

			<hr class="footer-divider">

			<div class="footer-content">
				<div class="footer-links">
					<div class="footer-column">
						<div class="column-title">Links</div>
						<a href="${pageContext.request.contextPath}/home"
							class="column-link">Home</a> <a
							href="${pageContext.request.contextPath}/books"
							class="column-link">Books</a>
					</div>

					<div class="footer-column">
						<div class="column-title">Books Section</div>

						<a href="${pageContext.request.contextPath}/home#featured-books"
							class="column-link">Featured Books</a> <a
							href="${pageContext.request.contextPath}/home#popular-books"
							class="column-link">Popular Books</a>
					</div>

					<div class="footer-column">
						<div class="column-title">About Us</div>
						<a href="${pageContext.request.contextPath}/aboutus"
							class="column-link">Our Story</a> <a
							href="${pageContext.request.contextPath}/contactus"
							class="column-link">Provide Feedback</a> <a
							href="${pageContext.request.contextPath}/aboutus#our-team"
							class="column-link">Our Team</a>
					</div>
				</div>

				<div class="payment-social">
					<div class="payment-methods">
						<a href="https://fonepay.com/" target="_blank"> <img
							src="${pageContext.request.contextPath}/resources/images/system/fonepay.png"
							alt="fonepay">
						</a> <a href="https://khalti.com/" target="_blank"> <img
							src="${pageContext.request.contextPath}/resources/images/system/khalti.png"
							alt="khalti">
						</a> <a href="https://esewa.com.np/#/home" target="_blank"> <img
							src="${pageContext.request.contextPath}/resources/images/system/esewa.png"
							alt="esewa">
						</a>
					</div>

					<div class="social-links">
						<a href="https://www.facebook.com/" class="social-icon facebook"
							target="_blank"> <img class="img-size"
							src="${pageContext.request.contextPath}/resources/images/system/facebook.png"
							alt="Facebook">
						</a> <a href="https://www.instagram.com/"
							class="social-icon instagram" target="_blank"> <img
							class="img-size"
							src="${pageContext.request.contextPath}/resources/images/system/insta.png"
							alt="Instagram">
						</a> <a href="https://www.whatsapp.com/" class="social-icon whatsapp"
							target="_blank"> <img class="img-size"
							src="${pageContext.request.contextPath}/resources/images/system/whatsapp.png"
							alt="WhatsApp">
						</a>
					</div>
				</div>
			</div>
		</div>
	</footer>

</body>
</html>