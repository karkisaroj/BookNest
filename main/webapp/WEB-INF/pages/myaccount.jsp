<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Account</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/myaccount.css" />
</head>
<body>
	<jsp:include page="header.jsp" />
	<%
	String errorMessage = (String) request.getAttribute("errorMessage");
	if (errorMessage != null) {
	%>
	<script>
        alert('<%=errorMessage%>
		');
	</script>
	<%
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
							src="${pageContext.request.contextPath}/<%= (String) request.getAttribute("profileImageUrl") %>"
							alt="Profile Picture">
					</div>
					<form action="${pageContext.request.contextPath}/myaccount"
						method="post" enctype="multipart/form-data">
						<input type="file" name="image" id="imageInput" accept="image/*"
							required> <br>
						<button type="submit" class="upload-btn">UPLOAD</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp" />
</body>
<jsp:include page="footer.jsp" />
</html>
