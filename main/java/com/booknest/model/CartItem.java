package com.booknest.model;

import java.sql.Timestamp;

public class CartItem {
	private int cartItemId;
	private int userId;
	private int bookId; // Foreign key to BookModel
	private int quantity;
	private Timestamp updatedAt;
	private BookModel bookModel; // Holds associated Book details

	public CartItem() {
	}

	// --- Getters and Setters ---
	public int getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public BookModel getBookModel() {
		return bookModel;
	}

	public void setBookModel(BookModel bookModel) {
		this.bookModel = bookModel;
	}
	// --- End Getters and Setters ---

	@Override
	public String toString() {
		return "CartItem{cartItemId=" + cartItemId + ", userId=" + userId + ", bookId=" + bookId + ", quantity="
				+ quantity + ", bookTitle=" + (bookModel != null ? bookModel.getBook_title() : "null") + '}';
	}
}