<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- JSTL taglib --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Account</title>

<%-- Link specifically to myaccount.css from within this page --%>
<%-- Assumes myaccount.css is located at webapp/css/myaccount.css --%>
<link rel="stylesheet" href="<c:url value='/css/myaccount.css'/>" />

<%-- Add any other specific head elements needed ONLY for this page --%>
<%-- Note: Global styles/scripts should ideally be in header.jsp --%>

</head>
<body>
	<%-- Include Header --%>
	<jsp:include page="header.jsp" />

	<%-- Main Account Container --%>
	<div class="account-container">
		<h1 class="account-title">My Account</h1>

		<%-- Display Success/Error Messages Passed from Controller --%>
		<c:if test="${not empty successMessage}">
			<div class="message success">
				<%-- Style defined in myaccount.css --%>
				<c:out value="${successMessage}" />
			</div>
		</c:if>
		<c:if test="${not empty errorMessage}">
			<div class="message error">
				<%-- Style defined in myaccount.css --%>
				<c:out value="${errorMessage}" />
			</div>
		</c:if>

		<div class="account-content">
			<!-- Sidebar Navigation -->
			<div class="account-sidebar">
				<ul class="sidebar-menu">
					<li class="sidebar-item active"><span class="sidebar-icon">👤</span>
						<span><a href="<c:url value='/myaccount'/>">My Details</a></span>
					</li>
					<li class="sidebar-item"><span class="sidebar-icon">📦</span>
						<span><a href="<c:url value='/orders'/>">My Orders</a></span></li>
					<li class="sidebar-item"><span class="sidebar-icon">⚙️</span>
						<span><a href="<c:url value='/accountsetting'/>">Account
								Setting</a></span></li>
				</ul>
			</div>

			<!-- Details Panel -->
			<div class="details-panel">
				<h2 class="details-title">My Details</h2>

				<!-- Profile Section -->
				<div class="profile-section">
					<div class="profile-image" id="profilePreview">
						<img id="previewImg" src="<c:url value='/${profileImageUrl}'/>"
							alt="Profile Picture"
							onerror="this.onerror=null; this.src='<c:url value="/resources/images/system/person.png"/>';">
						<%-- Fallback --%>
					</div>
					<form action="<c:url value='/myaccount'/>" method="post"
						enctype="multipart/form-data">
						<input type="file" name="image" id="imageInput" accept="image/*"
							required> <br>
						<button type="submit" class="upload-btn">UPLOAD</button>
					</form>
				</div>

				<!-- Personal Information -->
				<div class="info-section">
					<h3 class="info-section-title">Personal Information</h3>
					<div class="info-row">
						<div class="info-label">Username:</div>
						<div class="info-value">
							<c:out value="${userName}" />
						</div>
					</div>
					<div class="info-row">
						<div class="info-label">First Name:</div>
						<div class="info-value">
							<c:out value="${firstName}" />
						</div>
					</div>
					<div class="info-row">
						<div class="info-label">Last Name:</div>
						<div class="info-value">
							<c:out value="${lastName}" />
						</div>
					</div>
					<div class="info-row">
						<div class="info-label">Phone Number:</div>
						<div class="info-value">
							<c:out value="${phoneNumber}" />
						</div>
					</div>
					<div class="info-row">
						<div class="info-label">Address:</div>
						<div class="info-value">
							<c:out value="${address}" />
						</div>
					</div>
				</div>

				<!-- Email Information -->
				<div class="info-section">
					<h3 class="info-section-title">Email Information</h3>
					<div class="info-row">
						<div class="info-label">Email Address:</div>
						<div class="info-value">
							<c:out value="${email}" />
						</div>
					</div>
				</div>

				<!-- Action Buttons -->
				<div class="edit-button-container">
					<button class="edit-btn"
						onclick="location.href='<c:url value="/editAccount"/>'">EDIT
						Details</button>
				</div>
			</div>
			<%-- End details-panel --%>
		</div>
		<%-- End account-content --%>
	</div>
	<%-- End account-container --%>

	<%-- Include Footer --%>
	<jsp:include page="footer.jsp" />

</body>
</html>