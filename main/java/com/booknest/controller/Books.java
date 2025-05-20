package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.booknest.model.BookCartModel;
import com.booknest.service.BookService;
import com.booknest.service.BookServiceImpl;

/**
 * @author Saroj Karki 23047612
 */
/**
 * Books servlet handles requests for displaying all available books.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/books" })
public class Books extends HttpServlet {
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
	 * Handles GET requests to display the list of books.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Retrieve all books from the service
			List<BookCartModel> books = bookService.getAllBooks();

			// Set the books attribute for the JSP
			request.setAttribute("books", books);

		} catch (Exception e) {
			// Set error message on request in case of exception
			request.setAttribute("viewErrorMessage", "Error loading books: " + e.getMessage());
		}

		// Forward to the books JSP
		request.getRequestDispatcher("/WEB-INF/pages/books.jsp").forward(request, response);
	}
}