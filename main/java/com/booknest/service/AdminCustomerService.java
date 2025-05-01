package com.booknest.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.booknest.config.DbConfiguration;
import com.booknest.model.UserModel;
public class AdminCustomerService {
    private boolean isConnectionError = false;
    private Connection dbConn;
    
    /**
     * Constructor initializes the database connection. Sets the connection error
     * flag if the connection fails.
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
     * Gets all users with the 'User' role from the database.
     * 
     * @return List of UserModel objects representing users, or null if error occurs
     */
    public List<UserModel> getAllCustomers() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }
        
        List<UserModel> customers = new ArrayList<>();
        
        // Query to get all users with role=User
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
                
                // Store userID as a request attribute since it's not in the model
                user.setUser_name(result.getInt("userID") + ""); // Temporarily store ID in username for display
                
                customers.add(user);
            }
            
            return customers;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @return true if the user was deleted successfully, false otherwise.
     */
    public boolean deleteUserById(int userId) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return false;
        }

        String query = "DELETE FROM user WHERE userID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
