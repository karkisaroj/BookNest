<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.booknest.config.DbConfiguration"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Account</title>
<!-- Link to the external CSS file -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/myaccount.css" />
</head>
<body>
	<!-- Include header -->
	<jsp:include page="/WEB-INF/pages/header.jsp" />

	<%
	// Retrieve user information from session
	String userName = (String) session.getAttribute("userName");
	String firstName = (String) session.getAttribute("firstName");
	String lastName = (String) session.getAttribute("lastName");
	String email = (String) session.getAttribute("email");
	String phoneNumber = (String) session.getAttribute("phoneNumber");
	String address = (String) session.getAttribute("address");

	if (userName == null) {
		response.sendRedirect(request.getContextPath() + "/login");
		return;
	}

	// Retrieve profile image URL from the database from column 'user_img_url'
	String profileImageUrl = "resources/images/system/default.png"; // Fallback default image
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		conn = DbConfiguration.getDbConnection();
		String sql = "SELECT user_img_url FROM user WHERE user_name = ?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, userName);
		rs = ps.executeQuery();
		if (rs.next()) {
			String dbImageUrl = rs.getString("user_img_url");
			if (dbImageUrl != null && !dbImageUrl.trim().isEmpty()) {
		profileImageUrl = dbImageUrl;
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (rs != null)
			try {
		rs.close();
			} catch (Exception ignore) {
			}
		if (ps != null)
			try {
		ps.close();
			} catch (Exception ignore) {
			}
		if (conn != null)
			try {
		conn.close();
			} catch (Exception ignore) {
			}
	}
	%>

	<div class="account-container">
		<h1 class="account-title">My Account</h1>
		<div class="account-content">
			<!-- Sidebar Navigation -->
			<div class="account-sidebar">
				<ul class="sidebar-menu">
					<li class="sidebar-item active"><span class="sidebar-icon">👤</span>
						My Details</li>
					<li class="sidebar-item"><span class="sidebar-icon">📦</span>
						My Orders</li>
					<li class="sidebar-item"><span class="sidebar-icon">⚙️</span>
						Account Setting</li>
				</ul>
			</div>
			<!-- Details Panel -->
			<div class="details-panel">
				<h2 class="details-title">My Details</h2>
				<!-- Profile Section -->
				<div class="profile-section">
					<div class="profile-image" id="profilePreview">
						<img id="previewImg"
							src="${pageContext.request.contextPath}/<%= profileImageUrl %>"
							alt="Profile Picture">
						<h3>
							 ---large file size</h3>

					</div>
					<!-- Upload Form -->
					<form
						action="${pageContext.request.contextPath}/uploadProfilePicture"
						method="post" enctype="multipart/form-data">
						<input type="file" name="image" id="imageInput" accept="image/*"
							required> <br>
						<button type="submit" class="upload-btn">UPLOAD</button>
					</form>



				</div>
				<!-- Personal Information -->
				<div class="info-section">
					<h3 class="info-section-title">Personal Information</h3>
					<div class="info-row">
						<div class="info-label">Username:</div>
						<div class="info-value"><%=userName%></div>
					</div>
					<div class="info-row">
						<div class="info-label">First Name:</div>
						<div class="info-value"><%=firstName%></div>
					</div>
					<div class="info-row">
						<div class="info-label">Last Name:</div>
						<div class="info-value"><%=lastName%></div>
					</div>
					<div class="info-row">
						<div class="info-label">Phone Number:</div>
						<div class="info-value"><%=phoneNumber%></div>
					</div>
					<div class="info-row">
						<div class="info-label">Address:</div>
						<div class="info-value"><%=address%></div>
					</div>
				</div>
				<!-- Email Information -->
				<div class="info-section">
					<h3 class="info-section-title">Email Information</h3>
					<div class="info-row">
						<div class="info-label">Email Address:</div>
						<div class="info-value"><%=email%></div>
					</div>
				</div>
				<!-- Action Buttons -->
				<div class="edit-button-container">
					<button class="edit-btn" onclick="location.href='editAccount.jsp'">EDIT
						Details</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Include footer -->
	<jsp:include page="/WEB-INF/pages/footer.jsp" />

	<script>
    const imageInput = document.getElementById('imageInput');
    const previewImg = document.getElementById('previewImg');

    imageInput.addEventListener('change', function() {
        const file = this.files[0]; // Get the selected file
        if (file) {
            if (file.size > 3 * 1024 * 1024) { // 3 MB size limit
                alert("File size exceeds the maximum allowed size of 3 MB.");
                imageInput.value = ""; // Reset file input
                previewImg.setAttribute('src', ''); // Reset preview
                return;
            }
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImg.setAttribute('src', e.target.result);
            };
            reader.readAsDataURL(file);
        }
    });
</script>


</body>
</html>
