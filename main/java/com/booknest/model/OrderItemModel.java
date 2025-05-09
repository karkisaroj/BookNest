package com.booknest.model;

import java.math.BigDecimal;

public class OrderItemModel {
	private int orderItemId;
	private int orderId;
	private int bookId;
	private int quantity;
	private BigDecimal unitPrice;
	private BookModel book; // For convenience to access book details

	// Constructor
	public OrderItemModel(int orderItemId, int orderId, int bookId, int quantity, BigDecimal unitPrice) {
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.bookId = bookId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	// Default constructor
	public OrderItemModel() {
	}

	// Getters and Setters
	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BookModel getBook() {
		return book;
	}

	public void setBook(BookModel book) {
		this.book = book;
	}

	public BigDecimal getSubtotal() {
		return unitPrice.multiply(new BigDecimal(quantity));
	}

	@Override
	public String toString() {
		return "OrderItemModel [orderItemId=" + orderItemId + ", orderId=" + orderId + ", bookId=" + bookId
				+ ", quantity=" + quantity + ", unitPrice=" + unitPrice + "]";
	}
}