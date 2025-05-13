package com.booknest.model;

import java.math.BigDecimal;
import java.sql.Date;

public class BookModel {
	private int bookID; // Primary key
	private String bookTitle;
	private String isbn;
	private Date publicationDate;
	private BigDecimal price;
	private String description;
	private int stockQuantity;
	private int pageCount;
	private String bookImgUrl;
	private int authorID;
	private int publisherID;
	private int categoryID;
	private int soldCount;

	// Constructors
	public BookModel() {
	}

	public BookModel(int bookID, String bookTitle, String isbn, Date publicationDate, BigDecimal price,
			String description, int stockQuantity, int pageCount, String bookImgUrl, int publisherID) {
		this.bookID = bookID;
		this.bookTitle = bookTitle;
		this.isbn = isbn;
		this.publicationDate = publicationDate;
		this.price = price;
		this.description = description;
		this.stockQuantity = stockQuantity;
		this.pageCount = pageCount;
		this.bookImgUrl = bookImgUrl;
		this.publisherID = publisherID;
	}

	public BookModel(int bookID, String bookTitle, String bookImgUrl, int soldCount) {
	    this.bookID = bookID;
	    this.bookTitle = bookTitle;
	    this.bookImgUrl = bookImgUrl;
	    this.soldCount = soldCount;
	}

	// Getters and Setters
	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
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

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getBookImgUrl() {
		return bookImgUrl;
	}

	public void setBookImgUrl(String bookImgUrl) {
		this.bookImgUrl = bookImgUrl;
	}

	public int getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}

	public int getSoldCount() {
		return soldCount;
	}

	public void setSoldCount(int soldCount) {
		this.soldCount = soldCount;
	}
	public int getAuthorID() {
	    return authorID;
	}

	public void setAuthorID(int authorID) {
	    this.authorID = authorID;
	}
	public int getCategoryID() {
	    return categoryID;
	}

	public void setCategoryID(int categoryID) {
	    this.categoryID = categoryID;
	}
}