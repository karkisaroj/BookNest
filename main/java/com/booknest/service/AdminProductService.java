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
     * @param bookID   The ID of the book
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
     * Links a book to a category in the book_categories table.
     *
     * @param bookID     The ID of the book
     * @param categoryID The ID of the category
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

    /**
     * Fetches a book by its ID.
     *
     * @param bookID The ID of the book
     * @return The BookModel object if found, null otherwise
     */
    public BookModel getBookById(int bookID) {
        String query = "SELECT b.*, ba.authorID FROM book b LEFT JOIN book_author ba ON b.bookID = ba.bookID WHERE b.bookID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BookModel book = new BookModel(
                        rs.getInt("bookID"),
                        rs.getString("book_title"),
                        rs.getString("isbn"),
                        rs.getDate("publication_date"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock_quantity"),
                        rs.getInt("page_count"),
                        rs.getString("book_img_url"),
                        rs.getInt("publisherID")
                    );
                    book.setAuthorID(rs.getInt("authorID")); // Set the authorID
                    return book;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no book is found
    }

    /**
     * Updates the book details in the database.
     *
     * @param book The BookModel object containing updated book details
     * @return true if the update was successful, false otherwise
     */
    public boolean updateBook(BookModel book) {
        String query = "UPDATE book SET book_title = ?, isbn = ?, publication_date = ?, price = ?, description = ?, stock_quantity = ?, page_count = ?, book_img_url = ?, publisherID = ? WHERE bookID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, book.getBookTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setDate(3, book.getPublicationDate());
            stmt.setBigDecimal(4, book.getPrice());
            stmt.setString(5, book.getDescription());
            stmt.setInt(6, book.getStockQuantity());
            stmt.setInt(7, book.getPageCount());
            stmt.setString(8, book.getBookImgUrl());
            stmt.setInt(9, book.getPublisherID());
            stmt.setInt(10, book.getBookID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the author for a book in the book_author table.
     *
     * @param bookID   The ID of the book
     * @param authorID The new author ID
     * @return true if the update was successful, false otherwise
     */
    public boolean updateBookAuthor(int bookID, int authorID) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return false;
        }

        String deleteQuery = "DELETE FROM book_author WHERE bookID = ?";
        String insertQuery = "INSERT INTO book_author (bookID, authorID) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = dbConn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {

            deleteStmt.setInt(1, bookID);
            deleteStmt.executeUpdate();

            insertStmt.setInt(1, bookID);
            insertStmt.setInt(2, authorID);
            return insertStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the category for a book in the book_categories table.
     *
     * @param bookID     The ID of the book
     * @param categoryID The new category ID
     * @return true if the update was successful, false otherwise
     */
    public boolean updateBookCategory(int bookID, int categoryID) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return false;
        }

        String deleteQuery = "DELETE FROM book_categories WHERE bookID = ?";
        String insertQuery = "INSERT INTO book_categories (bookID, categoryID) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = dbConn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {

            deleteStmt.setInt(1, bookID);
            deleteStmt.executeUpdate();

            insertStmt.setInt(1, bookID);
            insertStmt.setInt(2, categoryID);
            return insertStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}