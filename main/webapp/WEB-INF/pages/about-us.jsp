<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us | BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about-us.css">
</head>
<body>
	<jsp:include page="header.jsp" />
    <!-- Hero Section -->
    <section class="about-hero">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="container">
            <span class="section-label">About Us</span>
            <h1 class="hero-title">Our Story</h1>
            <p class="hero-subtitle">Discover the passion and purpose behind BookNest, your literary sanctuary.</p>
        </div>
    </section>

    <!-- About Content Section -->
    <section class="about-content">
        <div class="container">
            <div class="content-card">
                <div class="story-section">
                    <h2 class="story-title">Welcome to BookNest</h2>
                    <p class="story-text">At BookNest, we're more than just a bookstore. We're a sanctuary for literary explorers and knowledge seekers. Founded with a passion for connecting readers with stories that inspire, challenge, and transform, we've curated a collection that spans genres, perspectives, and worlds.</p>
                    
                    <div class="divider"></div>
                    
                    <p class="story-text">Whether you're searching for the latest bestseller, a timeless classic, or hidden gems waiting to be discovered, our carefully selected inventory offers something for every reader. We believe books have the power to spark curiosity, joy, and profound understanding.</p>
                    
                    <p class="story-text">Our mission extends beyond simply selling books. We strive to foster a community where ideas can be shared, stories celebrated, and reading journeys enriched. Through our dedication to exceptional service and our love for literature, we aim to be your trusted companion in the wonderful world of reading.</p>
                </div>
                <div class="image-section">
                    <div class="book-stack">
                        <div class="book book-1"></div>
                        <div class="book book-2"></div>
                        <div class="book book-3"></div>
                        <div class="cat-silhouette"></div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    
    
    <!-- Team Section -->
    <section class="team-section" id="our-team">
        <div class="container">
            <div class="team-header">
                <span class="section-label">Our Team</span>
                <h2 class="team-title">Meet the Bookworms</h2>
                <p class="team-subtitle">The passionate creators of BookNest who built and shaped an exceptional online haven for book lovers.</p> 
            </div>
            
            <div class="team-grid">
            
            <!-- Team member 1 -->
                <div class="team-member">
    				<div class="member-image">
        				<div class="member-avatar">
            			<img src="${pageContext.request.contextPath}/resources/images/memberImage/saroj.jpeg" alt="Saroj Pratap Karki">
       	 				</div>
       		 		<div class="member-book">
            			<div class="book-spine"></div>
            			<div class="book-cover"></div>
        			</div>
    			</div>
    			<div class="member-info">
        			<h3 class="member-name">Saroj Pratap Karki</h3>
        			<p class="member-role">Leader & Backend developer</p>
        			<p class="member-bio">Chief Technology Officer & Lead Backend Developer Oversees technical strategy, backend architecture, and system security. Leads API development and cross-functional integration.</p>
    			</div>
				</div>
                
                <!-- Team member 2 -->
                <div class="team-member">
    				<div class="member-image">
        				<div class="member-avatar">
            				<img src="${pageContext.request.contextPath}/resources/images/memberImage/noble.jpg" alt="Noble Nepal">
        				</div>
        				<div class="member-book">
            				<div class="book-spine"></div>
            				<div class="book-cover"></div>
        				</div>
    				</div>
    				<div class="member-info">
        				<h3 class="member-name">Noble Nepal</h3>
        				<p class="member-role">Backend developer & database designer</p>
        				<p class="member-bio">Senior Backend Developer & Database Architecture Specialist
							Designs database systems powering recommendations and inventory. Optimizes data models and query performance for reliability.</p>
    				</div>
				</div>
                
                
                <!-- Team member 3 -->
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar">
                        	<img src="${pageContext.request.contextPath}/resources/images/memberImage/piyush.jpg" alt="Piyush Chand">
                        </div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Piyush Chand</h3>
                        <p class="member-role">Database designer</p>
                        <p class="member-bio">Database Administrator & Manages database operations, security, and children's content classification. Creates reporting tools and ensures data compliance.</p>
                    </div>
                </div>
                
                
                <!-- Team member 4 -->
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar">
                        	<img src="${pageContext.request.contextPath}/resources/images/memberImage/abin.jpg" alt="Abin Napit">
                        </div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Abin Napit</h3>
                        <p class="member-role">System Designer</p>
                        <p class="member-bio">Systems Architecture Designer maintains technical infrastructure, monitoring systems, and user friendly design.</p>
                    </div>
                </div>
                
                
                <!-- Team member 5 -->
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar">
                        	<img src="${pageContext.request.contextPath}/resources/images/memberImage/hrita.png" alt="Hrita Dutta">
                        </div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Hrita Dutta</h3>
                        <p class="member-role">Frontend developer</p>
                        <p class="member-bio">Lead Frontend Developer builds responsive interfaces. Manages frontend workflows and user experience improvements.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <jsp:include page="footer.jsp" />
</body>
</html>