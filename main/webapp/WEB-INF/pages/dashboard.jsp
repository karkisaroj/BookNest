<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
        <div class="sidebar">
            <ul class="sidebar-menu">
                <li><a href="${pageContext.request.contextPath}/admindashboard">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admincustomer">Customers</a></li>
                <li><a href="${pageContext.request.contextPath}/adminproduct">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/adminorder">Orders</a></li>
            </ul>
        </div>
</body>
</html>