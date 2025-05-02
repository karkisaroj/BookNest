<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.booknest.model.PublisherModel"%>
<%@ page import="com.booknest.model.AuthorModel"%>
<%@ page import="com.booknest.model.CategoryModel"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin - Add Product</title>

<style>
/* Encapsulating styles for the page to avoid collisions with header CSS */
.add-product-page {
	background-color: #f5f5f5;
	min-height: 100vh;
	display: flex;
	justify-content: center;
	align-items: center;
	padding-top: 100px; /* Prevent overlapping with header */
	padding-bottom: 50px; /* Prevent overlapping with footer */
}

.form-container {
	background-color: white;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	width: 90%;
	max-width: 1200px;
	margin: 0 auto; /* Center the form horizontally */
	animation: fadeIn 1s ease-in-out;
}

.form-header {
	text-align: center;
	margin-bottom: 30px;
}

.form-header h1 {
	color: #2c3e50;
	font-size: 32px;
	margin-bottom: 10px;
}

.form-header p {
	color: #7f8c8d;
	font-size: 16px;
}

.highlight {
	color: #DDA853;
	font-weight: bold;
}

.form-description {
	background-color: #f5f7fa; /* Light background to match header theme */
	padding: 15px 20px;
	border-left: 4px solid #2c3e50; /* Changed to header color */
	border-radius: 5px;
	margin-bottom: 25px;
	color: #2c3e50; /* Changed text color to header color */
}

.form-description i {
	margin-right: 10px;
	color: #2c3e50; /* Changed icon color to header color */
}

.message-container {
	margin-bottom: 20px;
	padding: 10px;
	border-radius: 5px;
}

.success-message {
	background-color: #d4edda;
	color: #155724;
	border: 1px solid #c3e6cb;
	padding: 10px 15px;
	border-radius: 5px;
	margin-bottom: 20px;
}

.error-message {
	background-color: #f8d7da;
	color: #721c24;
	border: 1px solid #f5c6cb;
	padding: 10px 15px;
	border-radius: 5px;
	margin-bottom: 20px;
}

.form-group {
	margin-bottom: 20px;
}

.form-group label {
	display: flex;
	align-items: center;
	margin-bottom: 5px;
	font-weight: bold;
	color: #2c3e50;
}

.form-group label i {
	margin-right: 10px;
	color: #DDA853;
}

.form-group input, .form-group select, .form-group textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #ecf0f1;
	border-radius: 5px;
	font-size: 16px;
	transition: border 0.3s;
}

.form-group input:focus, .form-group select:focus, .form-group textarea:focus
	{
	border-color: #DDA853;
	outline: none;
	box-shadow: 0 0 8px rgba(221, 168, 83, 0.5);
}

.form-group textarea {
	resize: none;
	height: 120px;
}

.form-actions {
	text-align: center;
	margin-top: 30px;
}

.form-actions button {
	background-color: #DDA853;
	color: white;
	border: none;
	padding: 12px 25px;
	border-radius: 5px;
	font-size: 18px;
	cursor: pointer;
	transition: background-color 0.3s, transform 0.2s;
}

.form-actions button:hover {
	background-color: #c69e4e;
	transform: scale(1.05);
}

.form-actions button:active {
	transform: scale(0.95);
}
</style>
</head>

<body>
	<jsp:include page="header.jsp" />
	<div class="add-product-page">
		<div class="form-container">
			<div class="form-header">
				<h1>Add New Book</h1>
				<p class="highlight">Complete the form to add a new product to
					the catalog.</p>
			</div>

			<div class="form-description">
				<i class="fas fa-info-circle"></i> Make sure to provide accurate
				book details. Fields marked with <span class="highlight">*</span>
				are mandatory.
			</div>

			<!-- Display Success or Error Messages -->
			<div class="message-container">
				<c:if test="${not empty successMessage}">
					<div class="success-message">${successMessage}</div>
				</c:if>
				<c:if test="${not empty errorMessage}">
					<div class="error-message">${errorMessage}</div>
				</c:if>
			</div>

			<form action="${pageContext.request.contextPath}/adminproduct"
				method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="book_title"><i class="fas fa-book"></i> Book
						Title <span class="highlight">*</span></label> <input type="text"
						id="book_title" name="book_title" placeholder="Enter book title"
						required>
				</div>

				<div class="form-group">
					<label for="isbn"><i class="fas fa-barcode"></i> ISBN <span
						class="highlight">*</span></label> <input type="text" id="isbn"
						name="isbn" placeholder="Enter ISBN" required>
				</div>

				<div class="form-group">
					<label for="publication_date"><i
						class="fas fa-calendar-alt"></i> Publication Date <span
						class="highlight">*</span></label> <input type="date"
						id="publication_date" name="publication_date" required>
				</div>

				<div class="form-group">
					<label for="price"><i class="fas fa-dollar-sign"></i> Price
						<span class="highlight">*</span></label> <input type="number" id="price"
						name="price" placeholder="Enter price" step="0.01" required>
				</div>

				<div class="form-group">
					<label for="description"><i class="fas fa-align-left"></i>
						Description</label>
					<textarea id="description" name="description"
						placeholder="Enter book description"></textarea>
				</div>

				<div class="form-group">
					<label for="stock_quantity"><i class="fas fa-boxes"></i>
						Stock Quantity <span class="highlight">*</span></label> <input
						type="number" id="stock_quantity" name="stock_quantity"
						placeholder="Enter stock quantity" required>
				</div>

				<div class="form-group">
					<label for="page_count"><i class="fas fa-file-alt"></i>
						Page Count</label> <input type="number" id="page_count" name="page_count"
						placeholder="Enter number of pages">
				</div>

				<div class="form-group">
					<label for="publisherID">Select Publisher:</label> <select
						id="publisherID" name="publisherID" required>
						<option value="" disabled selected>Select a publisher</option>
						<c:forEach var="publisher" items="${publishers}">
							<option value="${publisher.publisherID}">${publisher.publisherName}</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group">
					<label for="authorID"><i class="fas fa-user"></i> Author(s)
						<span class="highlight">*</span></label> <select id="authorID"
						name="authorID" required>
						<option value="" disabled>Select an author</option>
						<c:forEach var="author" items="${authors}">
							<option value="${author.authorId}">${author.authorName}</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group">
					<label for="categoryID"><i class="fas fa-folder"></i>
						Category <span class="highlight">*</span></label> <select id="categoryID"
						name="categoryID" required>
						<option value="" disabled selected>Select a category</option>
						<c:forEach var="category" items="${categories}">
							<option value="${category.categoryId}">${category.categoryName}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label for="book_img_url"><i class="fas fa-image"></i> Book
						Image</label> <input type="file" id="book_img_url" name="book_img_url"
						accept="image/*">
				</div>

				<div class="form-actions">
					<button type="submit">Add Book</button>
				</div>
			</form>
		</div>
	</div>
	<jsp:include page="footer.jsp" />
</body>

</html>