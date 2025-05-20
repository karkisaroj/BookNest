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
 * Implementation of CategoryService interface
 */
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        
        // FIXED: Changed 'description' to 'category_description' to match your database column
        String sql = "SELECT categoryID, category_name, category_description FROM categories";
        
        try (Connection conn =DbConfiguration.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("category_name"));
                
                // FIXED: Map 'category_description' column to description field
                category.setDescription(rs.getString("category_description"));
                
                categories.add(category);
            }
        }
        
        return categories;
    }
    
    public Category getCategoryById(int id) throws SQLException, ClassNotFoundException {
        // FIXED: Changed 'description' to 'category_description' to match your database column
        String sql = "SELECT categoryID, category_name, category_description FROM categories WHERE categoryID = ?";
        
        try (Connection conn = DbConfiguration.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setCategoryId(rs.getInt("categoryID"));
                    category.setCategoryName(rs.getString("category_name"));
                    
                    // FIXED: Map 'category_description' column to description field
                    category.setDescription(rs.getString("category_description"));
                    
                    return category;
                }
            }
        }
        
        return null;
    }
   

 
    @Override
	public int addCategory(Category category) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean updateCategory(Category category) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCategory(Integer categoryId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Category getCategoryById(Integer categoryId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    
  
}