<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNext - Account Settings</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/account-styles.css">

</head>
<body>
    <header>
        <div class="logo">
            <img src="${pageContext.request.contextPath}/resources/images/system/booknext-logo.png" alt="BookNext">
        </div>
        <nav class="nav-links">
            <a href="#">New Arrivals</a>
            <a href="#">Books</a>
            <a href="#">Kids</a>
            <div class="icons">
                <a href="#"><i class="fa fa-search"></i></a>
                <a href="#"><i class="fa fa-shopping-cart"></i></a>
                <a href="#"><i class="fa fa-user"></i></a>
            </div>
        </nav>
    </header>d

    <main>
        <aside class="sidebar">
            <ul>
                <li><a href="#">My Details</a></li>
                <li><a href="#">My Orders</a></li>
                <li><a href="#">Account Settings</a></li>
            </ul>
        </aside>

        <section class="account-settings">
            <h1>Account Settings</h1>

            <div class="profile-picture">
                <img src="${pageContext.request.contextPath}/resources/images/system/profile-placeholder.png" alt="Profile Picture">
                <button>Edit</button>
            </div>

            <form>
                <fieldset>
                    <legend>Update Account Information</legend>
                    <label for="first-name">First Name</label>
                    <input type="text" id="first-name" name="first-name">
                    
                    <label for="second-name">Second Name</label>
                    <input type="text" id="second-name" name="second-name">
                    
                    <label for="phone-number">Phone Number</label>
                    <input type="text" id="phone-number" name="phone-number">
                    
                    <button type="submit">Save</button>
                </fieldset>

                <fieldset>
                    <legend>Update Email Information</legend>
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email">
                    
                    <label for="confirm-email">Confirm Email Address</label>
                    <input type="email" id="confirm-email" name="confirm-email">
                    
                    <button type="submit">Save</button>
                </fieldset>

                <fieldset>
                    <legend>Update Password</legend>
                    <label for="current-password">Current Password</label>
                    <input type="password" id="current-password" name="current-password">
                    
                    <label for="new-password">New Password</label>
                    <input type="password" id="new-password" name="new-password">
                    
                    <label for="confirm-new-password">Confirm New Password</label>
                    <input type="password" id="confirm-new-password" name="confirm-new-password">
                    
                    <button type="submit">Save</button>
                </fieldset>
            </form>
        </section>
    </main>

    <footer>
        <div class="footer-container">
            <div class="footer-logo">
                <img src="${pageContext.request.contextPath}/resources/images/system/booknext-logo.png" alt="BookNext">
            </div>

            <div class="footer-links">
                <div class="footer-column">
                    <h4>Read</h4>
                    <a href="#">Links</a>
                    <a href="#">Links</a>
                    <a href="#">Links</a>
                    <a href="#">Links</a>
                </div>
                <div class="footer-column">
                    <h4>Explore</h4>
                    <a href="#">Links</a>
                    <a href="#">Links</a>
                    <a href="#">Links</a>
                    <a href="#">Links</a>
                </div>
            </div>

            <div class="footer-bottom">
                <div class="copyright">© 2015 - 2025 BookNext</div>
                <div class="payment-methods">
                    <img src="${pageContext.request.contextPath}/resources/images/system/visa.png" alt="Visa">
                    <img src="${pageContext.request.contextPath}/resources/images/system/mastercard.png" alt="MasterCard">
                    <img src="${pageContext.request.contextPath}/resources/images/system/paypal.png" alt="PayPal">
                </div>
                <div class="social-icons">
                    <a href="#"><i class="fa fa-facebook"></i></a>
                    <a href="#"><i class="fa fa-instagram"></i></a>
                    <a href="#"><i class="fa fa-whatsapp"></i></a>
                </div>
            </div>
        </div>
    </footer>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js"></script>
</body>
</html>
