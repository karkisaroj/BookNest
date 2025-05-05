package com.booknest.controller;

import com.booknest.model.BookCartModel;
import com.booknest.service.BookService;
import com.booknest.service.BookServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(asyncSupported = true, urlPatterns = { "/addbook" })
public class AddBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService;

	@Override
	public void init() throws ServletException {
		// Initialize the service (Consider dependency injection later)
		this.bookService = new BookServiceImpl();
		System.out.println("AddBookServlet Initialized.");
	}

	/**
	 * Handles GET requests: Displays the form to add a new book.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("AddBookServlet [doGet]: Forwarding to add-book form.");
		// Forward to the JSP page that contains the add book form
		// Place this JSP inside /WEB-INF/pages/ for better structure/security
		request.getRequestDispatcher("/WEB-INF/pages/add-book.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests: Processes the submitted form data to add a new book.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("AddBookServlet [doPost]: Processing add book form submission.");
		HttpSession session = request.getSession();
		String redirectPath = request.getContextPath() + "/addBook"; // Default redirect back to form on error

		// 1. Retrieve form parameters
		String title = request.getParameter("bookTitle");
		String isbn = request.getParameter("isbn");
		String pubDateStr = request.getParameter("publicationDate");
		String priceStr = request.getParameter("price");
		String description = request.getParameter("description");
		String stockStr = request.getParameter("stockQuantity");
		String imageUrl = request.getParameter("imageUrl");
		String publisherIdStr = request.getParameter("publisherId");

		// 2. Basic Validation (Add more robust validation as needed)
		if (title == null || title.trim().isEmpty() || priceStr == null || priceStr.trim().isEmpty() || stockStr == null
				|| stockStr.trim().isEmpty() || publisherIdStr == null || publisherIdStr.trim().isEmpty()
				|| pubDateStr == null || pubDateStr.trim().isEmpty()) {

			System.err.println("AddBookServlet [doPost]: Validation failed - Missing required fields.");
			session.setAttribute("flashErrorMessage",
					"Please fill in all required fields (Title, Price, Stock, Publisher ID, Publication Date).");
			// Optional: Repopulate form fields before redirecting/forwarding
			request.setAttribute("bookTitle", title);
			request.setAttribute("isbn", isbn);
			request.setAttribute("publicationDate", pubDateStr);
			request.setAttribute("price", priceStr);
			request.setAttribute("description", description);
			request.setAttribute("stockQuantity", stockStr);
			request.setAttribute("imageUrl", imageUrl);
			request.setAttribute("publisherId", publisherIdStr);
			request.getRequestDispatcher("/WEB-INF/pages/add-book.jsp").forward(request, response);
			return;
		}

		BookCartModel newBook = new BookCartModel();
		try {
			// 3. Type Conversion and Population
			newBook.setBook_title(title.trim());
			newBook.setIsbn(isbn != null ? isbn.trim() : null); // Handle optional ISBN
			newBook.setDescription(description != null ? description.trim() : null); // Handle optional description
			newBook.setBook_img_url(imageUrl != null ? imageUrl.trim() : null); // Handle optional image URL

			// Convert Price
			newBook.setPrice(new BigDecimal(priceStr.trim()));

			// Convert Stock Quantity
			newBook.setStock_quantity(Integer.parseInt(stockStr.trim()));

			// Convert Publisher ID
			newBook.setPublisherID(Integer.parseInt(publisherIdStr.trim()));

			// Convert Publication Date
			// IMPORTANT: Ensure your BookModel uses java.util.Date
			// If it uses java.sql.Date or LocalDate, adjust accordingly
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false); // Don't allow invalid dates like 2023-02-30
			Date publicationDate = dateFormat.parse(pubDateStr.trim());
			newBook.setPublication_date(publicationDate); // Assumes BookModel uses java.util.Date

			// 4. Call BookService to add the book
			System.out.println("AddBookServlet [doPost]: Calling bookService.addBook...");
			bookService.addBook(newBook); // Assuming this method handles the DB insertion

			// 5. Success Handling
			System.out.println("AddBookServlet [doPost]: Book added successfully (ID might be generated in DB).");
			session.setAttribute("flashSuccessMessage", "Book '" + newBook.getBook_title() + "' added successfully!");
			redirectPath = request.getContextPath() + "/books"; // Redirect to book list on success

		} catch (NumberFormatException e) {
			System.err.println("AddBookServlet [doPost]: Invalid number format - " + e.getMessage());
			session.setAttribute("flashErrorMessage", "Invalid number format for Price, Stock, or Publisher ID.");
			// Forward back to the form with error message and repopulated data
			request.setAttribute("bookTitle", title);
			request.setAttribute("isbn", isbn);
			request.setAttribute("publicationDate", pubDateStr);
			request.setAttribute("price", priceStr);
			request.setAttribute("description", description);
			request.setAttribute("stockQuantity", stockStr);
			request.setAttribute("imageUrl", imageUrl);
			request.setAttribute("publisherId", publisherIdStr);
			request.getRequestDispatcher("/WEB-INF/pages/add-book.jsp").forward(request, response);
			return; // Stop further processing

		} catch (ParseException e) {
			System.err.println("AddBookServlet [doPost]: Invalid date format - " + e.getMessage());
			session.setAttribute("flashErrorMessage",
					"Invalid date format for Publication Date. Please use YYYY-MM-DD.");
			// Forward back to the form with error message and repopulated data
			request.setAttribute("bookTitle", title);
			request.setAttribute("isbn", isbn);
			request.setAttribute("publicationDate", pubDateStr);
			request.setAttribute("price", priceStr);
			request.setAttribute("description", description);
			request.setAttribute("stockQuantity", stockStr);
			request.setAttribute("imageUrl", imageUrl);
			request.setAttribute("publisherId", publisherIdStr);
			request.getRequestDispatcher("/WEB-INF/pages/add-book.jsp").forward(request, response);
			return; // Stop further processing

		} catch (Exception e) { // Catch unexpected errors
			System.err.println("AddBookServlet [doPost]: Unexpected error - " + e.getMessage());
			e.printStackTrace(); // Log the stack trace
			session.setAttribute("flashErrorMessage", "An unexpected error occurred. Please check logs.");
			// Forward back to the form with error message and repopulated data
			request.setAttribute("bookTitle", title);
			request.setAttribute("isbn", isbn);
			request.setAttribute("publicationDate", pubDateStr);
			request.setAttribute("price", priceStr);
			request.setAttribute("description", description);
			request.setAttribute("stockQuantity", stockStr);
			request.setAttribute("imageUrl", imageUrl);
			request.setAttribute("publisherId", publisherIdStr);
			request.getRequestDispatcher("/WEB-INF/pages/add-book.jsp").forward(request, response);
			return; // Stop further processing
		}

		// 6. Redirect after processing
		System.out.println("AddBookServlet [doPost]: Redirecting to " + redirectPath);
		response.sendRedirect(redirectPath);
	}
}