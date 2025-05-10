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
 * Service class for managing the admin dashboard, including retrieving popular
 * books.
 * 
 * @author Noble Nepal
 */
public class AdminDashboardService {
	private boolean isConnectionError = false;
	private Connection dbConn;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public AdminDashboardService() {
		try {
			dbConn = DbConfiguration.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}

	// Method to calculate total revenue
	public double getTotalRevenue() throws SQLException {
		String query = "SELECT SUM(payment_amount) AS totalRevenue " + "FROM payment "
				+ "WHERE payment_status = 'completed'";
		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getDouble("totalRevenue");
			}
		}
		return 0;
	}

	// Method to calculate total orders
	public int getTotalOrders() throws SQLException {
		String query = "SELECT COUNT(*) AS totalOrders " + "FROM orders " 
				+ "AND order_date >= CURDATE() - INTERVAL 30 DAY";
		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("totalOrders");
			}
		}
		return 0;
	}

	// Method to calculate total books sold
	public int getTotalBooksSold() throws SQLException {
		String query = "SELECT COUNT(ob.bookID) AS totalBooksSold " + "FROM order_book ob "
				+ "JOIN orders o ON ob.orderID = o.orderID " + "WHERE o.order_status = 'completed'";
		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("totalBooksSold");
			}
		}
		return 0;
	}

	/**
	 * Retrieves the top 5 most popular books based on the number of times they
	 * appear in the bridging table.
	 * 
	 * @return List of BookModel objects representing the top 5 most popular books,
	 *         or null if an error occurs.
	 */
	public List<BookModel> getTopPopularBooks() {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		List<BookModel> popularBooks = new ArrayList<>();

		String query = "SELECT b.bookID, b.book_title, b.book_img_url, COUNT(bt.orderID) AS sold_count "
				+ "FROM book b " + "JOIN order_book bt ON b.bookID = bt.bookID "
				+ "GROUP BY b.bookID, b.book_title, b.book_img_url " + "ORDER BY sold_count DESC " + "LIMIT 5";

		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet resultSet = stmt.executeQuery()) {

			while (resultSet.next()) {
				// Using the new constructor to initialize BookModel
				BookModel book = new BookModel(resultSet.getInt("bookID"), // bookID
						resultSet.getString("book_title"), // bookTitle
						resultSet.getString("book_img_url"), // bookImgUrl
						resultSet.getInt("sold_count") // soldCount
				);

				popularBooks.add(book);
			}

			return popularBooks;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}