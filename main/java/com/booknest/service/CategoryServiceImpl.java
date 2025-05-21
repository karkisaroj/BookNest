package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CategoryService interface that handles category-related
 * operations with the database.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class CategoryServiceImpl implements CategoryService {

	// SQL query constants
	private static final String SQL_GET_ALL_CATEGORIES = "SELECT categoryID, category_name, category_description FROM categories";
	private static final String SQL_GET_CATEGORY_BY_ID = "SELECT categoryID, category_name, category_description FROM categories WHERE categoryID = ?";

	// Column name constants
	private static final String COLUMN_CATEGORY_ID = "categoryID";
	private static final String COLUMN_CATEGORY_NAME = "category_name";
	private static final String COLUMN_CATEGORY_DESCRIPTION = "category_description";

	/**
	 * Retrieves all categories from the database.
	 * 
	 * @return List of all categories
	 * @throws SQLException           If a database error occurs
	 * @throws ClassNotFoundException If the database driver class is not found
	 */
	@Override
	public List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
		List<Category> categories = new ArrayList<>();

		// FIXED: Changed 'description' to 'category_description' to match your database
		// column
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_GET_ALL_CATEGORIES);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Category category = new Category();
				category.setCategoryId(rs.getInt(COLUMN_CATEGORY_ID));
				category.setCategoryName(rs.getString(COLUMN_CATEGORY_NAME));

				// FIXED: Map 'category_description' column to description field
				category.setDescription(rs.getString(COLUMN_CATEGORY_DESCRIPTION));

				categories.add(category);
			}
		}

		return categories;
	}

	/**
	 * Retrieves a category by its ID (int version).
	 * 
	 * @param id The ID of the category to retrieve
	 * @return The category with the specified ID, or null if not found
	 * @throws SQLException           If a database error occurs
	 * @throws ClassNotFoundException If the database driver class is not found
	 */
	public Category getCategoryById(int id) throws SQLException, ClassNotFoundException {
		// FIXED: Changed 'description' to 'category_description' to match your database
		// column
		try (Connection conn = DbConfiguration.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_GET_CATEGORY_BY_ID)) {

			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Category category = new Category();
					category.setCategoryId(rs.getInt(COLUMN_CATEGORY_ID));
					category.setCategoryName(rs.getString(COLUMN_CATEGORY_NAME));

					// FIXED: Map 'category_description' column to description field
					category.setDescription(rs.getString(COLUMN_CATEGORY_DESCRIPTION));

					return category;
				}
			}
		}

		return null;
	}

	/**
	 * Adds a new category to the database.
	 * 
	 * @param category The category to add
	 * @return The ID of the newly added category
	 * @throws Exception If a database error occurs
	 */
	@Override
	public int addCategory(Category category) throws Exception {
		return 0;
	}

	/**
	 * Updates an existing category in the database.
	 * 
	 * @param category The category with updated information
	 * @return true if the update was successful, false otherwise
	 * @throws Exception If a database error occurs
	 */
	@Override
	public boolean updateCategory(Category category) throws Exception {
		return false;
	}

	/**
	 * Deletes a category from the database.
	 * 
	 * @param categoryId The ID of the category to delete
	 * @return true if the deletion was successful, false otherwise
	 * @throws Exception If a database error occurs
	 */
	@Override
	public boolean deleteCategory(Integer categoryId) throws Exception {
		return false;
	}

	/**
	 * Retrieves a category by its ID (Integer version).
	 * 
	 * @param categoryId The ID of the category to retrieve
	 * @return The category with the specified ID, or null if not found
	 * @throws Exception If a database error occurs
	 */
	@Override
	public Category getCategoryById(Integer categoryId) throws Exception {
		return null;
	}
}