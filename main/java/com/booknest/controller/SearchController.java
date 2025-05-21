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

/**
 * Controller for handling book search functionality. Supports searching by text
 * query and category filtering.
 * 
 * @author Saroj Karki 23047612
 */
@WebServlet("/search")
public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookService bookService;
	private CategoryService categoryService;

	// Path constants
	private static final String BOOKS_PAGE_PATH = "/books";
	private static final String SEARCH_RESULTS_JSP_PATH = "/WEB-INF/pages/search-result.jsp";

	// Parameter constants
	private static final String QUERY_PARAM = "query";
	private static final String CATEGORY_ID_PARAM = "categoryId";
	private static final String CATEGORIES_ATTR = "categories";
	private static final String SELECTED_CATEGORY_ID_ATTR = "selectedCategoryId";
	private static final String SELECTED_CATEGORY_NAME_ATTR = "selectedCategoryName";
	private static final String SEARCH_QUERY_ATTR = "searchQuery";
	private static final String SEARCH_RESULTS_ATTR = "searchResults";
	private static final String ERROR_ATTR = "error";

	// Message constants
	private static final String CATEGORIES_LOAD_ERROR_PREFIX = "Failed to load categories: ";
	private static final String CATEGORY_NAME_ERROR_PREFIX = "Failed to get category name: ";
	private static final String INVALID_CATEGORY_ID_PREFIX = "Invalid category ID: ";
	private static final String SEARCH_FAILED_PREFIX = "Search failed: ";
	private static final String CONTROLLER_ERROR_PREFIX = "CONTROLLER ERROR: ";

	public SearchController() {
		super();
		this.bookService = new BookServiceImpl();
		this.categoryService = new CategoryServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String query = request.getParameter(QUERY_PARAM);
		String categoryIdStr = request.getParameter(CATEGORY_ID_PARAM);
		Integer categoryId = null;

		// Load categories for the filter buttons
		try {
			List<Category> categories = categoryService.getAllCategories();
			request.setAttribute(CATEGORIES_ATTR, categories);
		} catch (Exception e) {
			System.err.println(CONTROLLER_ERROR_PREFIX + CATEGORIES_LOAD_ERROR_PREFIX + e.getMessage());
			// Continue with the search even if categories fail to load
		}

		// Convert categoryId to integer if present
		if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
			try {
				categoryId = Integer.parseInt(categoryIdStr);
				request.setAttribute(SELECTED_CATEGORY_ID_ATTR, categoryId);

				// Get category name for display
				try {
					Category category = categoryService.getCategoryById(categoryId);
					if (category != null) {
						request.setAttribute(SELECTED_CATEGORY_NAME_ATTR, category.getCategoryName());
					}
				} catch (Exception e) {
					System.err.println(CONTROLLER_ERROR_PREFIX + CATEGORY_NAME_ERROR_PREFIX + e.getMessage());
				}
			} catch (NumberFormatException e) {
				System.err.println(CONTROLLER_ERROR_PREFIX + INVALID_CATEGORY_ID_PREFIX + categoryIdStr);
			}
		}

		List<BookCartModel> results = new ArrayList<>();
		boolean hasSearchCriteria = false;

		try {
			// Search by text query if provided
			if (query != null && !query.isEmpty()) {
				results = bookService.searchBooksByTitle(query);
				request.setAttribute(SEARCH_QUERY_ATTR, query);
				hasSearchCriteria = true;
			}

			// Search by category if provided
			else if (categoryId != null) {
				results = bookService.getBooksByCategory(categoryId);
				hasSearchCriteria = true;
			}

			// No search criteria provided
			if (!hasSearchCriteria) {
				response.sendRedirect(request.getContextPath() + BOOKS_PAGE_PATH);
				return;
			}

			// Always use an empty list if null
			if (results == null) {
				results = new ArrayList<>();
			}

			request.setAttribute(SEARCH_RESULTS_ATTR, results);
			request.getRequestDispatcher(SEARCH_RESULTS_JSP_PATH).forward(request, response);

		} catch (Exception e) {
			System.err.println(CONTROLLER_ERROR_PREFIX + e.getMessage());
			e.printStackTrace();

			request.setAttribute(ERROR_ATTR, SEARCH_FAILED_PREFIX + e.getMessage());
			request.setAttribute(SEARCH_RESULTS_ATTR, new ArrayList<>());
			request.getRequestDispatcher(SEARCH_RESULTS_JSP_PATH).forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}