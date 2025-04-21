<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
</head>

<body>
    <header>
        <div class="container flex">
            <img class="logo" src="" alt="logo">
            <nav>
                <ul class="navigation flex">
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/books">Books</a></li>
                    <li><a href="${pageContext.request.contextPath}/aboutus">About</a></li>
                    <li><a href="${pageContext.request.contextPath}/contactus">Contact Us</a></li>
                </ul>
            </nav>
            <div class="utils">
                <ul class="utils-lists flex">
                    <li><a href="#"><img class="head-img" src="${pageContext.request.contextPath}/resources/images/system/search.png" alt="search button"></a></li>
                    <li><a href="${pageContext.request.contextPath}/cart"><img class="head-img" src="${pageContext.request.contextPath}/resources/images/system/cart.png" alt="cart button"></a></li>
                    
                    <li><a href="${pageContext.request.contextPath}/myaccount"><img class="head-img" src="${pageContext.request.contextPath}/resources/images/system/person.png" alt="myaccount button" height="25px"></a></li>
                    
                </ul>
            </div>
        </div>
    </header>
</body>

</html>