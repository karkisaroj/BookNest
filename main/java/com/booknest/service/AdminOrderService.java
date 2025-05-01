package com.booknest.service;

import com.booknest.model.OrderModel;
import com.booknest.config.DbConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderService {

    private Connection dbConn;
    private boolean isConnectionError = false;

    public AdminOrderService() {
        try {
            dbConn = DbConfiguration.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }

    public List<OrderModel> getAllOrders() {
        List<OrderModel> orders = new ArrayList<>();
        if (isConnectionError) {
            System.err.println("Database connection error. Unable to fetch orders.");
            return orders;
        }

        String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders`";

        try (PreparedStatement stmt = dbConn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setOrderID(rs.getInt("orderID"));
                order.setUserID(rs.getInt("userID"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setOrderStatus(rs.getString("order_status"));

                orders.add(order);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return orders;
    }

    public OrderModel getOrderById(int orderID) {
        if (isConnectionError) {
            System.err.println("Database connection error. Unable to fetch order.");
            return null;
        }

        String query = "SELECT orderID, userID, order_date, shipping_address, total_amount, order_status FROM `orders` WHERE orderID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                OrderModel order = new OrderModel();
                order.setOrderID(rs.getInt("orderID"));
                order.setUserID(rs.getInt("userID"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setOrderStatus(rs.getString("order_status"));

                return order;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void updateOrderStatus(int orderID, String status) {
        if (isConnectionError) {
            System.err.println("Database connection error. Unable to update order status.");
            return;
        }

        String query = "UPDATE `orders` SET order_status = ? WHERE orderID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    
}