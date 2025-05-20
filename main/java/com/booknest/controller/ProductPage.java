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

		String bookIdParam = request.getParameter("bookId");

		// Validate book ID parameter
		if (bookIdParam == null || bookIdParam.trim().isEmpty()) {
			request.getSession().setAttribute("flashErrorMessage", "No book selected. Please choose a book.");
			response.sendRedirect(request.getContextPath() + "/books");
			return;
		}

		try {
			// Parse and fetch the book
			int bookId = Integer.parseInt(bookIdParam);
			BookCartModel book = bookService.getBookById(bookId);

			// Check if book exists
			if (book == null) {
				request.getSession().setAttribute("flashErrorMessage", "The requested book could not be found.");
				response.sendRedirect(request.getContextPath() + "/books");
				return;
			}

			// Set book as request attribute
			request.setAttribute("book", book);

			// Forward to product page
			request.getRequestDispatcher("/WEB-INF/pages/product-page.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.getSession().setAttribute("flashErrorMessage", "Invalid book ID format.");
			response.sendRedirect(request.getContextPath() + "/books");
		} catch (Exception e) {
			request.getSession().setAttribute("flashErrorMessage", "Error loading book details: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/books");
		}
	}
}