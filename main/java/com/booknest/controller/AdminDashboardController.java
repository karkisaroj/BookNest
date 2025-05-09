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
import com.booknest.util.RedirectionUtil;

/**
 * Controller for handling admin dashboard functionalities, including book management.
 * 
 * @author Noble Nepal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admindashboard" })
public class AdminDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final String dashboardPagePath = "/WEB-INF/pages/admindashboard.jsp";
    private final String invalidActionMessage = "Invalid action provided.";
    private final String processRequestErrorMessage = "An error occurred while processing the request.";
    private final String stockUpdateSuccessMessage = "Book stock updated successfully!";
    private final String stockUpdateErrorMessage = "Failed to update book stock.";
    private final String deleteBookSuccessMessage = "Book deleted successfully!";
    private final String deleteBookErrorMessage = "Failed to delete the book.";
    private final String deleteBookReferencedErrorMessage = "Cannot delete the book because it is in a cart.";
    private final String invalidBookIdMessage = "Invalid book ID format.";
    private final String missingBookIdOrStockMessage = "Book ID or new stock quantity is missing.";

    private AdminBookService adminBookService;
    private RedirectionUtil redirectionUtil;

    @Override
    public void init() throws ServletException {
        this.adminBookService = new AdminBookService();
        this.redirectionUtil = new RedirectionUtil();
    }

    /**
     * Handles GET requests for the admin dashboard.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch the initial list of books and forward to the dashboard
		List<BookModel> books = adminBookService.getAllBooks();
		request.setAttribute("books", books);
		redirectionUtil.redirectToPage(request, response, dashboardPagePath);
    }

    /**
     * Handles POST requests for book-related actions (update stock and delete book).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("updateStock".equalsIgnoreCase(action)) {
                handleUpdateStock(request, response);
            } else if ("deleteBook".equalsIgnoreCase(action)) {
                handleDeleteBook(request, response);
            } else {
                redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", invalidActionMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", processRequestErrorMessage);
        }
    }

    /**
     * Handles stock updates for a book.
     */
    private void handleUpdateStock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
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
                    redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "success", stockUpdateSuccessMessage);
                } else {
                    redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", stockUpdateErrorMessage);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", invalidBookIdMessage);
            }
        } else {
            redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", missingBookIdOrStockMessage);
        }
    }

    /**
     * Handles deletion of a book.
     */
    private void handleDeleteBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String bookIdParam = request.getParameter("bookID");

        if (bookIdParam != null) {
            try {
                int bookId = Integer.parseInt(bookIdParam);

                // Delete the book and fetch the updated book list
                List<BookModel> updatedBooks = adminBookService.deleteBookById(bookId);

                if (updatedBooks != null) {
                    request.setAttribute("books", updatedBooks);
                    redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "success", deleteBookSuccessMessage);
                } else {
                    redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", deleteBookErrorMessage);
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Cannot delete book because it is referenced in another table")) {
                    redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", deleteBookReferencedErrorMessage);
                } else {
                    throw e; // Re-throw generic SQL exceptions
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", invalidBookIdMessage);
            }
        } else {
            redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", missingBookIdOrStockMessage);
        }
    }
}