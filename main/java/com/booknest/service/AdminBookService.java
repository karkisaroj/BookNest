package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booknest.config.DbConfiguration;
import com.booknest.model.BookModel;

/**
 * Service class for managing books in the admin dashboard. Handles database
 * operations for retrieving, updating, and deleting books.
 * 
 * @author 23047591 Noble-Nepal
 */
public class AdminBookService {
	private final String foreignKeyErrorMessage = "Cannot delete book because it is referenced in another table.";

	// Database connection properties
	private boolean isConnectionError = false;
	private Connection dbConn;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public AdminBookService() {
		try {
			dbConn = DbConfiguration.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			isConnectionError = true;
		}
	}

	/**
	 * Gets all books from the database.
	 * 
	 * @return List of BookModel objects representing books, or null if an error
	 *         occurs
	 */
	public List<BookModel> getAllBooks() {
		if (isConnectionError) {
			return null;
		}

		List<BookModel> books = new ArrayList<>();

		String query = "SELECT bookID, book_title, isbn, publication_date, price, description, "
				+ "stock_quantity, page_count, book_img_url, publisherID " + "FROM book";

		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet result = stmt.executeQuery()) {

			while (result.next()) {
				BookModel book = new BookModel(result.getInt("bookID"), result.getString("book_title"),
						result.getString("isbn"), result.getDate("publication_date"), result.getBigDecimal("price"),
						result.getString("description"), result.getInt("stock_quantity"), result.getInt("page_count"),
						result.getString("book_img_url"), result.getInt("publisherID"));
				books.add(book);
			}
			return books;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Updates the stock quantity of a book by its ID and fetches the updated list
	 * of books.
	 * 
	 * @param bookId   The ID of the book to update
	 * @param newStock The new stock quantity to set
	 * @return List of updated BookModel objects, or null if the update fails
	 */
	public List<BookModel> updateBookStock(int bookId, int newStock) {
		if (isConnectionError) {
			return null;
		}

		String query = "UPDATE book SET stock_quantity = ? WHERE bookID = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, newStock);
			stmt.setInt(2, bookId);
			int rowsAffected = stmt.executeUpdate();

			// Fetch the updated book list only if the update is successful
			if (rowsAffected > 0) {
				return getAllBooks();
			}
		} catch (SQLException e) {
			// Silent failure handling with null return
		}
		return null;
	}

	/**
	 * Deletes a book by its ID, along with its dependencies in `book_author` and
	 * `book_categories` tables. Also fetches the updated list of books.
	 * 
	 * @param bookId The ID of the book to delete
	 * @return List of updated BookModel objects, or null if the delete fails
	 * @throws SQLException if the book is referenced in other tables (e.g.,
	 *                      cart_item)
	 */
	public List<BookModel> deleteBookById(int bookId) throws SQLException {
		if (isConnectionError) {
			return null;
		}

		// SQL queries to delete dependencies and the book
		String deleteBookAuthorQuery = "DELETE FROM book_author WHERE bookID = ?";
		String deleteBookCategoriesQuery = "DELETE FROM book_categories WHERE bookID = ?";
		String deleteBookQuery = "DELETE FROM book WHERE bookID = ?";

		try (
				// Prepare statements for deleting related data
				PreparedStatement deleteBookAuthorStmt = dbConn.prepareStatement(deleteBookAuthorQuery);
				PreparedStatement deleteBookCategoriesStmt = dbConn.prepareStatement(deleteBookCategoriesQuery);
				PreparedStatement deleteBookStmt = dbConn.prepareStatement(deleteBookQuery)) {
			// Begin transaction
			dbConn.setAutoCommit(false);

			// Delete from book_author table
			deleteBookAuthorStmt.setInt(1, bookId);
			deleteBookAuthorStmt.executeUpdate();

			// Delete from book_categories table
			deleteBookCategoriesStmt.setInt(1, bookId);
			deleteBookCategoriesStmt.executeUpdate();

			// Delete from book table
			deleteBookStmt.setInt(1, bookId);
			int rowsAffected = deleteBookStmt.executeUpdate();

			if (rowsAffected > 0) {
				// Commit transaction
				dbConn.commit();

				// Fetch and return the updated book list
				return getAllBooks();
			} else {
				dbConn.rollback();
			}
		} catch (SQLException e) {
			// Rollback transaction on failure
			try {
				dbConn.rollback();
			} catch (SQLException rollbackEx) {
				// Combine rollback exception with the original one
				e.addSuppressed(rollbackEx);
			}

			// Check if the exception is due to a foreign key constraint
			if (e.getSQLState() != null && e.getSQLState().equals("23000")) { // SQL state for foreign key constraint
																				// violation
				throw new SQLException(foreignKeyErrorMessage, e);
			}

			throw e; // Re-throw other SQL exceptions
		} finally {
			// Restore auto-commit mode
			try {
				dbConn.setAutoCommit(true);
			} catch (SQLException autoCommitEx) {
				// Silent handling of auto-commit restoration errors
			}
		}
		return null;
	}

	/**
	 * Closes the database connection when the service is no longer needed. Should
	 * be called explicitly when done with this service.
	 */
	public void closeConnection() {
		try {
			if (dbConn != null && !dbConn.isClosed()) {
				dbConn.close();
			}
		} catch (SQLException ex) {
			// Silent handling of close errors
		}
	}
}