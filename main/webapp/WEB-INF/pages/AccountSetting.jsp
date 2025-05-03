<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNext - Account Settings</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/account-styles.css">
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

			<form>
				<fieldset>
					<legend>Update Account Information</legend>
					<label for="first-name">First Name</label> <input type="text"
						id="first-name" name="first-name"> <label
						for="second-name">Second Name</label> <input type="text"
						id="second-name" name="second-name"> <label
						for="phone-number">Phone Number</label> <input type="text"
						id="phone-number" name="phone-number">

					<button type="submit">Save</button>
				</fieldset>

				<fieldset>
					<legend>Update Email Information</legend>
					<label for="email">Email Address</label> <input type="email"
						id="email" name="email"> <label for="confirm-email">Confirm
						Email Address</label> <input type="email" id="confirm-email"
						name="confirm-email">

					<button type="submit">Save</button>
				</fieldset>

				<fieldset>
					<legend>Update Password</legend>
					<label for="current-password">Current Password</label> <input
						type="password" id="current-password" name="current-password">

					<label for="new-password">New Password</label> <input
						type="password" id="new-password" name="new-password"> <label
						for="confirm-new-password">Confirm New Password</label> <input
						type="password" id="confirm-new-password"
						name="confirm-new-password">

					<button type="submit">Save</button>
				</fieldset>
			</form>
		</section>
	</main>
	<jsp:include page="footer.jsp" />
</body>
</html>