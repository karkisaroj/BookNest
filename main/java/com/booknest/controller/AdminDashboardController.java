package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.booknest.model.BookModel;
import com.booknest.service.AdminBookService;
import com.booknest.service.AdminDashboardService;
import com.booknest.util.RedirectionUtil;

/**
 * Controller for handling admin dashboard functionalities, including book
 * management and popular books. Manages GET requests for displaying dashboard
 * analytics and POST requests for book operations.
 * 
 * @author Noble-Nepal
 * 
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admindashboard" })
public class AdminDashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Path constants
	private final String dashboardPagePath = "/WEB-INF/pages/admindashboard.jsp";

	// Error and success message constants
	private final String invalidActionMessage = "Invalid action provided.";
	private final String processRequestErrorMessage = "An error occurred while processing the request.";
	private final String stockUpdateSuccessMessage = "Book stock updated successfully!";
	private final String stockUpdateErrorMessage = "Failed to update book stock.";
	private final String deleteBookSuccessMessage = "Book deleted successfully!";
	private final String deleteBookErrorMessage = "Failed to delete the book.";
	private final String deleteBookReferencedErrorMessage = "Cannot delete the book because it is in a cart.";
	private final String invalidBookIdMessage = "Invalid book ID format.";
	private final String missingBookIdOrStockMessage = "Book ID or new stock quantity is missing.";

	// Service and utility objects
	private AdminBookService adminBookService;
	private AdminDashboardService adminDashboardService;
	private RedirectionUtil redirectionUtil;

	/**
	 * Initializes the servlet by creating service and utility instances. This
	 * method is called once when the servlet is first loaded.
	 * 
	 * @throws ServletException if a servlet-specific error occurs during
	 *                          initialization
	 */
	@Override
	public void init() throws ServletException {
		this.adminBookService = new AdminBookService();
		this.adminDashboardService = new AdminDashboardService();
		this.redirectionUtil = new RedirectionUtil();
	}

	/**
	 * Handles GET requests for the admin dashboard. Fetches analytics data
	 * including revenue, orders, books sold, all books inventory, and popular
	 * books.
	 * 
	 * @param request  The HttpServletRequest object containing client request
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during response handling
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			double totalRevenue = adminDashboardService.getTotalRevenue();
			int totalOrders = adminDashboardService.getTotalOrders();
			int totalBooksSold = adminDashboardService.getTotalBooksSold();
			// Fetch the initial list of books and the top 5 most popular books
			List<BookModel> books = adminBookService.getAllBooks();
			List<BookModel> popularBooks = adminDashboardService.getTopPopularBooks();

			// Set attributes for the JSP
			request.setAttribute("totalRevenue", totalRevenue);
			request.setAttribute("totalOrders", totalOrders);
			request.setAttribute("totalBooksSold", totalBooksSold);
			request.setAttribute("books", books);
			request.setAttribute("popularBooks", popularBooks);

			// Redirect to the dashboard page
			redirectionUtil.redirectToPage(request, response, dashboardPagePath);
		} catch (Exception e) {
			e.printStackTrace();
			redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error",
					"An error occurred while loading the dashboard.");
		}
	}

	/**
	 * Handles POST requests for book-related actions. Processes two main actions:
	 * updating book stock and deleting books. Routes the request to the appropriate
	 * handler method based on the 'action' parameter.
	 * 
	 * @param request  The HttpServletRequest object containing action parameters
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during response handling
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		try {
			if ("updateStock".equalsIgnoreCase(action)) {
				handleUpdateStock(request, response);
			} else if ("deleteBook".equalsIgnoreCase(action)) {
				handleDeleteBook(request, response);
			} else {
				request.setAttribute("error", invalidActionMessage);
				recalculateAndSetAnalytics(request);
				forwardToDashboard(request, response);
				return; 
			}

			
			recalculateAndSetAnalytics(request);

			
			forwardToDashboard(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", processRequestErrorMessage);
			forwardToDashboard(request, response);
		}
	}

	/**
	 * Forwards the request to the admin dashboard JSP. Used for rendering the
	 * dashboard page after processing actions.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during forwarding
	 */
	private void forwardToDashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher(dashboardPagePath).forward(request, response);
	}

	/**
	 * Recalculates analytics data and sets them as request attributes for the JSP.
	 * Updates total revenue, order count, books sold, and popular books list after
	 * operations that might affect these metrics.
	 * 
	 * @param request The HttpServletRequest object to update with fresh data
	 * @throws SQLException if a database access error occurs
	 */
	private void recalculateAndSetAnalytics(HttpServletRequest request) throws SQLException {
		double totalRevenue = adminDashboardService.getTotalRevenue();
		int totalOrders = adminDashboardService.getTotalOrders();
		int totalBooksSold = adminDashboardService.getTotalBooksSold();
		List<BookModel> popularBooks = adminDashboardService.getTopPopularBooks();

		// Set analytics attributes for the request
		request.setAttribute("totalRevenue", totalRevenue);
		request.setAttribute("totalOrders", totalOrders);
		request.setAttribute("totalBooksSold", totalBooksSold);
		request.setAttribute("popularBooks", popularBooks);
	}

	/**
	 * Handles stock updates for a book. Processes bookID and newStock parameters,
	 * validates them, and updates the book's stock quantity in the database.
	 * 
	 * @param request  The HttpServletRequest object containing update parameters
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 * @throws SQLException     if a database access error occurs
	 */
	private void handleUpdateStock(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String bookIdParam = request.getParameter("bookID");
		String newStockParam = request.getParameter("newStock");

		if (bookIdParam != null && newStockParam != null) {
			try {
				int bookId = Integer.parseInt(bookIdParam);
				int newStock = Integer.parseInt(newStockParam);

				// Update stock and fetch the updated book list
				List<BookModel> updatedBooks = adminBookService.updateBookStock(bookId, newStock);

				if (updatedBooks != null) {
					request.setAttribute("books", updatedBooks);
					request.setAttribute("success", stockUpdateSuccessMessage); 
				} else {
					request.setAttribute("error", stockUpdateErrorMessage); 
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				request.setAttribute("error", invalidBookIdMessage); 
			}
		} else {
			request.setAttribute("error", missingBookIdOrStockMessage); 
		}
	}

	/**
	 * Handles deletion of a book. Processes the bookID parameter, validates it, and
	 * attempts to delete the book from the database. Handles potential foreign key
	 * constraint violations.
	 * 
	 * @param request  The HttpServletRequest object containing delete parameters
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 * @throws SQLException     if a database access error occurs that can't be
	 *                          handled
	 */
	private void handleDeleteBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String bookIdParam = request.getParameter("bookID");

		if (bookIdParam != null) {
			try {
				int bookId = Integer.parseInt(bookIdParam);

				// Delete the book and fetch the updated book list
				List<BookModel> updatedBooks = adminBookService.deleteBookById(bookId);

				if (updatedBooks != null) {
					request.setAttribute("books", updatedBooks);
					request.setAttribute("success", deleteBookSuccessMessage); 
				} else {
					request.setAttribute("error", deleteBookErrorMessage); 
				}
			} catch (SQLException e) {
				if (e.getMessage().contains("Cannot delete book because it is referenced in another table")) {
					request.setAttribute("error", deleteBookReferencedErrorMessage); 
				} else {
					throw e; 
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				request.setAttribute("error", invalidBookIdMessage); 
			}
		} else {
			request.setAttribute("error", missingBookIdOrStockMessage); 
		}
	}
}