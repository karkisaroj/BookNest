<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Modern Header</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header.css">
</head>

<body>
	<header id="main-header">
		<div class="container">
			<div class="logo-container">
				<div class="logo">
					<span
						style="font-family: serif; font-style: italic; font-weight: bold; font-size: 22px;">BookNest</span>
				</div>
			</div>

			<button class="mobile-menu-btn" id="mobile-menu-toggle">
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
					viewBox="0 0 24 24" fill="none" stroke="currentColor"
					stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="3" y1="12" x2="21" y2="12"></line>
          <line x1="3" y1="6" x2="21" y2="6"></line>
          <line x1="3" y1="18" x2="21" y2="18"></line>
        </svg>
			</button>

			<nav id="main-nav">
				<ul class="navigation">
					<li><a href="${pageContext.request.contextPath}/home">Home</a></li>
					<li><a href="${pageContext.request.contextPath}/books">Books</a></li>
					<li><a href="${pageContext.request.contextPath}/aboutus">About</a></li>
					<li><a href="${pageContext.request.contextPath}/contactus">Contact
							Us</a></li>
				</ul>
			</nav>

			<!-- Search Panel -->
			<div class="search-panel" id="search-panel">
				<div class="search-container">
					<form class="search-form"
						action="${pageContext.request.contextPath}/search" method="GET"
						id="search-form">
						<input type="text" name="query" class="search-input"
							id="search-input" placeholder="Search for products..."
							autocomplete="off">
						<button type="submit" class="search-button">Search</button>
					</form>

					

					<div class="search-results-preview" id="search-results-preview">
						<div class="preview-title">
							<span>Recent Search Results</span> <a
								href="${pageContext.request.contextPath}/search">View all</a>
						</div>
						<div class="preview-items">
							<!-- Preview items will be loaded via JS -->
						</div>
					</div>
				</div>

				<button class="search-close" id="search-close">
					<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18"
						viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
				</button>
			</div>

			<div class="utils">
				<ul class="utils-lists">
					<li><a href="#" id="search-toggle" aria-label="Search"> <svg
								xmlns="http://www.w3.org/2000/svg" width="20" height="20"
								viewBox="0 0 24 24" fill="none" stroke="currentColor"
								stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
					</a></li>
					<li><a href="${pageContext.request.contextPath}/cart"
						aria-label="Cart"> <svg xmlns="http://www.w3.org/2000/svg"
								width="20" height="20" viewBox="0 0 24 24" fill="none"
								stroke="currentColor" stroke-width="2" stroke-linecap="round"
								stroke-linejoin="round">
                <circle cx="9" cy="21" r="1"></circle>
                <circle cx="20" cy="21" r="1"></circle>
                <path
									d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
              </svg>
					</a></li>
					<li><a href="${pageContext.request.contextPath}/myaccount"
						aria-label="My Account"> <svg
								xmlns="http://www.w3.org/2000/svg" width="20" height="20"
								viewBox="0 0 24 24" fill="none" stroke="currentColor"
								stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                <circle cx="12" cy="7" r="4"></circle>
              </svg>
					</a></li>
				</ul>
			</div>
		</div>
	</header>

	<script>
    // Mobile menu toggle
    const mobileMenuBtn = document.getElementById('mobile-menu-toggle');
    const mainNav = document.getElementById('main-nav');
    
    mobileMenuBtn.addEventListener('click', () => {
      mainNav.classList.toggle('active');
    });
    
    // Header scroll effect
    const header = document.getElementById('main-header');
    
    window.addEventListener('scroll', () => {
      if (window.scrollY > 10) {
        header.classList.add('scrolled');
      } else {
        header.classList.remove('scrolled');
      }
    });
    
    // Search functionality
    const searchToggle = document.getElementById('search-toggle');
    const searchPanel = document.getElementById('search-panel');
    const searchClose = document.getElementById('search-close');
    const searchInput = document.getElementById('search-input');
    const searchForm = document.getElementById('search-form');
    const searchResultsPreview = document.getElementById('search-results-preview');
    const categoryOptions = document.querySelectorAll('.category-option');
    
    // Toggle search panel
    searchToggle.addEventListener('click', (e) => {
      e.preventDefault();
      searchPanel.classList.add('active');
      searchInput.focus();
      
      // Close mobile menu if open
      mainNav.classList.remove('active');
    });
    
    // Close search panel
    searchClose.addEventListener('click', () => {
      searchPanel.classList.remove('active');
    });
    
    // Close search panel when clicking outside
    document.addEventListener('click', (e) => {
      if (!searchPanel.contains(e.target) && !searchToggle.contains(e.target) && searchPanel.classList.contains('active')) {
        searchPanel.classList.remove('active');
      }
    });
    
  
    // Live search functionality - without AJAX
    let searchTimeout;
    searchInput.addEventListener('input', () => {
      clearTimeout(searchTimeout);
      
      // Only search if there's text in the input
      if (searchInput.value.trim().length > 2) {
        searchTimeout = setTimeout(() => {
          fetchSearchResults(searchInput.value);
        }, 300);
      } else {
        searchResultsPreview.classList.remove('active');
      }
    });
    

    
    // Function to display search results
    function displaySearchResults(books) {
      const previewItemsContainer = document.querySelector('.preview-items');
      previewItemsContainer.innerHTML = '';
      
      if (books.length > 0) {
        // Show the results container
        searchResultsPreview.classList.add('active');
        
        // Add book items to the container
        books.forEach(book => {
          const bookItem = document.createElement('div');
          bookItem.className = 'preview-item';
          
          // FIXED: Use the correct property names (book_img_url, book_title)
          bookItem.innerHTML = `
            <div class="preview-item-img" style="background-image: url('${book.book_img_url}')"></div>
            <div class="preview-item-title">${book.book_title}</div>
            <div class="preview-item-price">Rs ${book.price}</div>
          `;
          
          // FIXED: Make the item link to the product page with the correct bookId
          bookItem.style.cursor = 'pointer';
          bookItem.addEventListener('click', () => {
            window.location.href = '${pageContext.request.contextPath}/product?bookId=' + book.bookID;
          });
          
          previewItemsContainer.appendChild(bookItem);
        });
      } else {
        // Show no results message
        searchResultsPreview.classList.add('active');
        previewItemsContainer.innerHTML = '<p style="grid-column: 1 / -1; text-align: center; color: #777;">No results found for your search.</p>';
      }
    }
  </script>
</body>
</html>