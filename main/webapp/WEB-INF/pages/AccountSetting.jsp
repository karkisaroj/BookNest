<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest - Account Settings</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/account-styles.css">
<style>
/* Add these styles for messages if not already in your CSS */
.message {
    margin: 15px 0;
    padding: 10px 15px;
    border-radius: 4px;
    font-weight: 500;
}
.success {
    color: #2e7d32;
    background-color: #e8f5e9;
    border-left: 4px solid #2e7d32;
}
.error {
    color: #c62828;
    background-color: #ffebee;
    border-left: 4px solid #c62828;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp" />

	<main>
		<aside class="sidebar">
			<ul class="sidebar-menu">
				<li><a href="${pageContext.request.contextPath}/myaccount">
						<span class="sidebar-icon">👤</span> <span>My Details</span>
				</a></li>
				<li><a href="${pageContext.request.contextPath}/orders"> <span
						class="sidebar-icon">📦</span> <span>My Orders</span>
				</a></li>
				<li class="active"><a
					href="${pageContext.request.contextPath}/accountsetting"> <span
						class="sidebar-icon">⚙️</span> <span>Account Setting</span>
				</a></li>
			</ul>
		</aside>

		<section class="account-settings">
			<h1>Account Settings</h1>

			<!-- Personal Information Form -->
			<form action="${pageContext.request.contextPath}/accountsetting" method="post">
				<fieldset>
					<legend>Update Account Information</legend>
					
					<c:if test="${not empty accountInfoSuccess}">
						<div class="message success">
							<c:out value="${accountInfoSuccess}" />
						</div>
					</c:if>
					<c:if test="${not empty accountInfoError}">
						<div class="message error">
							<c:out value="${accountInfoError}" />
						</div>
					</c:if>
					
					<label for="first-name">First Name</label> 
					<input type="text" id="first-name" name="first-name" value="${firstName}"> 
					
					<label for="second-name">Last Name</label> 
					<input type="text" id="second-name" name="second-name" value="${lastName}"> 
					
					<label for="phone-number">Phone Number</label> 
					<input type="text" id="phone-number" name="phone-number" value="${phoneNumber}">

					<button type="submit">Save</button>
				</fieldset>
			</form>

			<!-- Email Information Form -->
			<form action="${pageContext.request.contextPath}/accountsetting" method="post">
				<fieldset>
					<legend>Update Email Information</legend>
					
					<c:if test="${not empty emailSuccess}">
						<div class="message success">
							<c:out value="${emailSuccess}" />
						</div>
					</c:if>
					<c:if test="${not empty emailError}">
						<div class="message error">
							<c:out value="${emailError}" />
						</div>
					</c:if>
					
					<label for="email">Email Address</label> 
					<input type="email" id="email" name="email" value="${email}"> 
					
					<label for="confirm-email">Confirm Email Address</label> 
					<input type="email" id="confirm-email" name="confirm-email">

					<button type="submit">Save</button>
				</fieldset>
			</form>

			<!-- Password Form -->
			<form action="${pageContext.request.contextPath}/accountsetting" method="post">
				<fieldset>
					<legend>Update Password</legend>
					
					<c:if test="${not empty passwordSuccess}">
						<div class="message success">
							<c:out value="${passwordSuccess}" />
						</div>
					</c:if>
					<c:if test="${not empty passwordError}">
						<div class="message error">
							<c:out value="${passwordError}" />
						</div>
					</c:if>
					
					<label for="current-password">Current Password</label> 
					<input type="password" id="current-password" name="current-password">

					<label for="new-password">New Password</label> 
					<input type="password" id="new-password" name="new-password"> 
					
					<label for="confirm-new-password">Confirm New Password</label> 
					<input type="password" id="confirm-new-password" name="confirm-new-password">

					<button type="submit">Save</button>
				</fieldset>
			</form>
		</section>
	</main>
	<jsp:include page="footer.jsp" />
</body>
</html>