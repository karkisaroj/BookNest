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
	<%
	// Retrieve user information from session
	String userName = (String) session.getAttribute("userName");
	String firstName = (String) session.getAttribute("firstName");
	String lastName = (String) session.getAttribute("lastName");
	String email = (String) session.getAttribute("email");
	String phoneNumber = (String) session.getAttribute("phoneNumber");
	String address = (String) session.getAttribute("address");






	// Checking if user is logged in
	if (userName == null) {

		// If not logged in, redirecting to login page

		response.sendRedirect(request.getContextPath() + "/login");
		return;
	}
	%>
	<div class="account-container">
		<h1 class="account-title">My Account</h1>
		<div class="account-content">
			<!-- Sidebar Navigation -->
			<div class="account-sidebar">
				<ul class="sidebar-menu">
					<li class="sidebar-item active"><span class="sidebar-icon">👤</span>

						My Details</li>
					<li class="sidebar-item"><span class="sidebar-icon">📦</span>
						My Orders</li>
					<li class="sidebar-item"><span class="sidebar-icon">⚙️</span>
						Account Setting</li>

						<span><a href="${pageContext.request.contextPath}/myaccount">My Details</a></span></li>
					<li class="sidebar-item"><span class="sidebar-icon">📦</span>
						<span><a href="${pageContext.request.contextPath}/cart">My Orders</a></span></li>
					<li class="sidebar-item"><span class="sidebar-icon">⚙️</span>
						<span><a href="${pageContext.request.contextPath}/accountsetting">Account Setting</a></span></li>

				</ul>
			</div>
			<!-- Details Panel -->
			<div class="details-panel">
				<h2 class="details-title">My Details</h2>
				<!-- Profile Section -->
				<div class="profile-section">
					<div class="profile-image" id="profilePreview">
						<img id="previewImg"
							src="${pageContext.request.contextPath}/<%= (String) request.getAttribute("profileImageUrl") %>"
							alt="Profile Picture">
					</div>
					<form action="${pageContext.request.contextPath}/myaccount"
						method="post" enctype="multipart/form-data">
						<input type="file" name="image" id="imageInput" accept="image/*"
							required> <br>
						<button type="submit" class="upload-btn">UPLOAD</button>
					</form>
				</div>
			</div>
			
			<!-- Personal Information -->
				<div class="info-section">
					<h3 class="info-section-title">Personal Information</h3>
					<div class="info-row">
						<div class="info-label">Username:</div>
						<div class="info-value"><%=userName%></div>
					</div>
					<div class="info-row">
						<div class="info-label">First Name:</div>
						<div class="info-value"><%=firstName%></div>
					</div>
					<div class="info-row">
						<div class="info-label">Last Name:</div>
						<div class="info-value"><%=lastName%></div>
					</div>
					<div class="info-row">
						<div class="info-label">Phone Number:</div>
						<div class="info-value"><%=phoneNumber%></div>
					</div>
					<div class="info-row">
						<div class="info-label">Address:</div>
						<div class="info-value"><%=address%></div>
					</div>
				</div>
				<!-- Email Information -->
				<div class="info-section">
					<h3 class="info-section-title">Email Information</h3>
					<div class="info-row">
						<div class="info-label">Email Address:</div>
						<div class="info-value"><%=email%></div>
					</div>
				</div>
				<!-- Action Buttons -->
				<div class="edit-button-container">
					<button class="edit-btn" onclick="location.href='editAccount.jsp'">EDIT
						Details</button>
				</div>
			</div>
		</div>

	<jsp:include page="footer.jsp" />
</body>

</html>