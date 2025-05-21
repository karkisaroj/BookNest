package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booknest.config.DbConfiguration;
import com.booknest.model.PublisherModel;
import com.booknest.model.AuthorModel;
import com.booknest.model.CategoryModel;

/**
 * Service class for retrieving dropdown data from the database. Provides
 * methods to fetch publishers, authors, and categories for use in select menus.
 * 
 * @author 23047591 Noble-Nepal
 */
public class DropdownService {
	// Database connection properties
	private boolean isConnectionError = false;
	private Connection dbConn;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public DropdownService() {
		try {
			dbConn = DbConfiguration.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			isConnectionError = true;
		}
	}

	/**
	 * Retrieves all publishers from the database.
	 * 
	 * @return List of PublisherModel objects representing all publishers
	 */
	public List<PublisherModel> getPublishers() {
		List<PublisherModel> publishers = new ArrayList<>();
		if (isConnectionError) {
			return publishers;
		}

		String query = "SELECT publisherID, publisher_name FROM publisher";

		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				PublisherModel publisher = new PublisherModel(rs.getInt("publisherID"), rs.getString("publisher_name"));
				publishers.add(publisher);
			}
		} catch (SQLException e) {
			// Silent handling with empty list return
		}

		return publishers;
	}

	/**
	 * Retrieves all authors from the database.
	 * 
	 * @return List of AuthorModel objects representing all authors
	 */
	public List<AuthorModel> getAuthors() {
		List<AuthorModel> authors = new ArrayList<>();
		if (isConnectionError) {
			return authors;
		}

		String query = "SELECT authorID, author_name FROM author";

		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				AuthorModel author = new AuthorModel(rs.getInt("authorID"), rs.getString("author_name"));
				authors.add(author);
			}
		} catch (SQLException e) {
			// Silent handling with empty list return
		}

		return authors;
	}

	/**
	 * Retrieves all categories from the database.
	 * 
	 * @return List of CategoryModel objects representing all categories
	 */
	public List<CategoryModel> getCategories() {
		List<CategoryModel> categories = new ArrayList<>();
		if (isConnectionError) {
			return categories;
		}

		String query = "SELECT categoryID, category_name, category_description FROM categories";

		try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				CategoryModel category = new CategoryModel(rs.getInt("categoryID"), rs.getString("category_name"),
						rs.getString("category_description"));
				categories.add(category);
			}
		} catch (SQLException e) {
			// Silent handling with empty list return
		}

		return categories;
	}

	/**
	 * Retrieves a specific publisher by ID.
	 * 
	 * @param publisherID The ID of the publisher to retrieve
	 * @return The PublisherModel object if found, null otherwise
	 */
	public PublisherModel getPublisherById(int publisherID) {
		if (isConnectionError) {
			return null;
		}

		String query = "SELECT publisherID, publisher_name FROM publisher WHERE publisherID = ?";
		PublisherModel publisher = null;

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, publisherID);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					publisher = new PublisherModel(rs.getInt("publisherID"), rs.getString("publisher_name"));
				}
			}
		} catch (SQLException e) {
			// Silent handling with null return
		}

		return publisher;
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