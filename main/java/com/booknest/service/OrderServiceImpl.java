package com.booknest.service;

import com.booknest.config.DbConfiguration;
import com.booknest.model.BookModel;
import com.booknest.model.CartItem;
import com.booknest.model.OrderModel;
import com.booknest.model.OrderItemModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    // --- SQL Query Constants ---
    private static final String SQL_CREATE_ORDER = 
            "INSERT INTO orders (userID, order_date, shipping_address, subtotal, shipping_cost, discount, total_amount, order_status) " +
            "VALUES (?, NOW(), ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_CREATE_ORDER_ITEM = 
            "INSERT INTO order_item (orderID, bookID, quantity, unit_price) VALUES (?, ?, ?, ?)";
    
    private static final String SQL_GET_ORDER_BY_ID = 
            "SELECT * FROM orders WHERE orderID = ?";
    
    private static final String SQL_GET_ORDER_ITEMS = 
            "SELECT oi.*, b.book_title, b.book_img_url FROM order_item oi " +
            "JOIN book b ON oi.bookID = b.bookID WHERE oi.orderID = ?";
    
    private static final String SQL_UPDATE_ORDER_STATUS = 
            "UPDATE orders SET order_status = ? WHERE orderID = ?";
    
    private static final String SQL_GET_ORDERS_BY_USER_ID = 
            "SELECT * FROM orders WHERE userID = ? ORDER BY order_date DESC";
    
    private static final String SQL_CLEAR_CART = 
            "DELETE FROM cart_item WHERE userID = ?";
    
    public OrderServiceImpl() {
        new BookServiceImpl();
    }
    
    @Override
    public int createOrder(int userId, List<CartItem> cartItems, String shippingAddress, 
                           BigDecimal subtotal, BigDecimal shippingCost, BigDecimal discount, 
                           BigDecimal totalAmount) throws SQLException {
                           
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot create order with empty cart");
        }
        
        Connection conn = null;
        int generatedOrderId = -1;
        
        try {
            conn = DbConfiguration.getDbConnection();
            conn.setAutoCommit(false); // Start transaction
            
            try (PreparedStatement psOrder = conn.prepareStatement(SQL_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, userId);
                psOrder.setString(2, shippingAddress);
                psOrder.setBigDecimal(3, subtotal);
                psOrder.setBigDecimal(4, shippingCost);
                psOrder.setBigDecimal(5, discount);
                psOrder.setBigDecimal(6, totalAmount);
                psOrder.setString(7, "Pending"); // Default status
                
                int affectedRows = psOrder.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }
                
                try (ResultSet generatedKeys = psOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedOrderId = generatedKeys.getInt(1);
                        System.out.println("OrderServiceImpl: Created order with ID: " + generatedOrderId);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
            
            // Add order items
            try (PreparedStatement psItem = conn.prepareStatement(SQL_CREATE_ORDER_ITEM)) {
                for (CartItem item : cartItems) {
                    if (item.getBookModel() == null) {
                        throw new IllegalArgumentException("Cart item has null book model");
                    }
                    
                    psItem.setInt(1, generatedOrderId);
                    psItem.setInt(2, item.getBookModel().getBookID());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setBigDecimal(4, item.getBookModel().getPrice());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }
            
            // Clear the user's cart
            try (PreparedStatement psClearCart = conn.prepareStatement(SQL_CLEAR_CART)) {
                psClearCart.setInt(1, userId);
                psClearCart.executeUpdate();
            }
            
            // Commit the transaction
            conn.commit();
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database error creating order for user ID: " + userId);
            e.printStackTrace();
            
            // Attempt to roll back
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException("Database connection error", e);
            }
        } finally {
            // Reset auto-commit and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        
        return generatedOrderId;
    }

    @Override
    public OrderModel getOrderById(int orderId) throws SQLException {
        OrderModel order = null;
        
        try (Connection conn = DbConfiguration.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ORDER_BY_ID)) {
            
            ps.setInt(1, orderId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = mapResultSetToOrder(rs);
                    
                    // Also retrieve the order items
                    List<OrderItemModel> orderItems = getOrderItems(orderId);
                    order.setOrderItems(orderItems);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database error retrieving order ID: " + orderId);
            e.printStackTrace();
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException("Database connection error", e);
            }
        }
        
        return order;
    }

    @Override
    public List<OrderItemModel> getOrderItems(int orderId) throws SQLException {
        List<OrderItemModel> orderItems = new ArrayList<>();
        
        try (Connection conn = DbConfiguration.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ORDER_ITEMS)) {
            
            ps.setInt(1, orderId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItemModel item = new OrderItemModel();
                    
                    item.setOrderItemId(rs.getInt("id")); // Assuming the primary key column is named 'id'
                    item.setOrderId(rs.getInt("orderID"));
                    item.setBookId(rs.getInt("bookID"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    
                    // Create a minimal book model with available data
                    BookModel book = new BookModel();
                    book.setBookID(rs.getInt("bookID"));
                    book.setBookTitle(rs.getString("book_title"));
                    book.setBookImgUrl(rs.getString("book_img_url"));
                    
                    item.setBook(book);
                    orderItems.add(item);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database error retrieving order items for order ID: " + orderId);
            e.printStackTrace();
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException("Database connection error", e);
            }
        }
        
        return orderItems;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Order status cannot be empty");
        }
        
        int rowsUpdated = 0;
        
        try (Connection conn = DbConfiguration.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_ORDER_STATUS)) {
            
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            
            rowsUpdated = ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database error updating order status for ID: " + orderId);
            e.printStackTrace();
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException("Database connection error", e);
            }
        }
        
        return rowsUpdated > 0;
    }

    @Override
    public List<OrderModel> getOrdersByUserId(int userId) throws SQLException {
        List<OrderModel> orders = new ArrayList<>();
        
        try (Connection conn = DbConfiguration.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ORDERS_BY_USER_ID)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderModel order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database error retrieving orders for user ID: " + userId);
            e.printStackTrace();
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException("Database connection error", e);
            }
        }
        
        return orders;
    }
    
    // Helper method to map a ResultSet to an OrderModel
    private OrderModel mapResultSetToOrder(ResultSet rs) throws SQLException {
        OrderModel order = new OrderModel();
        
        order.setOrderID(rs.getInt("orderID"));
        order.setUserID(rs.getInt("userID"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setShippingCost(rs.getBigDecimal("shipping_cost"));
        order.setDiscount(rs.getBigDecimal("discount"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setOrderStatus(rs.getString("order_status"));
        
        return order;
    }
}