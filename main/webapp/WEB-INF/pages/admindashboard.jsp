<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admindashboard.css">

    <script>
        // JavaScript function to confirm deletion
        function confirmDelete(event, form) {
            event.preventDefault(); // Prevent the form from submitting immediately

            // Show a confirmation dialog
            let confirmation = confirm("Are you sure you want to delete this book?");
            if (confirmation) {
                form.submit(); // Submit the form if the user confirms
            }
        }
    </script>
</head>

<body>
    <!-- Include Header -->
    <jsp:include page="header.jsp" />
    <div class="container1">
        <!-- Sidebar -->
        <jsp:include page="dashboard.jsp" />

        <!-- Main Content -->
        <main>
            <div class="content-wrapper">
                <h1>Admin Dashboard</h1>
                <section class="store-overview">
                    <div class="overview-item">
                        <p>Rs 12,000</p>
                        <p>BookNest Sales</p>
                    </div>
                    <div class="overview-item">
                        <p>Rs 45,000</p>
                        <p>BookNest Orders</p>
                    </div>
                    <div class="overview-item">
                        <p>Rs 85,000</p>
                        <p>Store Earnings</p>
                    </div>
                </section>

                <section class="best-selling">
                    <h2>Best Selling Books</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Book Name</th>
                                <th>No. of Sold Books</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Harry Potter</td>
                                <td>12 pcs</td>
                            </tr>
                            <tr>
                                <td>The Hobbit</td>
                                <td>10 pcs</td>
                            </tr>
                            <tr>
                                <td>1984</td>
                                <td>8 pcs</td>
                            </tr>
                        </tbody>
                    </table>
                </section>

                <!-- New Section: Books Management -->
                <section class="books-management">
                    <h2>Books Management</h2>

                    <!-- Display Success or Error Messages -->
                    <div class="message-container">
                        <%
                        if (request.getAttribute("success") != null) { 
                        %>
                            <div class="success-message">
                                <%= request.getAttribute("success") %>
                            </div>
                        <%
                        } else if (request.getAttribute("error") != null) { 
                        %>
                            <div class="error-message">
                                <%= request.getAttribute("error") %>
                            </div>
                        <%
                        } 
                        %>
                    </div>

                    <div class="books-table-container">
                        <h2>Book List</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Book Image</th>
                                    <th>Book Title</th>
                                    <th class="id-column">ID</th>
                                    <th>ISBN</th>
                                    <th>Stock Quantity</th>
                                    <th>Price</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty books}">
                                        <c:forEach var="book" items="${books}">
                                            <tr>
                                                <!-- Book Image -->
                                                <td>
                                                    <img src="${book.bookImgUrl}" alt="${book.bookTitle}" class="book-image">
                                                </td>
                                                <td>${book.bookTitle}</td>
                                                <td>${book.bookID}</td>
                                                <td>${book.isbn}</td>
                                                <td>${book.stockQuantity}</td>
                                                <td>${book.price}</td>
                                                <td>
                                                    <!-- Update Stock Form -->
                                                    <form method="post" action="${pageContext.request.contextPath}/admindashboard" style="display: inline;">
                                                        <input type="hidden" name="action" value="updateStock" />
                                                        <input type="hidden" name="bookID" value="${book.bookID}" />
                                                        <input type="number" name="newStock" min="0" placeholder="New Stock" required />
                                                        <button type="submit" class="edit-btn">Update Stock</button>
                                                    </form>

                                                    <!-- Delete Book Form -->
                                                    <form method="post" action="${pageContext.request.contextPath}/admindashboard" style="display: inline;" onsubmit="confirmDelete(event, this);">
                                                        <input type="hidden" name="action" value="deleteBook" />
                                                        <input type="hidden" name="bookID" value="${book.bookID}" />
                                                        <button type="submit" class="delete-btn">Delete</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7">No books found</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </section>
            </div>
        </main>
    </div>
    <jsp:include page="footer.jsp" />
</body>

</html>