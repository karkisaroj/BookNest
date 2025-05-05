package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CategoryService interface
 */
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<Category> getAllCategories() throws Exception {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConfiguration.getDbConnection();
            stmt = conn.createStatement();

            // Get all columns needed but handle if description doesn't exist
            String sql = "SELECT categoryID, category_name, " +
                         "(SELECT column_name FROM information_schema.columns " +
                         "WHERE table_name='categories' AND column_name='description') AS has_desc_col, " +
                         "IFNULL(description, '') as description " +
                         "FROM categories ORDER BY category_name";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("category_name"));
                
                // Set description safely - either from column or empty string
                category.setDescription(rs.getString("description"));
                
   
                categories.add(category);
            }

            System.out.println("Found " + categories.size() + " categories from database");
        } catch (Exception e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
            throw e;
        } finally {
            closeResources(rs, stmt, conn);
        }

        return categories;
    }

    @Override
    public Category getCategoryById(Integer categoryId) throws Exception {
        Category category = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConfiguration.getDbConnection();

            // Get only the columns that are guaranteed to exist
            String sql = "SELECT c.categoryID, c.category_name, " +
                         "IFNULL(c.description, '') as description " +
                         "FROM categories c WHERE c.categoryID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                category = new Category();
                category.setCategoryId(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
  
            }
        } catch (Exception e) {
            System.err.println("Error retrieving category by ID: " + e.getMessage());
            throw e;
        } finally {
            closeResources(rs, stmt, conn);
        }

        return category;
    }
   

 
    private boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        ResultSet rs = null;
        try {
            rs = conn.getMetaData().getColumns(null, null, tableName, columnName);
            return rs.next();
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    /**
     * Helper method to close database resources
     */
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
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
    
  
}