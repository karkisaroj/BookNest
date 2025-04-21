<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNest Admin - Customers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customer.css">
</head>

<body>
	<jsp:include page="header.jsp" />
    <!-- You can add your header here -->

    <div class="admin-dashboard-container">
        <jsp:include page="dashboard.jsp" />

        <div class="main-content">
            <div class="header">
                <h1>Customers</h1>
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
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>01</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>02</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>03</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>04</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>05</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>06</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>07</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>08</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>09</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>10</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-btn">Edit</button></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
	<jsp:include page="footer.jsp" />
    <!-- You can add your footer here -->
</body>

</html>