
package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.booknest.config.DbConfiguration;
import com.booknest.model.UserModel;
import com.booknest.util.PasswordUtil;

public class LoginService {
    private boolean isConnectionError = false;
	private Connection dbConn;
    private static PasswordUtil passwordUtil=new PasswordUtil();
    /**
     * Constructor initializes the database connection. Sets the connection error
     * flag if the connection fails.
     */
    public LoginService() {
        try {
            dbConn = DbConfiguration.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) { 
            System.err.println("FATAL: Database connection failed in LoginService constructor: " + ex.getMessage());
            ex.printStackTrace(); 
            isConnectionError = true;
        }
    }

 
    /**
     * Validates the user credentials against the database records.
     *
     * @param userModel the UserModel object containing user credentials
     * @return true if the user credentials are valid, false otherwise; null if a
     *         connection error occurs
     */
    public Boolean loginUser(UserModel userModel) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }

        String query = "SELECT password FROM user WHERE user_name = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, userModel.getUserName());
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
  
                    String enpassword = result.getString("password").trim();
                    boolean isequal = passwordUtil.verifyPassword(userModel.getPassword(), enpassword);
                    return isequal;
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return false; // User not found in the database
    }
    /**
     * Retrieves the role ID for a given username.
     *
     * @param username the username to look up
     * @return the role ID as an integer, or 1 (default user role) if not found or error occurs
     */

    public UserModel getUserInfoWithRole(String userName) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }

        String query = "SELECT u.userID,u.first_name, u.last_name, u.user_name, u.password, u.email, u.phone_number, " +
                       "u.address, u.user_img_url, u.roleID, r.user_role " +
                       "FROM user u " +
                       "JOIN role r ON u.roleID = r.roleID " +
                       "WHERE u.user_name = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, userName);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    // Populate UserModel object
                    UserModel userModel = new UserModel(
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("user_name"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("phone_number"),
                        result.getString("address")
                    );

                    userModel.setId(result.getInt("userID"));
                    
                    userModel.setRoleId(result.getInt("roleID"));
                    
                    
                    String userRole = result.getString("user_role");
                     
                    userModel.setRoleName(userRole);
                    
                    return userModel;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    

}
    

    


