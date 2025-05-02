package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.booknest.model.BookModel;
import com.booknest.service.BookService;
import com.booknest.service.BookServiceImpl;

@WebServlet("/books")
public class Books extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService;

	@Override
	public void init() throws ServletException {
		this.bookService = new BookServiceImpl();
		System.out.println("Books Controller Initialized.");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Books Controller - doGet called");

		try {
			// Add debug print to see what's happening
			List<BookModel> books = bookService.getAllBooks();
			System.out.println("Books Controller - Books fetched: " + (books != null ? books.size() : "null"));

			// Set the books attribute for the JSP
			request.setAttribute("books", books);

		} catch (Exception e) {
			System.err.println("Error fetching books: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("viewErrorMessage", "Error loading books: " + e.getMessage());
		}

		// Forward to the books JSP
		request.getRequestDispatcher("/WEB-INF/pages/books.jsp").forward(request, response);
	}
}