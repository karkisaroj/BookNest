package com.booknest.service;

import com.booknest.model.Category;
import java.util.List;

/**
 * Interface for Category Service
 */
public interface CategoryService {

	/**
	 * Get all categories
	 * 
	 * @return list of all categories
	 * @throws Exception if any error occurs
	 */
	List<Category> getAllCategories() throws Exception;

	/**
	 * Get category by ID
	 * 
	 * @param categoryId the ID of the category
	 * @return the category with the given ID, or null if not found
	 * @throws Exception if any error occurs
	 */
	Category getCategoryById(Integer categoryId) throws Exception;

	/**
	 * Add a new category
	 * 
	 * @param category the category to add
	 * @return the ID of the newly added category
	 * @throws Exception if any error occurs
	 */
	int addCategory(Category category) throws Exception;

	/**
	 * Update an existing category
	 * 
	 * @param category the category to update
	 * @return true if successfully updated, false otherwise
	 * @throws Exception if any error occurs
	 */
	boolean updateCategory(Category category) throws Exception;

	/**
	 * Delete a category
	 * 
	 * @param categoryId the ID of the category to delete
	 * @return true if successfully deleted, false otherwise
	 * @throws Exception if any error occurs
	 */
	boolean deleteCategory(Integer categoryId) throws Exception;
}