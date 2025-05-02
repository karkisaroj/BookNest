package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.booknest.config.DbConfiguration;
import com.booknest.model.BookModel;

public class AdminProductService {
    private boolean isConnectionError = false;
    private Connection dbConn;

    /**
     * Constructor initializes the database connection. Sets the connection error
     * flag if the connection fails.
     */
    public AdminProductService() {
        try {
            dbConn = DbConfiguration.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }

    /**
     * Inserts a new book into the database and returns the generated book ID.
     *
     * @param bookModel The BookModel object containing book details
     * @return The generated book ID, or -1 if insertion fails
     */
    public int addBookAndGetID(BookModel bookModel) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return -1;
        }

        String query = "INSERT INTO book (book_title, isbn, publication_date, price, description, stock_quantity, page_count, book_img_url, publisherID) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dbConn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bookModel.getBookTitle());
            stmt.setString(2, bookModel.getIsbn());
            stmt.setDate(3, bookModel.getPublicationDate());
            stmt.setBigDecimal(4, bookModel.getPrice());
            stmt.setString(5, bookModel.getDescription());
            stmt.setInt(6, bookModel.getStockQuantity());
            stmt.setInt(7, bookModel.getPageCount());
            stmt.setString(8, bookModel.getBookImgUrl());
            stmt.setInt(9, bookModel.getPublisherID());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated book ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return -1 if insertion fails
    }

    /**
     * Links a book to an author in the book_author table.
     *
     * @param bookID  The ID of the book
     * @param authorID The ID of the author
     * @return true if the relationship was successfully inserted, false otherwise
     */
    public boolean addBookAuthor(int bookID, int authorID) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return false;
        }

        String query = "INSERT INTO book_author (bookID, authorID) VALUES (?, ?)";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, bookID);
            stmt.setInt(2, authorID);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Returns true if the row is inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Links a book to an author in the book_categoryID table.
     *
     * @param bookID  The ID of the book
     * @param categoryID The ID of the author
     * @return true if the relationship was successfully inserted, false otherwise
     */
    public boolean addBookCategory(int bookID, int categoryID) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return false;
        }

        String query = "INSERT INTO book_categories (bookID, categoryID) VALUES (?, ?)";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, bookID);
            stmt.setInt(2, categoryID);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}