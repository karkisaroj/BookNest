<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.booknest.model.PublisherModel"%>
<%@ page import="com.booknest.model.AuthorModel"%>
<%@ page import="com.booknest.model.CategoryModel"%>
<%@ page import="com.booknest.model.BookModel"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - <c:choose>
            <c:when test="${action == 'update'}">Update Product</c:when>
            <c:otherwise>Add Product</c:otherwise>
        </c:choose>
    </title>

    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/addproduct.css">
    <style>
        /* Styling for the image preview */
        .image-preview {
            margin-top: 10px;
            width: 150px; /* Set the width of the preview image */
            height: auto; /* Maintain aspect ratio */
            border: 1px solid #ddd;
            padding: 5px;
            display: none; /* Initially hide the preview */
        }
    </style>
</head>

<body>
    <jsp:include page="header.jsp" />
    <div class="add-product-page">
        <div class="form-container">
            <div class="form-header">
                <h1>
                    <c:choose>
                        <c:when test="${action == 'update'}">Update Book</c:when>
                        <c:otherwise>Add New Book</c:otherwise>
                    </c:choose>
                </h1>
                <p class="highlight">
                    <c:choose>
                        <c:when test="${action == 'update'}">
                            Complete the form to update the product details.
                        </c:when>
                        <c:otherwise>
                            Complete the form to add a new product to the catalog.
                        </c:otherwise>
                    </c:choose>
                </p>
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

            <form action="<c:choose>
                    <c:when test='${action == "update"}'>
                        ${pageContext.request.contextPath}/adminupdatebook
                    </c:when>
                    <c:otherwise>
                        ${pageContext.request.contextPath}/adminproduct
                    </c:otherwise>
                </c:choose>" method="post" enctype="multipart/form-data">

                <!-- Only include the hidden bookID for updates -->
                <c:if test="${action == 'update'}">
                    <input type="hidden" name="bookID" value="${book.bookID}" />
                </c:if>

                <div class="form-group">
                    <label for="book_title"><i class="fas fa-book"></i> Book
                        Title <span class="highlight">*</span></label>
                    <input type="text"
                        id="book_title" name="book_title" placeholder="Enter book title"
                        value="${book.bookTitle}" required>
                </div>

                <div class="form-group">
                    <label for="isbn"><i class="fas fa-barcode"></i> ISBN <span
                        class="highlight">*</span></label>
                    <input type="text" id="isbn"
                        name="isbn" placeholder="Enter ISBN" value="${book.isbn}" required>
                </div>

                <div class="form-group">
                    <label for="publication_date"><i
                        class="fas fa-calendar-alt"></i> Publication Date <span
                        class="highlight">*</span></label>
                    <input type="date"
                        id="publication_date" name="publication_date"
                        value="${book.publicationDate}" required>
                </div>

                <div class="form-group">
                    <label for="price"><i class="fas fa-dollar-sign"></i> Price
                        <span class="highlight">*</span></label>
                    <input type="number" id="price"
                        name="price" placeholder="Enter price" step="0.01"
                        value="${book.price}" required>
                </div>

                <div class="form-group">
                    <label for="description"><i class="fas fa-align-left"></i>
                        Description</label>
                    <textarea id="description" name="description"
                        placeholder="Enter book description">${book.description}</textarea>
                </div>

                <div class="form-group">
                    <label for="stock_quantity"><i class="fas fa-boxes"></i>
                        Stock Quantity <span class="highlight">*</span></label>
                    <input
                        type="number" id="stock_quantity" name="stock_quantity"
                        placeholder="Enter stock quantity" value="${book.stockQuantity}"
                        required>
                </div>

                <div class="form-group">
                    <label for="page_count"><i class="fas fa-file-alt"></i>
                        Page Count</label>
                    <input type="number" id="page_count" name="page_count"
                        placeholder="Enter number of pages" value="${book.pageCount}">
                </div>

                <div class="form-group">
                    <label for="publisherID">Select Publisher:</label>
                    <select
                        id="publisherID" name="publisherID" required>
                        <option value="" disabled>Select a publisher</option>
                        <c:forEach var="publisher" items="${publishers}">
                            <option value="${publisher.publisherID}"
                                <c:if test="${publisher.publisherID == book.publisherID}">selected</c:if>>
                                ${publisher.publisherName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="authorID"><i class="fas fa-user"></i> Author(s)
                        <span class="highlight">*</span></label>
                    <select id="authorID"
                        name="authorID" required>
                        <option value="" disabled>Select an author</option>
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.authorId}"
                                <c:if test="${!empty book.authorID && author.authorId == book.authorID}">selected</c:if>>
                                ${author.authorName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="categoryID"><i class="fas fa-folder"></i>
                        Category <span class="highlight">*</span></label>
                    <select id="categoryID"
                        name="categoryID" required>
                        <option value="" disabled>Select a category</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.categoryId}"
                                <c:if test="${!empty book.categoryID && category.categoryId == book.categoryID}">selected</c:if>>
                                ${category.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="book_img_url"><i class="fas fa-image"></i> Book
                        Image</label>
                    <input type="file" id="book_img_url" name="book_img_url" accept="image/*">
                    <p id="file-name" ></p>
                    <img id="image-preview" class="image-preview" alt="Image Preview" />
                    <c:if test="${not empty book.bookImgUrl}">
                        <img src="${pageContext.request.contextPath}/${book.bookImgUrl}"
                            alt="Book Image" class="image-preview" />
                    </c:if>
                </div>

                <div class="form-actions">
                    <button type="submit">
                        <c:choose>
                            <c:when test="${action == 'update'}">Update Book</c:when>
                            <c:otherwise>Add Book</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </div>
    <jsp:include page="footer.jsp" />

    <script>
        // Display the file name and preview the image when a new file is chosen
        const fileInput = document.getElementById('book_img_url');
        const fileNameDisplay = document.getElementById('file-name');
        const imagePreview = document.getElementById('image-preview');

        fileInput.addEventListener('change', function () {
            // If a file is selected, show its name and preview
            if (fileInput.files.length > 0) {
                const file = fileInput.files[0];
                fileNameDisplay.textContent = `Selected file: ${file.name}`;

                const reader = new FileReader();
                reader.onload = function (e) {
                    imagePreview.src = e.target.result;
                    imagePreview.style.display = "block"; // Show the preview
                };
                reader.readAsDataURL(file);
            } else {
                fileNameDisplay.textContent = ''; // Clear text if no file is selected
                imagePreview.style.display = "none"; // Hide the preview
            }
        });
    </script>
</body>

</html>