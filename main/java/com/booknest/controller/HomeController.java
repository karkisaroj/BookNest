package com.booknest.controller;

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

/**
 * HomeController handles requests to the home page of the BookNest application.
 * Displays featured books including random selections and popular books.
 * 
 * @author Saroj Karki 23047612
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/home", "/" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService;

	/**
	 * Initializes the book service.
	 */
	@Override
	public void init() throws ServletException {
		this.bookService = new BookServiceImpl();
	}

	/**
	 * Handles GET requests to display the home page with featured books. Shows
	 * random books and popular books (most added to cart).
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<BookCartModel> booksForBookSection = Collections.emptyList();
		List<BookCartModel> booksForPopularSection = Collections.emptyList();
		boolean errorOccurred = false;
		String errorMessage = null;

		// 1. Fetch Random Books for the "Books" section
		try {
			booksForBookSection = bookService.getRandomBooks(4);
			request.setAttribute("randomBooks", booksForBookSection);
		} catch (Exception e) {
			errorOccurred = true;
			errorMessage = "Could not load some book sections.";
		}

		// 2. Fetch Popular Books (most added to cart)
		try {
			List<BookCartModel> popularResult = bookService.getTopAddedToCartBooks(4);

			// 3. Implement Fallback Logic for "Popular" section
			if (popularResult == null || popularResult.isEmpty()) {
				// If no popular books found, fetch random ones instead
				try {
					booksForPopularSection = bookService.getRandomBooks(4);
				} catch (Exception e) {
					errorOccurred = true;
					if (errorMessage == null) {
						errorMessage = "Could not load popular books section.";
					}
					// If fallback fails, ensure the list is empty
					booksForPopularSection = Collections.emptyList();
				}
			} else {
				// Popular books were found, use them
				booksForPopularSection = popularResult;
			}
			request.setAttribute("popularBooks", booksForPopularSection);
		} catch (Exception e) {
			errorOccurred = true;
			if (errorMessage == null) {
				errorMessage = "An unexpected error occurred.";
			}
			request.setAttribute("popularBooks", Collections.emptyList());
		}

		// Set error message if any step failed
		if (errorOccurred) {
			request.setAttribute("homeErrorMessage", errorMessage);
		}

		request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
	}
}