<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admindashboard.css">
</head>
<body>
    <!-- Include Header -->
   <jsp:include page="header.jsp" />
	<div class=container1>
	    <!-- Sidebar -->
	    <div>
	    	<aside class="sidebar">
	        <ul>
	            <li><a href="#">Dashboard</a></li>
	            <li><a href="#">Customers</a></li>
	            <li><a href="#">Products</a></li>
	            <li><a href="#">Orders</a></li>
	        </ul>
	    </aside>
	    </div>
	    
	    <div>
	    	<!-- Main Content -->
	    <main>
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
	    </main>
	    </div>

    

    
	</div>
	<jsp:include page="footer.jsp" />

</body>
</html>