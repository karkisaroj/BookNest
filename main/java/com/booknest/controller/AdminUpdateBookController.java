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

import com.booknest.model.AuthorModel;
import com.booknest.model.BookModel;
import com.booknest.model.CategoryModel;
import com.booknest.model.PublisherModel;
import com.booknest.service.AdminProductService;
import com.booknest.service.DropdownService;
import com.booknest.util.imageUtil;

/**
 * Controller for handling book update operations in the admin panel.
 * Manages the retrieval of book details for editing and processing of update form submissions.
 * 
 * @author 23047591 Noble-Nepal
 */
@MultipartConfig
@WebServlet(asyncSupported = true, urlPatterns = { "/adminupdatebook" })
public class AdminUpdateBookController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Path constants
    private final String addProductPagePath = "/WEB-INF/pages/addproduct.jsp";
    
    // Error and success message constants
    private final String bookNotFoundMessage = "Book not found for the given ID.";
    private final String invalidBookIdMessage = "Invalid book ID.";
    private final String missingBookIdMessage = "Book ID is missing.";
    private final String imageUploadErrorMessage = "Failed to upload the book image.";
    private final String bookUpdateSuccessMessage = "Book updated successfully!";
    private final String bookUpdateErrorMessage = "Failed to update the book.";
    private final String invalidInputMessage = "Invalid input data.";
    private final String bookIdRequiredMessage = "Book ID is required for updating.";
    
    // Image upload constants
    private final String uploadFolderPath = "images/UploadedProfilePicture";
    private final String resourcesPath = "/resources";
    private final String dbImagePathPrefix = "resources/images/UploadedProfilePicture/";

    // Services
    private AdminProductService adminProductService;
    private DropdownService dropdownService;
    private imageUtil imageUtil;

    /**
     * Initializes the servlet by creating service and utility instances.
     * 
     * @throws ServletException if a servlet-specific error occurs during initialization
     */
    @Override
    public void init() throws ServletException {
        this.adminProductService = new AdminProductService();
        this.dropdownService = new DropdownService();
        this.imageUtil = new imageUtil();
    }

    /**
     * Handles GET requests to fetch book details for updating.
     * Retrieves book information by ID and loads related dropdown data
     * for publishers, authors, and categories.
     * 
     * @param request  The HttpServletRequest object containing client request
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during response handling
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookIDParam = request.getParameter("bookID");

        // Fetch dropdown data using the service
        List<PublisherModel> publishers = dropdownService.getPublishers();
        List<AuthorModel> authors = dropdownService.getAuthors();
        List<CategoryModel> categories = dropdownService.getCategories();

        // Set the data as request attributes
        request.setAttribute("publishers", publishers);
        request.setAttribute("authors", authors);
        request.setAttribute("categories", categories);

        if (bookIDParam != null) {
            try {
                int bookID = Integer.parseInt(bookIDParam);

                // Fetch the book details by ID
                BookModel book = adminProductService.getBookById(bookID);
                if (book != null) {
                    request.setAttribute("book", book);
                    request.setAttribute("action", "update");
                } else {
                    request.setAttribute("errorMessage", bookNotFoundMessage);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", invalidBookIdMessage);
            }
        } else {
            request.setAttribute("errorMessage", missingBookIdMessage);
        }

        // Forward the request to the JSP page
        request.getRequestDispatcher(addProductPagePath).forward(request, response);
    }

    /**
     * Handles POST requests to update book details.
     * Processes the form submission, validates input, updates the book information,
     * and manages related author and category relationships.
     * 
     * @param request  The HttpServletRequest object containing form data
     * @param response The HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during file processing or response handling
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookIDParam = request.getParameter("bookID");

        if (bookIDParam != null) {
            try {
                int bookID = Integer.parseInt(bookIDParam);

                // Retrieve updated book details from the request
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

                // Process the book image upload if a new image was provided
                String imageUrl = processBookImageUpload(request, response);
                
                // If image upload failed and was required, return early
                if (imageUrl == null && request.getPart("book_img_url").getSize() > 0) {
                    return;
                }

                // Create a BookModel object with updated details
                BookModel updatedBook = new BookModel(
                    bookID, 
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

                // Update the book in the database
                boolean isUpdated = adminProductService.updateBook(updatedBook);

                if (isUpdated) {
                    // Update author and category relationships
                    adminProductService.updateBookAuthor(bookID, Integer.parseInt(authorID));
                    adminProductService.updateBookCategory(bookID, Integer.parseInt(categoryID));

                    request.setAttribute("successMessage", bookUpdateSuccessMessage);
                } else {
                    request.setAttribute("errorMessage", bookUpdateErrorMessage);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", invalidInputMessage);
                e.printStackTrace();
            } catch (Exception e) {
                request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            request.setAttribute("errorMessage", bookIdRequiredMessage);
        }

        // Redirect back to the GET request to reload the updated book details
        doGet(request, response);
    }
    
    /**
     * Processes the book image upload from the multipart form.
     * Handles validation, file saving, and generates the URL path for database storage.
     * 
     * @param request  The HttpServletRequest containing the file part
     * @param response The HttpServletResponse for potential error forwarding
     * @return The relative URL path for the uploaded image, or null if upload failed or no image was provided
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