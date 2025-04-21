<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNest - Customers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <header>
        <div class="container">
            <div class="header-right">
                <button class="icon-button search-button"><i class="fas fa-search"></i></button>
                <button class="icon-button cart-button"><i class="fas fa-shopping-cart"></i></button>
                <button class="icon-button user-button"><i class="fas fa-user"></i></button>
            </div>
        </div>
    </header>

    <main class="container">
        <h1>Customers</h1>
        
        <div class="content-wrapper">
            <aside class="sidebar">
                <nav>
                    <ul>
                        <li><a href="#">Dashboard</a></li>
                        <li class="active"><a href="#">Customers</a></li>
                        <li><a href="#">Products</a></li>
                        <li><a href="#">Order</a></li>
                    </ul>
                </nav>
            </aside>
            
            <div class="main-content">
                <table class="customers-table">
                    <thead>
                        <tr>
                            <th><input type="checkbox"></th>
                            <th>Name</th>
                            <th>ID</th>
                            <th>Address</th>
                            <th>Email</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>

                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        

                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>

                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        

                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        

                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        


                        
                        <tr>
                            <td><input type="checkbox"></td>
                            <td>Roshan Karki</td>
                            <td>101</td>
                            <td>Lalitpur</td>
                            <td>roshan@gmail.com</td>
                            <td><button class="edit-button"><i class="fas fa-edit"></i> Edit</button></td>
                        </tr>
                        <!-- Repeat rows 17 more times -->
                        <!-- Rows 3-19 omitted for brevity but would be identical -->
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <footer>
        <div class="container">
            <div class="footer-top">
                <div class="logo-container">
                    <img src="logo.png" alt="BookNest Logo" class="logo">
                </div>
                <div class="copyright">
                    © 2025 - 2026 BookNest
                </div>
            </div>
            
            <div class="footer-middle">
                <div class="footer-column">
                    <h3>Head</h3>
                    <ul>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                    </ul>
                </div>
                <div class="footer-column">
                    <h3>Head</h3>
                    <ul>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                    </ul>
                </div>
                <div class="footer-column">
                    <h3>Head</h3>
                    <ul>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                        <li><a href="#">Links</a></li>
                    </ul>
                </div>
                <div class="payment-methods">
                    <img src="/api/placeholder/150/40" alt="Payment methods">
                </div>
            </div>
            
            <div class="footer-bottom">
                <div class="social-icons">
                    <a href="#"><i class="fab fa-facebook-f"></i></a>
                    <a href="#"><i class="fab fa-instagram"></i></a>
                    <a href="#"><i class="fab fa-whatsapp"></i></a>
                </div>
            </div>
        </div>
    </footer>
</body>
</html>