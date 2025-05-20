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
                <p class="team-subtitle">The passionate minds behind BookNest who curate extraordinary literary experiences.</p>
            </div>
            
            <div class="team-grid">
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar"></div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Alex Morgan</h3>
                        <p class="member-role">Founder & Lead Curator</p>
                        <p class="member-bio">Literary enthusiast with a passion for connecting readers with life-changing stories. Specializes in contemporary fiction and literary classics.</p>
                    </div>
                </div>
                
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar"></div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Jordan Chen</h3>
                        <p class="member-role">Non-Fiction Specialist</p>
                        <p class="member-bio">History buff and science enthusiast who curates our non-fiction collection. Believes knowledge is the greatest adventure of all.</p>
                    </div>
                </div>
                
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar"></div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Taylor Reynolds</h3>
                        <p class="member-role">Children's Literature Expert</p>
                        <p class="member-bio">Former elementary teacher who knows how to spark the joy of reading in young minds. Curates our enchanting children's section.</p>
                    </div>
                </div>
                
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar"></div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Sam Patel</h3>
                        <p class="member-role">Rare Books Collector</p>
                        <p class="member-bio">Treasure hunter for literary gems with an eye for first editions and signed copies. Creates our special collections section.</p>
                    </div>
                </div>
                
                <div class="team-member">
                    <div class="member-image">
                        <div class="member-avatar"></div>
                        <div class="member-book">
                            <div class="book-spine"></div>
                            <div class="book-cover"></div>
                        </div>
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Casey Williams</h3>
                        <p class="member-role">Community Manager</p>
                        <p class="member-bio">The heart of our bookish community who organizes reading groups, author events, and makes everyone feel welcome at BookNest.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <jsp:include page="footer.jsp" />
</body>
</html>