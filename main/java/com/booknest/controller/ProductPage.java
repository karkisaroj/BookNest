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

@WebServlet("/product")
public class ProductPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService;

	@Override
	public void init() throws ServletException {
		this.bookService = new BookServiceImpl();
		System.out.println("ProductPage Controller Initialized.");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String bookIdParam = request.getParameter("bookId");
		System.out.println("ProductPage Controller - doGet called with bookId: " + bookIdParam);

		if (bookIdParam == null || bookIdParam.trim().isEmpty()) {
			System.err.println("No book ID provided");
			request.getSession().setAttribute("flashErrorMessage", "No book selected. Please choose a book.");
			response.sendRedirect(request.getContextPath() + "/books");
			return;
		}

		try {
			int bookId = Integer.parseInt(bookIdParam);
			BookCartModel book = bookService.getBookById(bookId);

			if (book == null) {
				System.err.println("Book not found: " + bookId);
				request.getSession().setAttribute("flashErrorMessage", "The requested book could not be found.");
				response.sendRedirect(request.getContextPath() + "/books");
				return;
			}

			System.out.println("ProductPage Controller - Book found: " + book.getBook_title());
			request.setAttribute("book", book);

			// Forward to product page
			request.getRequestDispatcher("/WEB-INF/pages/product-page.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			System.err.println("Invalid book ID format: " + bookIdParam);
			request.getSession().setAttribute("flashErrorMessage", "Invalid book ID format.");
			response.sendRedirect(request.getContextPath() + "/books");
		} catch (Exception e) {
			System.err.println("Error fetching book details: " + e.getMessage());
			e.printStackTrace();
			request.getSession().setAttribute("flashErrorMessage", "Error loading book details: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/books");
		}
	}
}