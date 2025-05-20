<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>BookNest</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/register.css" />
</head>

<body>
	<div class="container">
		<div class="left-side">
			<h1 class="tagline">Nest Your Book and Be in Peace</h1>
			<img class="book-stack"
				src="${pageContext.request.contextPath}/resources/images/system/bookstack.png"
				alt="Stack of books">
		</div>
		<div class="right-side">
			<div class="registration-container">
				<h1 class="registration-title">Create Account</h1>

				<form method="Post">
					<div class="form-row">
						<div class="form-group">
							<input type="text" id="firstName" name="firstName"> <label
								for="firstName">First Name</label>
						</div>

						<div class="form-group">
							<input type="text" id="lastName" name="lastName"> <label
								for="lastName">Last Name</label>
						</div>
					</div>
					<div class="form-row">
						<div class="form-group">
							<input type="text" id="userName" name="userName"> <label
								for="userName">User Name</label>
						</div>


						<div class="form-group">
							<input type="text" id="email" name="email"> <label
								for="text">Email</label>
						</div>
					</div>



					<div class="form-group">
						<input type="password" id="password" name="password"> <label
							for="password">Password</label>
					</div>

					<div class="form-row">
						<div class="form-group">
							<input type="text" id="address" name="address"> <label
								for="address">Address</label>
						</div>

						<div class="form-group">
							<input type="text" id="phoneNumber" name="phoneNumber"> <label
								for="phoneNumber">Phone Number</label>
						</div>

					</div>
					<!-- Error message display -->
					<%
					if (request.getAttribute("error") != null) {
					%>
					<div class="error-message">
						<span class="error-icon"></span> ${error}
					</div>
					<%
					}
					%>
					<button type="submit" class="btn-create">Create Account</button>

					<div class="login-link">
						Already Have an Account? <a
							href="${pageContext.request.contextPath}/login">Login</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>

</html>