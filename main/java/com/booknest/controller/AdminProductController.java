package com.booknest.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.booknest.model.PublisherModel;
import com.booknest.model.AuthorModel;
import com.booknest.model.BookModel;
import com.booknest.model.CategoryModel;
import com.booknest.service.AdminProductService;
import com.booknest.service.DropdownService;
import com.booknest.util.imageUtil;

/**
 * AdminProductController handles both fetching dropdown data and processing form submissions.
 */
@MultipartConfig
@WebServlet(asyncSupported = true, urlPatterns = { "/adminproduct" })
public class AdminProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Services
    private DropdownService dropdownService;
    private AdminProductService adminProductService;
	private imageUtil imageUtil;

    @Override
    public void init() throws ServletException {
        this.dropdownService = new DropdownService();
        this.adminProductService = new AdminProductService();
        this.imageUtil=new imageUtil();
    }

    /**
     * Handles GET requests to fetch dropdown data for the admin page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch dropdown data using the service
        List<PublisherModel> publishers = dropdownService.getPublishers();
        List<AuthorModel> authors = dropdownService.getAuthors();
        List<CategoryModel> categories = dropdownService.getCategories();

        // Set the data as request attributes
        request.setAttribute("publishers", publishers);
        request.setAttribute("authors", authors);
        request.setAttribute("categories", categories);

        // Forward the request to the JSP page
        request.getRequestDispatcher("/WEB-INF/pages/addproduct.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to process form submissions for adding a new product.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve text inputs using request.getParameter()
            String bookTitle = request.getParameter("book_title");
            String isbn = request.getParameter("isbn");
            String publicationDate = request.getParameter("publication_date");
            String price = request.getParameter("price");
            String description = request.getParameter("description");
            String stockQuantity = request.getParameter("stock_quantity");
            String pageCount = request.getParameter("page_count");
            String publisherID = request.getParameter("publisherID");
            String authorID = request.getParameter("authorID");
            String categoryID = request.getParameter("categoryID");

            // Retrieve file input using request.getPart()
            Part filePart = request.getPart("book_img_url"); // File input field name in the form
            String imageUrl = null;

            if (filePart != null && filePart.getSize() > 0) {
                String saveFolder = "bookimg"; // Folder to save the image
                imageUtil imageUtil = new imageUtil();
                boolean uploaded = imageUtil.uploadImage(filePart, getServletContext().getRealPath(""), saveFolder);
                if (uploaded) {
                    imageUrl = "resources/" + saveFolder + "/" + imageUtil.getImageNameFromPart(filePart);
                } else {
                    request.setAttribute("errorMessage", "Failed to upload the book image.");
                    doGet(request, response); // Reload the form with an error message
                    return;
                }
            }

            // Process the data (example: save to the database)
            AdminProductService adminProductService = new AdminProductService();
            BookModel book = new BookModel(
                0, // bookID (auto-incremented)
                bookTitle, isbn, Date.valueOf(publicationDate),
                new BigDecimal(price), description,
                Integer.parseInt(stockQuantity),
                Integer.parseInt(pageCount),
                imageUrl, Integer.parseInt(publisherID)
            );
            int bookID = adminProductService.addBookAndGetID(book);

            if (bookID > 0) {
                // Add author and category links
                adminProductService.addBookAuthor(bookID, Integer.parseInt(authorID));
                adminProductService.addBookCategory(bookID, Integer.parseInt(categoryID));

                request.setAttribute("successMessage", "Book added successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to add the book.");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        // Reload dropdowns and forward to the form
        doGet(request, response);
    }
}