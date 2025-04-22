<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Account</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/myaccount.css" />
</head>
<body>
	<jsp:include page="header.jsp" />
	<%@ page import="com.booknest.util.SessionUtil" %>

<%
	// Retrieve user information from session
	String userName = (String) session.getAttribute("userName");
	String firstName = (String) session.getAttribute("firstName");
	String lastName = (String) session.getAttribute("lastName");
	String email = (String) session.getAttribute("email");
	String phoneNumber = (String) session.getAttribute("phoneNumber");
	String address = (String) session.getAttribute("address");

	// Check if user is logged in
	if (userName == null) {
		// If not logged in, redirect to login page
		response.sendRedirect(request.getContextPath() + "/login");
		return;
	}
	%>

	<div class="account-container">
		<h1 class="account-title">My Account</h1>

		<div class="account-content">
			<!-- Sidebar navigation -->
			<nav class="account-sidebar">
				<ul class="sidebar-menu">
					<li class="sidebar-item active"><span class="sidebar-icon">👤</span>
						<span><a>My Details</a></span></li>
					<li class="sidebar-item"><span class="sidebar-icon">📦</span>
						<span><a>My Orders</a></span></li>
					<li class="sidebar-item"><span class="sidebar-icon">⚙️</span>
						<span><a>Account Setting</a></span></li>
				</ul>
			</nav>

			<!-- Details panel -->
			<div class="details-panel">
				<h2 class="details-title">My Details</h2>

				<!-- Profile image section -->
				<div class="profile-section">
					<div class="profile-image"></div>
					<button class="upload-btn">UPLOAD</button>
				</div>

				<!-- Welcome user message -->
				<div class="welcome-message">
					<h3>
						Welcome,
						<%=userName%>!
						
						
					</h3>
					<p>
						Session ID:
						<%=session.getId()%></p>
					<p>
						Session will expire in
						<%=session.getMaxInactiveInterval()%>
						seconds
					</p>
				</div>

				<!-- Personal information section -->
				<div class="info-section">
					<h3 class="info-section-title">Personal Information</h3>

					<div class="info-row">
						<div class="info-label">Username:</div>
						<div class="info-value"><%=userName%></div>
					</div>

					<div class="info-row">
						<div class="info-label">First name:</div>
						<div class="info-value"><%=firstName%>!</div>
					</div>

					<div class="info-row">
						<div class="info-label">Last name:</div>
						<div class="info-value"><%=lastName%>!</div>
					</div>

					<div class="info-row">
						<div class="info-label">Phone Number:</div>
						<div class="info-value"><%=phoneNumber%>!</div>
					</div>

					<div class="info-row">
						<div class="info-label">Address:</div>
						<div class="info-value"><%=address%>!</div>
					</div>
				</div>

				<!-- Email information section -->
				<div class="info-section">
					<h3 class="info-section-title">Email Information</h3>

					<div class="info-row">
						<div class="info-label">Email Address</div>
						<div class="info-value"><%=email%>!</div>
					</div>
				</div>

				<!-- Edit button -->
				<div class="edit-button-container">
					<button class="edit-btn">EDIT Details</button>
				</div>

				<!-- Logout button -->
				<div class="edit-button-container">
				    <form method="post">
				        <button type="submit" name="logout" class="logout-btn">Logout</button>
				    </form>
				</div>


			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp" />

</body>
</html>