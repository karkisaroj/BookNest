package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.model.BookCartModel;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class BookServiceImpl implements BookService {
	@Override
	public List<BookCartModel> searchBooksByTitle(String title) throws Exception {
		List<BookCartModel> books = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		System.out.println("SEARCH DEBUG: Starting search for title: '" + title + "'");

		try {
			conn = DbConfiguration.getDbConnection();
			System.out.println("SEARCH DEBUG: Database connection established: " + (conn != null));

			// Updated SQL query to include author information
			String sql = "SELECT b.*, GROUP_CONCAT(DISTINCT a.author_name SEPARATOR ', ') AS author_name "
					+ "FROM book b " + "LEFT JOIN book_author ba ON b.bookID = ba.bookID "
					+ "LEFT JOIN author a ON ba.authorID = a.authorID " + "WHERE b.book_title LIKE ? "
					+ "GROUP BY b.bookID";

			System.out.println("SEARCH DEBUG: Using SQL: " + sql);

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + title + "%");
			System.out.println("SEARCH DEBUG: Search parameter: %" + title + "%");

			rs = stmt.executeQuery();
			System.out.println("SEARCH DEBUG: Query executed successfully");

			int count = 0;
			while (rs.next()) {
				count++;
				BookCartModel book = new BookCartModel();
				book.setBookID(rs.getInt("bookID"));
				book.setBook_title(rs.getString("book_title"));
				book.setIsbn(rs.getString("isbn"));
				book.setPrice(rs.getBigDecimal("price"));
				book.setDescription(rs.getString("description"));
				book.setStock_quantity(rs.getInt("stock_quantity"));
				book.setBook_img_url(rs.getString("book_img_url"));
				book.setPublisherID(rs.getInt("publisherID"));
				book.setAuthorName(rs.getString("author_name")); // Set the author name from the query

				System.out.println("SEARCH DEBUG: Found book #" + count + ": " + book.getBookID() + " - "
						+ book.getBook_title() + " by " + book.getAuthorName());

				books.add(book);
			}

			System.out.println("SEARCH DEBUG: Total books found: " + count);

		} catch (Exception e) {
			System.err.println("SEARCH ERROR: " + e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				System.out.println("SEARCH DEBUG: Resources closed properly");
			} catch (SQLException e) {
				System.err.println("SEARCH ERROR: Failed to close resources: " + e.getMessage());
			}
		}

		return books;
	}

	@Override
	public List<BookCartModel> getAllBooks() throws ClassNotFoundException {
		List<BookCartModel> books = new ArrayList<>();

		try (Connection conn = DbConfiguration.getDbConnection()) {
			if (conn == null) {
				System.err.println("Database connection failed - connection is null");
				return books;
			}

			// Simple query without joins for testing
			String sql = "SELECT * FROM book";

			try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

				while (rs.next()) {
					BookCartModel book = new BookCartModel();
					book.setBookID(rs.getInt("bookID"));
					book.setBook_title(rs.getString("book_title"));
					book.setIsbn(rs.getString("isbn"));
					book.setPrice(rs.getBigDecimal("price"));
					book.setDescription(rs.getString("description"));
					book.setStock_quantity(rs.getInt("stock_quantity"));
					book.setBook_img_url(rs.getString("book_img_url"));

					// Set a default author name since we're not joining with author table
					book.setAuthorName("Unknown");

					books.add(book);
					
				}

				
			}
		} catch (SQLException e) {
			System.err.println("Error getting all books (simple query): " + e.getMessage());
			e.printStackTrace();
		}

		return books;
	}

	@Override
	public BookCartModel getBookById(int bookId) throws Exception {
		if (bookId <= 0)
			throw new Exception("Invalid Book ID: " + bookId);
		BookCartModel book = null;

		String sql = "SELECT " + "  b.bookID, b.book_title, b.description, b.price, b.book_img_url, "
				+ "  GROUP_CONCAT(DISTINCT a.author_name ORDER BY a.author_name SEPARATOR ', ') AS authors "
				+ "FROM book b " + "LEFT JOIN book_author ba ON b.bookID = ba.bookID "
				+ "LEFT JOIN author a ON ba.authorID = a.authorID " + "WHERE b.bookID = ? "
				+ "GROUP BY b.bookID, b.book_title, b.description, b.price, b.book_img_url";

		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					book = mapRowToBookCartModel(rs); // Updated method name
				} else {
					throw new Exception("Book with ID " + bookId + " not found.");
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println("SERVICE DB Error finding book by ID " + bookId + ": " + e.getMessage());
			e.printStackTrace(); // Log full trace for SQL errors
			throw new Exception("Could not retrieve book " + bookId + ".", e);
		}
		return book;
	}

	@Override
	public BookCartModel addBook(BookCartModel book) throws Exception {
		System.err
				.println("WARNING: BookServiceImpl.addBook does NOT currently handle author relationships correctly!");
		// (Existing code that only inserts into 'book' table - Author info is ignored
		// on insert)
		if (book == null || book.getBook_title() == null || book.getBook_title().trim().isEmpty()
				|| book.getPrice() == null || book.getPrice().compareTo(BigDecimal.ZERO) < 0)
			throw new Exception("Invalid book data.");

		String sql = "INSERT INTO book (book_title, description, price, book_img_url) VALUES (?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet keys = null;
		try {
			conn = DbConfiguration.getDbConnection();
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, book.getBook_title().trim());
			ps.setString(2, book.getDescription());
			ps.setBigDecimal(3, book.getPrice());
			ps.setString(4, book.getBook_img_url());
			int rows = ps.executeUpdate();
			if (rows > 0) {
				keys = ps.getGeneratedKeys();
				if (keys.next()) {
					book.setBookID(keys.getInt(1));
					return book;
				} else
					throw new Exception("Added but failed to get ID.");
			} else
				throw new Exception("Failed to add book.");
		} catch (SQLException | ClassNotFoundException e) {
			throw new Exception("Could not add book.", e);
		} finally {
			close(keys);
			close(ps);
			close(conn);
		}
	}

	@Override
	public List<BookCartModel> getTopAddedToCartBooks(int limit) throws Exception { // Changed return type to
																					// BookCartModel
		List<BookCartModel> topBooks = new ArrayList<>();
		String sql = "SELECT b.bookID, b.book_title, b.price, b.book_img_url, COUNT(ci.bookID) AS times_added "
				+ "FROM book b " + "JOIN cart_item ci ON b.bookID = ci.bookID "
				+ "GROUP BY b.bookID, b.book_title, b.price, b.book_img_url " + "ORDER BY times_added DESC "
				+ "LIMIT ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbConfiguration.getDbConnection();
			if (conn == null)
				throw new Exception("Failed to get database connection.");
			ps = conn.prepareStatement(sql);
			ps.setInt(1, limit);
			System.out.println("Executing SQL for top books: " + ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				BookCartModel book = new BookCartModel(); // Changed to BookCartModel
				book.setBookID(rs.getInt("bookID"));
				book.setBook_title(rs.getString("book_title"));
				book.setPrice(rs.getBigDecimal("price"));
				book.setBook_img_url(rs.getString("book_img_url"));
				topBooks.add(book);
			}
			System.out.println("Found " + topBooks.size() + " top books in service.");
		} catch (SQLException e) {
			System.err.println("SQL Error getting top books: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Could not retrieve top added books: " + e.getMessage(), e);
		} finally {
			// Close resources
			close(rs);
			close(ps);
			close(conn);
		}
		return topBooks;
	}

	@Override
	public List<BookCartModel> getRandomBooks(int limit) throws Exception { // Changed return type to BookCartModel
		List<BookCartModel> randomBooks = new ArrayList<>();
		// Query to get random books (Syntax might vary for non-MySQL DBs)
		String sql = "SELECT bookID, book_title, price, book_img_url FROM book ORDER BY RAND() LIMIT ?"; // Using RAND()
																											// for MySQL

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DbConfiguration.getDbConnection();
			if (conn == null) {
				throw new Exception("Failed to get database connection.");
			}
			ps = conn.prepareStatement(sql);
			ps.setInt(1, limit);

			System.out.println("Executing SQL for random books: " + ps.toString());

			rs = ps.executeQuery();

			while (rs.next()) {
				BookCartModel book = new BookCartModel(); // Changed to BookCartModel
				book.setBookID(rs.getInt("bookID"));
				book.setBook_title(rs.getString("book_title"));
				book.setPrice(rs.getBigDecimal("price"));
				book.setBook_img_url(rs.getString("book_img_url"));
				randomBooks.add(book);
			}
			System.out.println("Found " + randomBooks.size() + " random books in service.");
		} catch (SQLException e) {
			System.err.println("SQL Error getting random books: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Could not retrieve random books: " + e.getMessage(), e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}
		return randomBooks;
	}

	@Override
	public List<BookCartModel> getBooksByCategory(Integer categoryId) throws Exception {
		List<BookCartModel> books = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		System.out.println("CATEGORY SEARCH DEBUG: Starting search for category ID: " + categoryId);

		try {
			conn = DbConfiguration.getDbConnection();
			System.out.println("CATEGORY SEARCH DEBUG: Database connection established: " + (conn != null));

			// Modified SQL to properly handle multiple authors per book using GROUP_CONCAT
			String sql = "SELECT b.*, GROUP_CONCAT(DISTINCT a.author_name SEPARATOR ', ') AS author_name "
					+ "FROM book b " + "INNER JOIN book_categories bc ON b.bookID = bc.bookID "
					+ "LEFT JOIN book_author ba ON b.bookID = ba.bookID "
					+ "LEFT JOIN author a ON ba.authorID = a.authorID " + "WHERE bc.categoryID = ? "
					+ "GROUP BY b.bookID";

			System.out.println("CATEGORY SEARCH DEBUG: Using SQL: " + sql);
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, categoryId);
			System.out.println("CATEGORY SEARCH DEBUG: Parameter set: categoryId=" + categoryId);

			rs = stmt.executeQuery();
			System.out.println("CATEGORY SEARCH DEBUG: Query executed successfully");

			int count = 0;
			while (rs.next()) {
				count++;
				BookCartModel book = new BookCartModel();
				book.setBookID(rs.getInt("bookID"));
				book.setBook_title(rs.getString("book_title"));
				book.setIsbn(rs.getString("isbn"));
				book.setPrice(rs.getBigDecimal("price"));
				book.setDescription(rs.getString("description"));
				book.setStock_quantity(rs.getInt("stock_quantity"));
				book.setBook_img_url(rs.getString("book_img_url"));
				book.setPublisherID(rs.getInt("publisherID"));
				book.setAuthorName(rs.getString("author_name"));

				System.out.println("CATEGORY SEARCH DEBUG: Found book #" + count + ": " + book.getBookID() + " - "
						+ book.getBook_title());

				books.add(book);
			}

			System.out.println("CATEGORY SEARCH DEBUG: Total books found for category " + categoryId + ": " + count);

		} catch (Exception e) {
			System.err.println("CATEGORY SEARCH ERROR: " + e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				System.out.println("CATEGORY SEARCH DEBUG: Resources closed properly");
			} catch (SQLException e) {
				System.err.println("CATEGORY SEARCH ERROR: Failed to close resources: " + e.getMessage());
			}
		}

		return books;
	}

	@Override
	public boolean updateBook(BookCartModel book) throws Exception { // Changed parameter type to BookCartModel
		System.err.println(
				"WARNING: BookServiceImpl.updateBook does NOT currently handle author relationships correctly!");

		if (book == null || book.getBookID() <= 0 || book.getBook_title() == null
				|| book.getBook_title().trim().isEmpty() || book.getPrice() == null
				|| book.getPrice().compareTo(BigDecimal.ZERO) < 0)
			throw new Exception("Invalid book data for update.");

		String sql = "UPDATE book SET book_title = ?, description = ?, price = ?, book_img_url = ? WHERE bookID = ?";

		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, book.getBook_title().trim());
			ps.setString(2, book.getDescription());
			ps.setBigDecimal(3, book.getPrice());
			ps.setString(4, book.getBook_img_url());
			ps.setInt(5, book.getBookID());
			return ps.executeUpdate() > 0;
		} catch (SQLException | ClassNotFoundException e) {
			throw new Exception("Could not update book " + book.getBookID() + ".", e);
		}
	}

	@Override
	public boolean deleteBook(int bookId) throws Exception {
		// This might need adjustment if book_author has foreign key constraints
		// that prevent deleting a book if it's linked to authors.
		System.err.println(
				"WARNING: BookServiceImpl.deleteBook might fail if foreign key constraints exist on book_author table.");
		if (bookId <= 0)
			throw new Exception("Invalid Book ID for deletion.");
		String sql = "DELETE FROM book WHERE bookID = ?";
		try (Connection conn = DbConfiguration.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookId);
			return ps.executeUpdate() > 0;
		} catch (SQLException | ClassNotFoundException e) {
			if (e instanceof SQLException && ((SQLException) e).getSQLState() != null
					&& ((SQLException) e).getSQLState().startsWith("23"))
				throw new Exception("Could not delete book " + bookId + " (referenced elsewhere).", e);
			throw new Exception("Could not delete book " + bookId + ".", e);
		}
	}

	// Changed method name and return type to match BookCartModel
	private BookCartModel mapRowToBookCartModel(ResultSet rs) throws SQLException {
		BookCartModel book = new BookCartModel();
		book.setBookID(rs.getInt("bookID"));
		book.setBook_title(rs.getString("book_title"));
		book.setDescription(rs.getString("description"));
		book.setPrice(rs.getBigDecimal("price"));
		book.setBook_img_url(rs.getString("book_img_url"));
		// Get the comma-separated author list from the 'authors' alias created by
		// GROUP_CONCAT
		book.setAuthorName(rs.getString("authors")); // Use the alias 'authors'
		return book;
	}

	// Helper method to safely close JDBC resources
	private void close(AutoCloseable resource) {
		if (resource != null)
			try {
				resource.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public List<BookCartModel> searchBooksByCategories(String[] categories) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookCartModel> searchBooksByTitleAndCategories(String title, String[] categories) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}