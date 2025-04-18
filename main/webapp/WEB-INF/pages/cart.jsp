<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Your Cart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css" />

</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container1">
        <div class="cart-heading">
            <h1>Your cart</h1>
        </div>
        
        <table class="cart-table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <div class="book-item">
                            <img src="resources/images/system/pichai-book.jpg" alt="Pichai Book" class="book-image">
                            <div class="book-details">
                                <h3>Jagmohan S. Bhanver</h3>
                                <p>Changing Face of Microsoft</p>
                                <div class="price">Rs.1,600.00</div>
                            </div>
                        </div>
                    </td>
                    <td>
                        <button class="remove-btn">REMOVE</button>
                    </td>
                    <td>Rs.1,600.00</td>
                </tr>
                <tr>
                    <td>
                        <div class="book-item">
                            <img src="resources/images/system/psychology-money.jpg" alt="Psychology of Money" class="book-image">
                            <div class="book-details">
                                <h3>James Clear</h3>
                                <p>The Psychology Of Money</p>
                                <div class="price">Rs.1,600.00</div>
                            </div>
                        </div>
                    </td>
                    <td>
                        <button class="remove-btn">REMOVE</button>
                    </td>
                    <td>Rs.1,600.00</td>
                </tr>
                <tr>
                    <td>
                        <div class="book-item">
                            <img src="resources/images/system/Nandela-book.jpg" alt="Nandela Book" class="book-image">
                            <div class="book-details">
                                <h3>Jagmohan S. Bhanver</h3>
                                <p>Future Garage</p>
                                <div class="price">Rs.1,600.00</div>
                            </div>
                        </div>
                    </td>
                    <td>
                        <button class="remove-btn">REMOVE</button>
                    </td>
                    <td>Rs.1,600.00</td>
                </tr>
            </tbody>
        </table>
        
        <a href="#" class="continue-shopping">Continue Shopping →</a>
        
        <div class="cart-summary">
            <div class="total">Estimated total: <strong>Rs.5,120.00</strong></div>
            <div class="taxes">Taxes, discounts and shipping calculated at checkout.</div>
            <button class="checkout-btn">Check Out</button>
        </div>
    </div>
    
    <!-- Font Awesome for icons -->
    <!--<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js"></script> -->
    <jsp:include page="footer.jsp" />
</body>
</html>