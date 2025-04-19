
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
            stmt.setString(1, userModel.getUser_name());
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


    
}

