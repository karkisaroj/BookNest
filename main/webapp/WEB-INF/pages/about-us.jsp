<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	 <jsp:include page="header.jsp" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us | BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about-us.css">
</head>
<div class="container">
    <section class="about-us">
        <h1>About Us</h1>
        <p>Welcome to BookNest, your ultimate destination for book lovers! We specialize in selling new and popular books to cater to readers of all interests and genres. Whether you’re searching for the latest bestseller, a timeless classic, or hidden gems, BookNest offers a wide selection to inspire your reading journey.</p>
        <p>Our goal is to connect people to books that spark curiosity, joy, and knowledge. Dive into our collection and discover the stories waiting for you. Happy reading!</p>
    </section>
    <section class="image-section">
        <img src="${pageContext.request.contextPath}/resources/images/system/Nandela-book.jpg" alt="About Us Image">
    </section>
</div>
<jsp:include page="footer.jsp" />
</html>