<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Contact Us | BookNest</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/ContactUs.css">
</head>

<body>
	<!-- Header would go here -->
	<jsp:include page="header.jsp" />
	<!-- Contact Content -->
	<main class="booknest-contact">
		<div class="booknest-contact-container">
			<div class="booknest-logo-container">
				<div class="booknest-contact-logo">BookNest</div>
			</div>

			<div class="booknest-contact-header">
				<h2>REACH OUT TO US</h2>
				<h1>We'd Love to Hear From You</h1>
				<div class="booknest-decorative-line"></div>
				<p>Have questions about our books, your orders, or anything
					else? Our team is ready to assist you. Fill in the form below, and
					we'll get back to you as soon as possible.</p>
			</div>

			<!-- Contact Form -->
			<div class="booknest-contact-form">
				<form>
					<div class="booknest-form-grid">
						<div class="booknest-form-group">
							<label for="name">Your Name</label> <input type="text" id="name"
								name="name" placeholder="Enter your full name">
						</div>
						<div class="booknest-form-group">
							<label for="email">Email Address</label> <input type="email"
								id="email" name="email" placeholder="Enter your email address">
						</div>
						<div class="booknest-form-group">
							<label for="phone">Phone (Optional)</label> <input type="tel"
								id="phone" name="phone" placeholder="Enter your phone number">
						</div>
						<div class="booknest-form-group">
							<label for="subject">Subject</label> <input type="text"
								id="subject" name="subject"
								placeholder="What is this regarding?">
						</div>
						<div class="booknest-form-group booknest-form-full">
							<label for="message">Your Message</label>
							<textarea id="message" name="message"
								placeholder="Tell us how we can help you..."></textarea>
						</div>
					</div>
					<div class="booknest-submit-container">
						<button type="submit">
							Send Message
							<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18"
								viewBox="0 0 24 24" fill="none" stroke="currentColor"
								stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <line x1="22" y1="2" x2="11" y2="13"></line>
                                <polygon
									points="22 2 15 22 11 13 2 9 22 2"></polygon>
                            </svg>
						</button>
					</div>
				</form>
			</div>

			<!-- Contact Info -->
			<div class="booknest-contact-info">
				<div class="booknest-contact-item">
					<div class="booknest-contact-icon">
						<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path
								d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                            <polyline points="22,6 12,13 2,6"></polyline>
                        </svg>
					</div>
					<h3>Email Us</h3>
					<p>hello@booknest.com</p>
				</div>
				<div class="booknest-contact-item">
					<div class="booknest-contact-icon">
						<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path
								d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                        </svg>
					</div>
					<h3>Call Us</h3>
					<p>(555) 123-4567</p>
				</div>
				<div class="booknest-contact-item">
					<div class="booknest-contact-icon">
						<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path
								d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                            <circle cx="12" cy="10" r="3"></circle>
                        </svg>
					</div>
					<h3>Visit Us</h3>
					<p>
						123 Book Lane<br>Reading, RG1 2BK
					</p>
				</div>
			</div>
		</div>
	</main>
	<jsp:include page="footer.jsp" />
</body>
</html>