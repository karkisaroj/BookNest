<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add New Book</title>
<%-- Link to your CSS file --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/form-styles.css">
<%-- Adjust path as needed --%>
<style>
/* Basic styles for form layout and messages */
.form-container {
	max-width: 600px;
	margin: 20px auto;
	padding: 20px;
	border: 1px solid #ccc;
	border-radius: 5px;
	background-color: #f9f9f9;
}

.form-group {
	margin-bottom: 15px;
}

.form-group label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
}

.form-group input[type="text"], .form-group input[type="number"],
	.form-group input[type="date"], .form-group textarea {
	width: 95%;
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 3px;
}

.form-group textarea {
	resize: vertical;
	min-height: 80px;
}

.form-actions {
	text-align: right;
	margin-top: 20px;
}

.form-actions button {
	padding: 10px 20px;
	background-color: #007bff;
	color: white;
	border: none;
	border-radius: 3px;
	cursor: pointer;
}

.form-actions button:hover {
	background-color: #0056b3;
}

.message {
	padding: 10px;
	margin-bottom: 15px;
	border-radius: 3px;
}

.success-message {
	background-color: #d4edda;
	color: #155724;
	border: 1px solid #c3e6cb;
}

.error-message {
	background-color: #f8d7da;
	color: #721c24;
	border: 1px solid #f5c6cb;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp" />
	<%-- Include your standard header --%>

	<div class="form-container">
		<h2>Add New Book</h2>

		<%-- Display Flash Messages from Session --%>
		<c:if test="${not empty sessionScope.flashSuccessMessage}">
			<div class="message success-message">
				${sessionScope.flashSuccessMessage}</div>
			<%
			session.removeAttribute("flashSuccessMessage");
			%>
		</c:if>
		<c:if test="${not empty sessionScope.flashErrorMessage}">
			<div class="message error-message">
				${sessionScope.flashErrorMessage}</div>
			<%
			session.removeAttribute("flashErrorMessage");
			%>
		</c:if>

		<%-- The action should point to the URL mapped in @WebServlet --%>
		<form action="${pageContext.request.contextPath}/addBook"
			method="post">

			<div class="form-group">
				<label for="bookTitle">Book Title: *</label> <input type="text"
					id="bookTitle" name="bookTitle" value="${bookTitle}" required>
				<%-- Repopulate value --%>
			</div>

			<div class="form-group">
				<label for="isbn">ISBN:</label> <input type="text" id="isbn"
					name="isbn" value="${isbn}">
				<%-- Repopulate value --%>
			</div>

			<div class="form-group">
				<label for="publicationDate">Publication Date: *
					(YYYY-MM-DD)</label> <input type="date" id="publicationDate"
					name="publicationDate" value="${publicationDate}" required>
				<%-- Use type="date" for browser picker, still need server validation --%>
			</div>

			<div class="form-group">
				<label for="price">Price: *</label> <input type="number" id="price"
					name="price" step="0.01" min="0" value="${price}" required>
				<%-- Use type="number" for better input --%>
			</div>

			<div class="form-group">
				<label for="description">Description:</label>
				<textarea id="description" name="description">${description}</textarea>
				
			</div>

			<div class="form-group">
				<label for="stockQuantity">Stock Quantity: *</label> <input
					type="number" id="stockQuantity" name="stockQuantity" min="0"
					value="${stockQuantity}" required>
			</div>

			<div class="form-group">
				<label for="imageUrl">Image URL:</label> <input type="text"
					id="imageUrl" name="imageUrl" value="${imageUrl}">
				<%-- Consider type="url" --%>
			</div>

			<div class="form-group">
				<label for="publisherId">Publisher ID: *</label>
				<%-- Ideally, this would be a dropdown populated from the DB --%>
				<input type="number" id="publisherId" name="publisherId" min="1"
					value="${publisherId}" required>
			</div>

			<div class="form-actions">
				<button type="submit">Add Book</button>
			</div>

		</form>
	</div>

	<jsp:include page="footer.jsp" />

</body>
</html>