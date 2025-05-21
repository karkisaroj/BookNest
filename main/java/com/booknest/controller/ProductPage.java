package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.booknest.model.BookCartModel;
import com.booknest.service.BookService;
import com.booknest.service.BookServiceImpl;

/**
 * ProductController handles requests to display individual book product pages.
 * 
 * @author Saroj Karki 23047612
 */
@WebServlet("/product")
public class ProductPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService;

	// Path constants
	private static final String BOOKS_PAGE_PATH = "/books";
	private static final String PRODUCT_PAGE_JSP_PATH = "/WEB-INF/pages/product-page.jsp";

	// Parameter constants
	private static final String BOOK_ID_PARAM = "bookId";
	private static final String BOOK_ATTR = "book";
	private static final String FLASH_ERROR_MESSAGE_KEY = "flashErrorMessage";

	// Message constants
	private static final String NO_BOOK_SELECTED_MESSAGE = "No book selected. Please choose a book.";
	private static final String BOOK_NOT_FOUND_MESSAGE = "The requested book could not be found.";
	private static final String INVALID_BOOK_ID_MESSAGE = "Invalid book ID format.";
	private static final String BOOK_LOAD_ERROR_PREFIX = "Error loading book details: ";

	/**
	 * Initializes the book service.
	 */
	@Override
	public void init() throws ServletException {
		this.bookService = new BookServiceImpl();
	}

	/**
	 * Handles GET requests to display details for a specific book. Requires a
	 * bookId parameter to identify which book to display.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String bookIdParam = request.getParameter(BOOK_ID_PARAM);

		// Validate book ID parameter
		if (bookIdParam == null || bookIdParam.trim().isEmpty()) {
			request.getSession().setAttribute(FLASH_ERROR_MESSAGE_KEY, NO_BOOK_SELECTED_MESSAGE);
			response.sendRedirect(request.getContextPath() + BOOKS_PAGE_PATH);
			return;
		}

		try {
			// Parse and fetch the book
			int bookId = Integer.parseInt(bookIdParam);
			BookCartModel book = bookService.getBookById(bookId);

			// Check if book exists
			if (book == null) {
				request.getSession().setAttribute(FLASH_ERROR_MESSAGE_KEY, BOOK_NOT_FOUND_MESSAGE);
				response.sendRedirect(request.getContextPath() + BOOKS_PAGE_PATH);
				return;
			}

			// Set book as request attribute
			request.setAttribute(BOOK_ATTR, book);

			// Forward to product page
			request.getRequestDispatcher(PRODUCT_PAGE_JSP_PATH).forward(request, response);

		} catch (NumberFormatException e) {
			request.getSession().setAttribute(FLASH_ERROR_MESSAGE_KEY, INVALID_BOOK_ID_MESSAGE);
			response.sendRedirect(request.getContextPath() + BOOKS_PAGE_PATH);
		} catch (Exception e) {
			request.getSession().setAttribute(FLASH_ERROR_MESSAGE_KEY, BOOK_LOAD_ERROR_PREFIX + e.getMessage());
			response.sendRedirect(request.getContextPath() + BOOKS_PAGE_PATH);
		}
	}
}