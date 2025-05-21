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
 * AdminProductController handles the administration of book products.
 * Manages both fetching dropdown data for the product form and
 * processing form submissions for adding new books to the database.
 * 
 * @author Noble-Nepal
 * @version 2025-05-21 02:28:36
 */
@MultipartConfig
@WebServlet(asyncSupported = true, urlPatterns = { "/adminproduct" })
public class AdminProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Path constants
    private final String addProductPagePath = "/WEB-INF/pages/addproduct.jsp";
    
    // Message constants
    private final String imageUploadErrorMessage = "Failed to upload the book image.";
    private final String bookAddSuccessMessage = "Book added successfully!";
    private final String bookAddErrorMessage = "Failed to add the book.";
    
    // Image upload constants
    private final String uploadFolderPath = "images/UploadedProfilePicture";
    private final String resourcesPath = "/resources";
    private final String dbImagePathPrefix = "resources/images/UploadedProfilePicture/";

    // Services
    private DropdownService dropdownService;
    private AdminProductService adminProductService;
    private imageUtil imageUtil;

    /**
     * Initializes the servlet by creating service instances.
     * This method is called once when the servlet is first loaded.
     * 
     * @throws ServletException if a servlet-specific error occurs during initialization
     */
    @Override
    public void init() throws ServletException {
        this.dropdownService = new DropdownService();
        this.adminProductService = new AdminProductService();
        this.imageUtil = new imageUtil();
    }

    /**
     * Handles GET requests to fetch dropdown data for the admin product page.
     * Retrieves lists of publishers, authors, and categories from the database
     * and forwards them to the JSP page for display in dropdown menus.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
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
        request.getRequestDispatcher(addProductPagePath).forward(request, response);
    }

    /**
     * Handles POST requests to process form submissions for adding a new book product.
     * Extracts form data including file uploads, validates and processes the input,
     * creates database entries for the book and its relationships, and displays
     * appropriate success or error messages.
     * 
     * @param request  The HttpServletRequest object containing form data
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during file processing or response handling
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

            // Process the book image upload
            String imageUrl = processBookImageUpload(request, response);
            
            // If image upload failed and was required, return early
            if (imageUrl == null && request.getPart("book_img_url").getSize() > 0) {
                return;
            }

            // Create and save the book object
            BookModel book = new BookModel(
                0, // bookID (auto-incremented by database)
                bookTitle, 
                isbn, 
                Date.valueOf(publicationDate), 
                new BigDecimal(price), 
                description,
                Integer.parseInt(stockQuantity), 
                Integer.parseInt(pageCount), 
                imageUrl,
                Integer.parseInt(publisherID)
            );
            
            // Add the book to the database and get its ID
            int bookID = adminProductService.addBookAndGetID(book);

            // Create book relationships if book was added successfully
            if (bookID > 0) {
                // Add author and category associations
                adminProductService.addBookAuthor(bookID, Integer.parseInt(authorID));
                adminProductService.addBookCategory(bookID, Integer.parseInt(categoryID));

                request.setAttribute("successMessage", bookAddSuccessMessage);
            } else {
                request.setAttribute("errorMessage", bookAddErrorMessage);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        // Reload the page with updated dropdown data and status message
        doGet(request, response);
    }
    
    /**
     * Processes the book image upload from the multipart form.
     * Handles validation, file saving, and generates the URL path for database storage.
     * 
     * @param request  The HttpServletRequest containing the file part
     * @param response The HttpServletResponse for potential error forwarding
     * @return The relative URL path for the uploaded image, or null if upload failed
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during file processing
     */
    private String processBookImageUpload(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Retrieve file input using request.getPart()
        Part filePart = request.getPart("book_img_url");
        String imageUrl = null;

        if (filePart != null && filePart.getSize() > 0) {
            // Get the correct real path from servlet context
            String realPath = getServletContext().getRealPath(resourcesPath);


            boolean uploaded = imageUtil.uploadImage(filePart, realPath, uploadFolderPath);

            if (uploaded) {
                String imageName = imageUtil.getImageNameFromPart(filePart);
                // Set the path to save in the database - consistent with profile images
                imageUrl = dbImagePathPrefix + imageName;
            } else {
                request.setAttribute("errorMessage", imageUploadErrorMessage);
                doGet(request, response); // Reload the form with an error message
            }
        }
        
        return imageUrl;
    }
}