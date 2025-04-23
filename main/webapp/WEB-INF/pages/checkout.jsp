<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNest - Checkout</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkout.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="main-content">
        <div class="shipping-section">
            <h1>Checkout</h1>
            
            <h2>Shipping Information</h2>
            
            <div class="delivery-options">
                <label class="delivery-option">
                    <input type="radio" name="delivery-type" checked>
                    <span>Delivery</span>
                </label>
                <label class="delivery-option">
                    <input type="radio" name="delivery-type">
                    <span>Pick Up</span>
                </label>
            </div>
            
            <div class="form-group">
                <label>First name</label>
                <input type="text" class="form-control" placeholder="Enter your first name">
            </div>
            
            <div class="form-group">
                <label>Email Address</label>
                <input type="email" class="form-control" placeholder="Enter your email address">
            </div>
            
            <div class="form-group">
                <label>Phone Number *</label>
                <input type="tel" class="form-control" placeholder="Enter your phone number">
            </div>
            
            <div class="form-group">
                <label>Country *</label>
                <input type="text" class="form-control" placeholder="Enter your country name">
            </div>
            
            <div class="terms-checkbox">
                <input type="checkbox" id="terms">
                <label for="terms">I have read and agree to the Terms and Conditions.</label>
            </div>
        </div>
        
        <div class="review-section">
            <h2>Review Your Cart</h2>
            
            <div class="cart-item">
                <img src="book-cover.jpg" alt="Sri Siddhi Ma">
                <div class="cart-item-details">
                    <p>Sri Siddhi Ma</p>
                    <p>1x</p>
                    <p>Rs. 1,600.00</p>
                </div>
            </div>
            
            <div class="price-summary">
                <div class="price-row">
                    <div>Subtotal</div>
                    <div>Rs. 1600.00</div>
                </div>
                <div class="price-row">
                    <div>Shipping</div>
                    <div>Rs. 100.00</div>
                </div>
                <div class="price-row">
                    <div>Discount</div>
                    <div>Rs. 200.00</div>
                </div>
                <div class="price-row total-row">
                    <div>Total</div>
                    <div>Rs. 1,500.00</div>
                </div>
            </div>
            
            <button class="btn btn-primary">Pay Now</button>
            <button class="btn btn-secondary">Cancel</button>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>