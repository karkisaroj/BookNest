<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- JSTL taglib --%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Account</title>

    <%-- Link specifically to myaccount.css from within this page --%>
    <link rel="stylesheet" href="<c:url value='/css/myaccount.css'/>" />

    <%-- Add any other specific head elements needed ONLY for this page --%>
    <%-- Note: Global styles/scripts should ideally be in header.jsp --%>

</head>
<body>
    <%-- Include Header --%>
    <jsp:include page="header.jsp" />

    <%-- Main Account Container --%>
    <div class="account-container">
        <h1 class="account-title">My Account</h1>

        <%-- Display Success/Error Messages Passed from Controller --%>
        <c:if test="${not empty successMessage}">
            <div class="message success">
                <c:out value="${successMessage}" />
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="message error">
                <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <div class="account-content">
            <!-- Sidebar Navigation -->
            <div class="account-sidebar">
                <ul class="sidebar-menu">
                    <li class="sidebar-item active">
                        <span class="sidebar-icon">👤</span>
                        <span><a href="<c:url value='/myaccount'/>">My Details</a></span>
                    </li>
                    <li class="sidebar-item">
                        <span class="sidebar-icon">📦</span>
                        <span><a href="<c:url value='/cart'/>">My Orders</a></span>
                    </li>
                    <li class="sidebar-item">
                        <span class="sidebar-icon">⚙️</span>
                        <span><a href="<c:url value='/accountsetting'/>">Account Setting</a></span>
                    </li>
                </ul>
            </div>

            <!-- Details Panel -->
            <div class="details-panel">
                <h2 class="details-title">My Details</h2>

                <!-- Profile Section -->
                <div class="profile-section">
                    <div class="profile-image" id="profilePreview">
                        <img id="previewImg" src="<c:url value='/${profileImageUrl}'/>"
                             alt="Profile Picture"
                             onerror="this.onerror=null; this.src='<c:url value="/resources/images/system/person.png"/>';">
                    </div>
                    <form action="<c:url value='/myaccount'/>" method="post" enctype="multipart/form-data">
                        <input type="file" name="image" id="imageInput" accept="image/*" required>
                        <p id="fileName" class="file-name"></p>
                        <img id="imagePreview" class="profile-preview" alt="Selected Image Preview" />
                        <button type="submit" class="upload-btn">UPLOAD</button>
                    </form>
                </div>

                <!-- Personal Information -->
                <div class="info-section">
                    <h3 class="info-section-title">Personal Information</h3>
                    <div class="info-row">
                        <div class="info-label">Username:</div>
                        <div class="info-value">
                            <c:out value="${userName}" />
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">First Name:</div>
                        <div class="info-value">
                            <c:out value="${firstName}" />
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Last Name:</div>
                        <div class="info-value">
                            <c:out value="${lastName}" />
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Phone Number:</div>
                        <div class="info-value">
                            <c:out value="${phoneNumber}" />
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Address:</div>
                        <div class="info-value">
                            <c:out value="${address}" />
                        </div>
                    </div>
                </div>

                <!-- Email Information -->
                <div class="info-section">
                    <h3 class="info-section-title">Email Information</h3>
                    <div class="info-row">
                        <div class="info-label">Email Address:</div>
                        <div class="info-value">
                            <c:out value="${email}" />
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="edit-button-container">
                    <form action="<c:url value='/logout'/>" method="post" class="logout-form">
                        <button type="submit" class="logout-btn">LOG OUT</button>
                    </form>
                    <button class="edit-btn" onclick="location.href='<c:url value="/accountsetting"/>'">EDIT Details</button>
                </div>
            </div>
        </div>
    </div>

    <%-- Include Footer --%>
    <jsp:include page="footer.jsp" />

    <script>
        // Display the file name and preview the image when a new file is chosen
        const fileInput = document.getElementById('imageInput');
        const fileNameDisplay = document.getElementById('fileName');
        const imagePreview = document.getElementById('imagePreview');

        fileInput.addEventListener('change', function () {
            if (fileInput.files.length > 0) {
                const file = fileInput.files[0];
                fileNameDisplay.textContent = `Selected file: ${file.name}`;

                const reader = new FileReader();
                reader.onload = function (e) {
                    imagePreview.src = e.target.result;
                    imagePreview.style.display = "block";
                };
                reader.readAsDataURL(file);
            } else {
                fileNameDisplay.textContent = '';
                imagePreview.style.display = "none";
            }
        });
    </script>
</body>
</html>