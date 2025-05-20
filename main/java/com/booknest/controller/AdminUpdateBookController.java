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



@MultipartConfig
@WebServlet(asyncSupported = true, urlPatterns = { "/adminupdatebook" })
public class AdminUpdateBookController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AdminProductService adminProductService;
    private DropdownService dropdownService;
    private imageUtil imageUtil;

    @Override
    public void init() throws ServletException {
        this.adminProductService = new AdminProductService();
        this.dropdownService = new DropdownService();
        this.imageUtil = new imageUtil();
    }

    /**
     * Handles GET requests to fetch book details for updating.
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
                    request.setAttribute("errorMessage", "Book not found for the given ID.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid book ID.");
            }
        } else {
            request.setAttribute("errorMessage", "Book ID is missing.");
        }

        // Forward the request to the JSP page
        request.getRequestDispatcher("/WEB-INF/pages/addproduct.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to update book details.
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

                // Retrieve file input using request.getPart()
                Part filePart = request.getPart("book_img_url"); // File input field name in the form
                String imageUrl = null;

                if (filePart != null && filePart.getSize() > 0) {
                    // UPDATED: Use "images/UploadedProfilePicture" as the saveFolder
                    String saveFolder = "images/UploadedProfilePicture";

                    // Get the correct real path from servlet context
                    String realPath = getServletContext().getRealPath("/resources");

                    System.out.println("Uploading book image to: " + realPath + " folder: " + saveFolder);

                    boolean uploaded = imageUtil.uploadImage(filePart, realPath, saveFolder);

                    if (uploaded) {
                        String imageName = imageUtil.getImageNameFromPart(filePart);
                        // Set the path to save in the database - consistent with profile images
                        imageUrl = "resources/images/UploadedProfilePicture/" + imageName;
                        System.out.println("Book image URL in DB: " + imageUrl);
                    } else {
                        request.setAttribute("errorMessage", "Failed to upload the book image.");
                        doGet(request, response); // Reload the form with an error message
                        return;
                    }
                }

                // Create a BookModel object with updated details
                BookModel updatedBook = new BookModel(bookID, bookTitle, isbn, Date.valueOf(publicationDate),
                        new BigDecimal(price), description, Integer.parseInt(stockQuantity), Integer.parseInt(pageCount),
                        imageUrl, Integer.parseInt(publisherID));

                // Update the book in the database
                boolean isUpdated = adminProductService.updateBook(updatedBook);

                if (isUpdated) {
                    // Update author and category links
                    adminProductService.updateBookAuthor(bookID, Integer.parseInt(authorID));
                    adminProductService.updateBookCategory(bookID, Integer.parseInt(categoryID));

                    request.setAttribute("successMessage", "Book updated successfully!");
                } else {
                    request.setAttribute("errorMessage", "Failed to update the book.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid input data.");
                e.printStackTrace();
            } catch (Exception e) {
                request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            request.setAttribute("errorMessage", "Book ID is required for updating.");
        }

        // Redirect back to the GET request to reload the updated book details
        doGet(request, response);
    }
}