<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BookNest Admin - Customers</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/customer.css">



<script>
    // JavaScript function to confirm deletion
    function confirmDelete(event, form) {
        event.preventDefault(); // Prevent the form from submitting immediately

        // Show a confirmation dialog
        let confirmation = confirm("Are you sure you want to delete this customer?");
        if (confirmation) {
            form.submit(); // Submit the form if the user confirms
        }
    }
</script>
</head>

<body>
    <jsp:include page="header.jsp" />

    <div class="admin-dashboard-container">
        <jsp:include page="dashboard.jsp" />
        <div class="main-content">
            <div class="header">
                <h1>Customers</h1>
            </div>
			
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

            <div class="customers-table-container">
                <h2>Customer List</h2>
                <table>
                    <thead>
                        <tr>
                            <th class="checkbox-column"><input type="checkbox"></th>
                            <th>Name</th>
                            <th class="id-column">ID</th>
                            <th>Address</th>
                            <th class="email-column">Email</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty customers}">
                                <c:forEach var="customer" items="${customers}">
                                    <tr>
                                        <td><input type="checkbox"></td>
                                        <td>${customer.firstName} ${customer.lastName}</td>
                                        <td>${customer.userName}</td>
                                        <td>${customer.address}</td>
                                        <td>${customer.email}</td>
                                        <td>
                                            <form method="post"
                                                action="${pageContext.request.contextPath}/admincustomer"
                                                style="display: inline;"
                                                onsubmit="confirmDelete(event, this);">
                                                <input type="hidden" name="userId"
                                                    value="${customer.userName}" />
                                                <button type="submit" class="edit-btn">Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6">No customers found</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
</body>

</html>