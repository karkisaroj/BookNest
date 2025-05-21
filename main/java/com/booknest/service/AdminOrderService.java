package com.booknest.service;

import com.booknest.model.OrderModel;
import com.booknest.config.DbConfiguration;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing orders in the admin dashboard.
 * Handles database operations for retrieving, updating, and analyzing orders.
 * 
 * @author 23047591 Noble-Nepal
 */
public class AdminOrderService {

    
    // Status constants
    private final String orderStatusCompleted = "completed";
    
    // Database connection properties
    private boolean isConnectionError = false;
    private Connection dbConn;

    /**
     * Constructor initializes the database connection.
     * Sets the connection error flag if the connection fails.
     */
    public AdminOrderService() {
        try {
            dbConn = DbConfiguration.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            isConnectionError = true;
        }
    }

    /**
     * Gets all orders from the database.
     * 
     * @return List of OrderModel objects representing all orders
     */
    public List<OrderModel> getAllOrders() {
        List<OrderModel> orders = new ArrayList<>();
        if (isConnectionError) {
            return orders;
        }

        String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders`";

        try (PreparedStatement stmt = dbConn.prepareStatement(query); 
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                orders.add(createOrderFromResultSet(rs));
            }
        } catch (SQLException ex) {
            // Silent handling with empty list return
        }

        return orders;
    }

    /**
     * Gets an order by its ID.
     * 
     * @param orderID The ID of the order to retrieve
     * @return The OrderModel object if found, null otherwise
     */
    public OrderModel getOrderById(int orderID) {
        if (isConnectionError) {
            return null;
        }

        String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` WHERE orderID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createOrderFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            // Silent handling with null return
        }
        return null;
    }

    /**
     * Updates the status of an order.
     * 
     * @param orderID The ID of the order to update
     * @param status The new status to set
     * @return true if the update was successful, false otherwise
     */
    public boolean updateOrderStatus(int orderID, String status) {
        if (isConnectionError) {
            return false;
        }

        String query = "UPDATE `orders` SET order_status = ? WHERE orderID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Gets orders filtered by status.
     * 
     * @param status The status to filter by
     * @return List of OrderModel objects with the specified status
     */
    public List<OrderModel> getOrdersByStatus(String status) {
        List<OrderModel> orders = new ArrayList<>();
        if (isConnectionError) {
            return orders;
        }

        String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` WHERE order_status = ?";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(createOrderFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // Silent handling with empty list return
        }
        return orders;
    }

    /**
     * Gets orders for a specific user.
     * 
     * @param userId The ID of the user whose orders to retrieve
     * @return List of OrderModel objects belonging to the specified user
     */
    public List<OrderModel> getOrdersByUserId(int userId) {
        List<OrderModel> orders = new ArrayList<>();
        if (isConnectionError) {
            return orders;
        }

        String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` WHERE userID = ? ORDER BY order_date DESC";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(createOrderFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // Silent handling with empty list return
        }
        return orders;
    }

    /**
     * Gets the total number of orders in the system.
     * 
     * @return The count of all orders
     */
    public int getOrderCount() {
        if (isConnectionError) {
            return 0;
        }

        String query = "SELECT COUNT(*) as count FROM `orders`";
        try (PreparedStatement stmt = dbConn.prepareStatement(query); 
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException ex) {
            // Silent handling with zero return
        }
        return 0;
    }

    /**
     * Gets the most recent orders, limited to a specified count.
     * 
     * @param limit The maximum number of orders to retrieve
     * @return List of OrderModel objects representing the most recent orders
     */
    public List<OrderModel> getRecentOrders(int limit) {
        List<OrderModel> orders = new ArrayList<>();
        if (isConnectionError) {
            return orders;
        }

        String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` ORDER BY order_date DESC LIMIT ?";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(createOrderFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // Silent handling with empty list return
        }
        return orders;
    }

    /**
     * Calculates the total sales amount from completed orders.
     * 
     * @return The sum of total_amount for all completed orders
     */
    public BigDecimal getTotalSales() {
        if (isConnectionError) {
            return BigDecimal.ZERO;
        }

        String query = "SELECT SUM(total_amount) as total FROM `orders` WHERE order_status = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, orderStatusCompleted);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
        } catch (SQLException ex) {
            // Silent handling with zero return
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Helper method to create an OrderModel from a ResultSet row.
     * 
     * @param rs The ResultSet containing order data
     * @return A populated OrderModel object
     * @throws SQLException if a database access error occurs
     */
    private OrderModel createOrderFromResultSet(ResultSet rs) throws SQLException {
        OrderModel order = new OrderModel();
        order.setOrderID(rs.getInt("orderID"));
        order.setUserID(rs.getInt("userID"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setOrderStatus(rs.getString("order_status"));
        return order;
    }

    /**
     * Closes the database connection when the service is no longer needed.
     * Should be called explicitly when done with this service.
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