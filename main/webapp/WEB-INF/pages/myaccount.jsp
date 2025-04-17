<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Account</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/myaccount.css" />
</head>
<body>
   <jsp:include page="header.jsp" />

    <div class="account-container">
        <h1 class="account-title">My Account</h1>
        
        <div class="account-content">
            <!-- Sidebar navigation -->
            <nav class="account-sidebar">
                <ul class="sidebar-menu">
                    <li class="sidebar-item active">
                        <span class="sidebar-icon">👤</span>
                        <span>My Details</span>
                    </li>
                    <li class="sidebar-item">
                        <span class="sidebar-icon">📦</span>
                        <span>My Orders</span>
                    </li>
                    <li class="sidebar-item">
                        <span class="sidebar-icon">⚙️</span>
                        <span>Account Setting</span>
                    </li>
                </ul>
            </nav>
            
            <!-- Details panel -->
            <div class="details-panel">
                <h2 class="details-title">My Details</h2>
                
                <!-- Profile image section -->
                <div class="profile-section">
                    <div class="profile-image"></div>
                    <button class="upload-btn">UPLOAD</button>
                </div>
                
                <!-- Personal information section -->
                <div class="info-section">
                    <h3 class="info-section-title">Personal Information</h3>
                    
                    <div class="info-row">
                        <div class="info-label">First name:</div>
                        <div class="info-value">Saroj</div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">Last name:</div>
                        <div class="info-value">Karki</div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">Phone Number:</div>
                        <div class="info-value">9812345678</div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">Address:</div>
                        <div class="info-value">9812345678</div>
                    </div>
                </div>
                
                <!-- Email information section -->
                <div class="info-section">
                    <h3 class="info-section-title">Email Information</h3>
                    
                    <div class="info-row">
                        <div class="info-label">Email Address</div>
                        <div class="info-value">saroj@gmail.com</div>
                    </div>
                </div>
                
                <!-- Edit button -->
                <div class="edit-button-container">
                    <button class="edit-btn">EDIT Details</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
    
</body>
</html>