package com.booknest.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderModel {
	private int orderID;
	private int userID;
	private Date orderDate;
	private String shippingAddress;
	private BigDecimal subtotal;
	private BigDecimal shippingCost;
	private BigDecimal discount;
	private BigDecimal totalAmount;
	private String orderStatus;
	private List<OrderItemModel> orderItems;

	// Constructor
	public OrderModel(int orderID, int userID, Date orderDate, String shippingAddress, BigDecimal totalAmount,
			String orderStatus) {
		this.orderID = orderID;
		this.userID = userID;
		this.orderDate = orderDate;
		this.shippingAddress = shippingAddress;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
	}

	// Default constructor
	public OrderModel() {
	}

	// Getters and Setters with both ID naming conventions for compatibility
	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getOrderId() {
		return orderID;
	}

	public void setOrderId(int orderID) {
		this.orderID = orderID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getUserId() {
		return userID;
	}

	public void setUserId(int userID) {
		this.userID = userID;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderItemModel> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemModel> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public String toString() {
		return "OrderModel [orderID=" + orderID + ", userID=" + userID + ", orderDate=" + orderDate + ", totalAmount="
				+ totalAmount + ", orderStatus=" + orderStatus + "]";
	}
}