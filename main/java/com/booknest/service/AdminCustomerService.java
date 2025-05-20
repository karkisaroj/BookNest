package com.booknest.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.booknest.config.DbConfiguration;
import com.booknest.model.UserModel;

/**
 * Service class for managing customer-related operations in the admin panel.
 * Handles database operations for retrieving and deleting customers.
 * 
 * @author Noble-Nepal 23047591
 * 
 */
public class AdminCustomerService {
    // Error messages as constants
    private final String connectionErrorMessage = "Database connection error";
    private final String userNotFoundMessage = "User not found";
    private final String foreignKeyErrorMessage = "Cannot delete customer. This customer has related records (orders,carts,etc).";
    private final String successMessage = "success";
    
    // Database connection properties
    private boolean isConnectionError = false;
    private Connection dbConn;
    
    /**
     * Constructor initializes the database connection.
     * Sets the connection error flag if the connection fails.
     * Uses the DbConfiguration utility to establish connection.
     */
    public AdminCustomerService() {
        try {
            dbConn = DbConfiguration.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }
    
    /**
     * Retrieves all users with the 'User' role from the database.
     * Executes a SQL query joining the user and role tables.
     * Creates UserModel objects from the query results.
     * 
     * @return List of UserModel objects representing customers, or null if a
     *         database error occurs
     */
    public List<UserModel> getAllCustomers() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }
        
        List<UserModel> customers = new ArrayList<>();
        
        String query = "SELECT u.userID, u.first_name, u.last_name, u.user_name, u.email, " +
                      "u.phone_number, u.address, u.user_img_url, u.roleID, r.user_role " +
                      "FROM user u " +
                      "JOIN role r ON u.roleID = r.roleID " +
                      "WHERE r.user_role = 'User'";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query);
             ResultSet result = stmt.executeQuery()) {
            
            while (result.next()) {
                UserModel user = new UserModel(
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("user_name"),
                    "", 
                    result.getString("email"),
                    result.getString("phone_number"),
                    result.getString("address"),
                    result.getInt("roleID"),
                    result.getString("user_role"),
                    result.getString("user_img_url")
                );
                
                user.setUserName(result.getInt("userID") + "");
                customers.add(user);
            }
            
            return customers;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes a user from the database by their ID.
     * Handles various error conditions including foreign key constraints.
     * Returns appropriate status messages based on the operation result.
     * 
     * @param userId The ID of the user to delete
     * @return String indicating the result of the deletion operation:
     *         "success" for successful deletion,
     *         "User not found" if the userId doesn't exist,
     *         Constraint violation message if the user has related records,
     *         Generic error message for other database errors
     */
    public String deleteUserById(int userId) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return connectionErrorMessage;
        }

        String query = "DELETE FROM user WHERE userID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return successMessage;
            } else {
                return userNotFoundMessage;
            }
        } catch (SQLException e) {
            if (e instanceof java.sql.SQLIntegrityConstraintViolationException) {
                return foreignKeyErrorMessage;
            }
            return "Database error occurred: " + e.getMessage();
        }
    }
}