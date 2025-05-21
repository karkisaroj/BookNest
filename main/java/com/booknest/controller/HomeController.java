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

	// Path constants
	private final String homePagePath = "/WEB-INF/pages/home.jsp";

	// Parameter constants
	private final String randomBooksParam = "randomBooks";
	private final String popularBooksParam = "popularBooks";
	private final String errorMessageParam = "homeErrorMessage";
	// Message constants
	private final String loadSectionErrorMessage = "Could not load some book sections.";
	private final String popularBooksErrorMessage = "Could not load popular books section.";
	private final String unexpectedErrorMessage = "An unexpected error occurred.";

	// Configuration constants
	private final int featuredBookCount = 4;

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
			booksForBookSection = bookService.getRandomBooks(featuredBookCount);
			request.setAttribute(randomBooksParam, booksForBookSection);
		} catch (Exception e) {
			errorOccurred = true;
			errorMessage = loadSectionErrorMessage;
		}

		// 2. Fetch Popular Books (most added to cart)
		try {
			List<BookCartModel> popularResult = bookService.getTopAddedToCartBooks(featuredBookCount);

			// 3. Implement Fallback Logic for "Popular" section
			if (popularResult == null || popularResult.isEmpty()) {
				// If no popular books found, fetch random ones instead
				try {
					booksForPopularSection = bookService.getRandomBooks(featuredBookCount);
				} catch (Exception e) {
					errorOccurred = true;
					if (errorMessage == null) {
						errorMessage = popularBooksErrorMessage;
					}
					// If fallback fails, ensure the list is empty
					booksForPopularSection = Collections.emptyList();
				}
			} else {
				// Popular books were found, use them
				booksForPopularSection = popularResult;
			}
			request.setAttribute(popularBooksParam, booksForPopularSection);
		} catch (Exception e) {
			errorOccurred = true;
			if (errorMessage == null) {
				errorMessage = unexpectedErrorMessage;
			}
			request.setAttribute(popularBooksParam, Collections.emptyList());
		}

		// Set error message if any step failed
		if (errorOccurred) {
			request.setAttribute(errorMessageParam, errorMessage);
		}

		request.getRequestDispatcher(homePagePath).forward(request, response);
	}
}