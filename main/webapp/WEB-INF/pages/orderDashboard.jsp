<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNest - Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderDashboard.css">
</head>
<body>
    <!-- Header section -->
    <header>
        <div class="logo-container">
            <img src="/api/placeholder/150/50" alt="BookNest Logo" class="logo">
        </div>
        <nav>
            <ul>
                <li><a href="#">New Arrivals</a></li>
                <li><a href="#">Books</a></li>
                <li><a href="#">Kids</a></li>
            </ul>
        </nav>
        <div class="right-nav">
            <a href="#"><i class="fas fa-search">🔍</i></a>
            <a href="#"><i class="fas fa-shopping-cart">🛒</i></a>
            <a href="#"><i class="fas fa-user">👤</i></a>
        </div>
    </header>

    <!-- Main content section -->
    <main>
        <div class="sidebar">
            <h3>Links</h3>
            <ul>
                <li><a href="#">Dashboard</a></li>
                <li><a href="#">Customers</a></li>
                <li><a href="#">Products</a></li>
                <li><a href="#">Order</a></li>
            </ul>
        </div>
        <div class="content">
            <h1>Orders</h1>
            <table>
                <thead>
                    <tr>
                        <th><input type="checkbox"></th>
                        <th>Name</th>
                        <th>ID</th>
                        <th>Delivery Address</th>
                        <th>Status</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Repeat rows for orders -->
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>Roshan Karki</td>
                        <td>101</td>
                        <td>Lalitpur</td>
                        <td>roshan@gmail.com</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </main>

    <!-- Footer section -->
    <footer>
        <div class="footer-content">
            <div class="footer-top">
                <img src="/api/placeholder/150/50" alt="BookNest Logo" class="footer-logo">
                <div class="payment-methods">
                    <img src="/api/placeholder/200/35" alt="Payment Methods">
                </div>
            </div>
            <div class="footer-links">
                <div class="footer-links-column">
                    <h4>Head</h4>
                    <ul>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                    </ul>
                </div>
                <div class="footer-links-column">
                    <h4>Head</h4>
                    <ul>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                    </ul>
                </div>
                <div class="footer-links-column">
                    <h4>Head</h4>
                    <ul>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                <p>© 2025 - 2026 BookNest</p>
                <div class="social-icons">
                    <a href="#"><i class="fab fa-facebook">📘</i></a>
                    <a href="#"><i class="fab fa-instagram">📷</i></a>
                    <a href="#"><i class="fab fa-whatsapp">💬</i></a>
                </div>
            </div>
        </div>
    </footer>
</body>
</html>