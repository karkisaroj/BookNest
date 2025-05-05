package com.booknest.controller;

import com.booknest.model.BookCartModel;
import com.booknest.model.Category;
import com.booknest.service.BookService;
import com.booknest.service.BookServiceImpl;
import com.booknest.service.CategoryService;
import com.booknest.service.CategoryServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class SearchController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookService bookService;
    private CategoryService categoryService;

    public SearchController() {
        super();
        this.bookService = new BookServiceImpl();
        this.categoryService = new CategoryServiceImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("CONTROLLER DEBUG: SearchController doGet called");

        String query = request.getParameter("query");
        String categoryIdStr = request.getParameter("categoryId");
        Integer categoryId = null;
        
        System.out.println("CONTROLLER DEBUG: Search query parameter: " + query);
        System.out.println("CONTROLLER DEBUG: Category ID parameter: " + categoryIdStr);
        
        // Load categories for the filter buttons
        try {
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
        } catch (Exception e) {
            System.err.println("CONTROLLER ERROR: Failed to load categories: " + e.getMessage());
            // Continue with the search even if categories fail to load
        }
        
        // Convert categoryId to integer if present
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
                request.setAttribute("selectedCategoryId", categoryId);
                
                // Get category name for display
                try {
                    Category category = categoryService.getCategoryById(categoryId);
                    if (category != null) {
                        request.setAttribute("selectedCategoryName", category.getCategoryName());
                    }
                } catch (Exception e) {
                    System.err.println("CONTROLLER ERROR: Failed to get category name: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
                System.err.println("CONTROLLER ERROR: Invalid category ID: " + categoryIdStr);
            }
        }

        List<BookCartModel> results = new ArrayList<>();
        boolean hasSearchCriteria = false;

        try {
            // Search by text query if provided
            if (query != null && !query.isEmpty()) {
                System.out.println("CONTROLLER DEBUG: Searching by query: " + query);
                results = bookService.searchBooksByTitle(query);
                request.setAttribute("searchQuery", query);
                hasSearchCriteria = true;
            }
            
            // Search by category if provided
            else if (categoryId != null) {
                System.out.println("CONTROLLER DEBUG: Searching by category ID: " + categoryId);
                results = bookService.getBooksByCategory(categoryId);
                hasSearchCriteria = true;
            }
            
            // No search criteria provided
            if (!hasSearchCriteria) {
                System.out.println("CONTROLLER DEBUG: No search criteria, redirecting to books page");
                response.sendRedirect(request.getContextPath() + "/books");
                return;
            }
            
            System.out.println("CONTROLLER DEBUG: Search returned " + 
                (results == null ? "null" : results.size() + " results"));

            // Always use an empty list if null
            if (results == null) {
                results = new ArrayList<>();
            }

            request.setAttribute("searchResults", results);
            System.out.println("CONTROLLER DEBUG: Forwarding to search-result.jsp with " + results.size() + " results");
            request.getRequestDispatcher("/WEB-INF/pages/search-result.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("CONTROLLER ERROR: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("error", "Search failed: " + e.getMessage());
            request.setAttribute("searchResults", new ArrayList<>());
            
            System.out.println("CONTROLLER DEBUG: Forwarding to search-result.jsp with error");
            request.getRequestDispatcher("/WEB-INF/pages/search-result.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}