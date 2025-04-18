<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us | BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ContactUs.css">
</head>

<body>
	<jsp:include page="header.jsp" />
    <div class="container2">
        <section class="contact-form">
            <h1>Contact Us</h1>
            <p>We’re here to help! Whether you have questions about our books, your orders, or anything else, feel free to reach out to us. Fill in the form below, and we’ll get back to you as soon as possible.</p>
            <form>
                <label for="name">Your Name</label>
                <input type="text" id="name" name="name" placeholder="Enter your name">

                <label for="email">Your Email</label>
                <input type="email" id="email" name="email" placeholder="Enter your email address">

                <label for="message">Your Message</label>
                <textarea id="message" name="message" rows="6" placeholder="Write your message here"></textarea>

                <button type="submit">Send</button>
            </form>
        </section>
        
    </div>
    <jsp:include page="footer.jsp" />
</body>

</html>