package com.booknest.controller; // Assuming this is your controller package

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.booknest.model.BookCartModel;
import com.booknest.service.BookService;
import com.booknest.service.BookServiceImpl;

@WebServlet(asyncSupported = true, urlPatterns = { "/home", "/" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService;

	@Override
	public void init() throws ServletException {
		this.bookService = new BookServiceImpl();
		System.out.println("HomeController Initialized.");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("HomeController doGet called.");

		List<BookCartModel> booksForBookSection = Collections.emptyList();
		List<BookCartModel> booksForPopularSection = Collections.emptyList();
		boolean errorOccurred = false;
		String errorMessage = null;

		// 1. Fetch Random Books for the "Books" section
		try {
			booksForBookSection = bookService.getRandomBooks(4);
			request.setAttribute("randomBooks", booksForBookSection);
			System.out.println(
					"HomeController: Fetched " + booksForBookSection.size() + " random books for 'Books' section.");
		} catch (Exception e) {
			System.err.println("HomeController: Error fetching random books - " + e.getMessage());
			e.printStackTrace();
			errorOccurred = true;
			errorMessage = "Could not load some book sections.";
		}

		// 2. Fetch Popular Books (most added to cart)
		try {
			List<BookCartModel> popularResult = bookService.getTopAddedToCartBooks(4);
			System.out.println("HomeController: Fetched " + popularResult.size() + " popular books initially.");

			// 3. Implement Fallback Logic for "Popular" section
			if (popularResult == null || popularResult.isEmpty()) {
				System.out.println(
						"HomeController: Popular books empty, fetching random books as fallback for 'Popular' section.");
				// If no popular books found, fetch random ones instead
				try { // --- Start Inner Try for Fallback ---
					booksForPopularSection = bookService.getRandomBooks(4); // Fetch another random set
				} catch (Exception e) {
					System.err.println("HomeController: Error fetching fallback random books for popular section - "
							+ e.getMessage());
					e.printStackTrace();
					errorOccurred = true;
					if (errorMessage == null)
						errorMessage = "Could not load popular books section.";
					// If fallback fails, ensure the list is empty
					booksForPopularSection = Collections.emptyList();
				} // --- End Inner Try for Fallback ---
			} else {
				// Popular books were found, use them
				booksForPopularSection = popularResult;
			}
			request.setAttribute("popularBooks", booksForPopularSection);

		} catch (Exception e) { // Catch for initial popular books fetch
			System.err.println("HomeController: Unexpected error fetching popular books - " + e.getMessage());
			e.printStackTrace();
			errorOccurred = true;
			if (errorMessage == null)
				errorMessage = "An unexpected error occurred.";
			request.setAttribute("popularBooks", Collections.emptyList()); // Ensure empty on error
		} // --- THIS IS THE CORRECTED CLOSING BRACE for the outer try-catch ---

		// Set error message if any step failed
		if (errorOccurred) {
			request.setAttribute("homeErrorMessage", errorMessage);
		}

		// Forward to the home JSP page - Ensure path is correct!
		// If home.jsp is directly under webapp, use "/home.jsp"
		// If it's under WEB-INF/pages, use "/WEB-INF/pages/home.jsp"
		request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
		// Note: Your screenshot shows /WEB-INF/pages/home.jsp, so I've used that here.
		// Adjust if needed.
	}
}