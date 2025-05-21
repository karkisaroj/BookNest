package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.booknest.config.DbConfiguration;
import com.booknest.util.PasswordUtil;

/**
 * Service class for account settings functionality.
 * Handles database operations for updating user information.
 * 
 * @author 23047591 Noble-Nepal
 */
public class AccountSettingService {

    
    // Database connection properties
    private boolean isConnectionError = false;
    private Connection dbConn;
    private PasswordUtil passwordUtil;

    /**
     * Constructor initializes the database connection and password utility.
     * Sets the connection error flag if the connection fails.
     */
    public AccountSettingService() {
        try {
            dbConn = DbConfiguration.getDbConnection();
            passwordUtil = new PasswordUtil();
        } catch (SQLException | ClassNotFoundException ex) {
            isConnectionError = true;
        }
    }

    /**
     * Updates personal information for a user.
     * 
     * @param userName    The username of the user
     * @param firstName   The new first name
     * @param lastName    The new last name
     * @param phoneNumber The new phone number
     * @return true if update was successful, false otherwise
     */
    public boolean updatePersonalInfo(String userName, String firstName, String lastName, String phoneNumber) {
        if (isConnectionError) {
            return false;
        }
        
        String query = "UPDATE user SET first_name = ?, last_name = ?, phone_number = ? WHERE user_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, userName);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Updates email address for a user.
     * 
     * @param userName  The username of the user
     * @param email     The new email address
     * @return true if update was successful, false otherwise
     */
    public boolean updateEmail(String userName, String email) {
        if (isConnectionError) {
            return false;
        }
        
        String query = "UPDATE user SET email = ? WHERE user_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, userName);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Updates password for a user with secure hashing using PBKDF2.
     * 
     * @param userName     The username of the user
     * @param newPassword  The new password (will be hashed before storing)
     * @return true if update was successful, false otherwise
     */
    public boolean updatePassword(String userName, String newPassword) {
        if (isConnectionError) {
            return false;
        }
        
        // Hash the password using PBKDF2WithHmacSHA256
        String hashedPassword = passwordUtil.hashPassword(newPassword);
        if (hashedPassword == null) {
            return false;
        }
        
        String query = "UPDATE user SET password = ? WHERE user_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, userName);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Checks if an email is already taken by another user.
     * 
     * @param email    The email to check
     * @param userName The current user's username (to exclude from check)
     * @return true if the email is already taken by another user, false otherwise
     */
    public boolean isEmailTaken(String email, String userName) {
        if (isConnectionError) {
            return false;
        }
        
        String query = "SELECT COUNT(*) FROM user WHERE email = ? AND user_name != ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, userName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Verify if the current password is correct for the user.
     * 
     * @param userName The username of the user
     * @param password The password to verify
     * @return true if password is correct, false otherwise
     */
    public boolean verifyCurrentPassword(String userName, String password) {
        if (isConnectionError) {
            return false;
        }
        
        String query = "SELECT password FROM user WHERE user_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, userName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    return passwordUtil.verifyPassword(password, storedHashedPassword);
                }
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Get user details by username.
     * 
     * @param userName The username of the user
     * @return Object array containing first name, last name, phone number, and email
     */
    public Object[] getUserDetails(String userName) {
        if (isConnectionError) {
            return null;
        }
        
        String query = "SELECT first_name, last_name, phone_number, email FROM user WHERE user_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, userName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Object[] details = new Object[4];
                    details[0] = rs.getString("first_name");
                    details[1] = rs.getString("last_name");
                    details[2] = rs.getString("phone_number");
                    details[3] = rs.getString("email");
                    return details;
                }
            }
            return null;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    /**
     * Closes the database connection.
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