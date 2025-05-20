<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>



        <!-- Admin Sidebar -->
<div class="admin-sidebar">
    <div class="sidebar-brand">
        <h2>BookNest Admin</h2>
    </div>
    <ul class="sidebar-menu">
        <li>
            <a href="${pageContext.request.contextPath}/admindashboard" class="${pageContext.request.servletPath eq '/WEB-INF/views/admindashboard.jsp' ? 'active' : ''}">
                <i class="fas fa-tachometer-alt"></i>
                <span>Dashboard</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/admincustomer" class="${pageContext.request.servletPath eq '/WEB-INF/views/admincustomer.jsp' ? 'active' : ''}">
                <i class="fas fa-users"></i>
                <span>Customers</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/adminproduct" class="${pageContext.request.servletPath eq '/WEB-INF/views/adminproduct.jsp' ? 'active' : ''}">
                <i class="fas fa-book"></i>
                <span>Add Books</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/adminorder" class="${pageContext.request.servletPath eq '/WEB-INF/views/adminorder.jsp' ? 'active' : ''}">
                <i class="fas fa-shopping-cart"></i>
                <span>Orders</span>
            </a>
        </li>
    </ul>
</div>


</body>
</html>