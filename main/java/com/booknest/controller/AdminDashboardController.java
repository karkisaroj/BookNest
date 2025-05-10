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
 * Controller for handling admin dashboard functionalities, including book management and popular books.
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
    private AdminDashboardService adminDashboardService;
    private RedirectionUtil redirectionUtil;

    @Override
    public void init() throws ServletException {
        this.adminBookService = new AdminBookService();
        this.adminDashboardService = new AdminDashboardService();
        this.redirectionUtil = new RedirectionUtil();
    }

    /**
     * Handles GET requests for the admin dashboard.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            redirectionUtil.setMsgAndRedirect(request, response, dashboardPagePath, "error", "An error occurred while loading the dashboard.");
        }
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
                request.setAttribute("error", invalidActionMessage);
                recalculateAndSetAnalytics(request);
                forwardToDashboard(request, response);
                return; // Exit early for invalid actions
            }

            // Recalculate and set analytics after update or delete
            recalculateAndSetAnalytics(request);

            // Forward to dashboard instead of redirecting
            forwardToDashboard(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", processRequestErrorMessage);
            forwardToDashboard(request, response);
        }
    }
    /**
     * Forward the request to the admin dashboard JSP.
     */
    private void forwardToDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(dashboardPagePath).forward(request, response);
    }
    /**
     * Recalculates analytics data and sets them as request attributes.
     */
    /**
     * Recalculates analytics data and sets them as request attributes for the JSP.
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
                    request.setAttribute("success", stockUpdateSuccessMessage); // Set success message
                } else {
                    request.setAttribute("error", stockUpdateErrorMessage); // Set error message
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("error", invalidBookIdMessage); // Set error message
            }
        } else {
            request.setAttribute("error", missingBookIdOrStockMessage); // Set error message
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
                    request.setAttribute("success", deleteBookSuccessMessage); // Set success message
                } else {
                    request.setAttribute("error", deleteBookErrorMessage); // Set error message
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Cannot delete book because it is referenced in another table")) {
                    request.setAttribute("error", deleteBookReferencedErrorMessage); // Set error message
                } else {
                    throw e; // Re-throw generic SQL exceptions
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("error", invalidBookIdMessage); // Set error message
            }
        } else {
            request.setAttribute("error", missingBookIdOrStockMessage); // Set error message
        }
    }

}