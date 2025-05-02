package com.booknest.model;

import java.math.BigDecimal;
import java.util.Date; // If using Date for publication_date

public class BookCartModel {
	private int bookID; // Or long if needed
	private String book_title;
	private String isbn;
	private Date publication_date; // Or String/LocalDate
	private BigDecimal price;
	private String description;
	private int stock_quantity;
	private String book_img_url;
	private int publisherID; // Or a PublisherModel object
	private String authorName; // Added field for author names

	public BookCartModel() {
	}

	// Getters and Setters for all fields...

	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public String getBook_title() {
		return book_title;
	}

	public void setBook_title(String book_title) {
		this.book_title = book_title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Date getPublication_date() {
		return publication_date;
	}

	public void setPublication_date(Date publication_date) {
		this.publication_date = publication_date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStock_quantity() {
		return stock_quantity;
	}

	public void setStock_quantity(int stock_quantity) {
		this.stock_quantity = stock_quantity;
	}

	public String getBook_img_url() {
	    return book_img_url;
	}

	public void setBook_img_url(String book_img_url) {
		this.book_img_url = book_img_url;
	}

	public int getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	// toString() method (optional but helpful for debugging)
	@Override
	public String toString() {
		return "BookModel{" + "bookID=" + bookID + ", book_title='" + book_title + '\'' + ", price=" + price
				+ ", authorName='" + authorName + '\'' +
				// ... other fields
				'}';
	}


}