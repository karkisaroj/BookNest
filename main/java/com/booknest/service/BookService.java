package com.booknest.service;

import com.booknest.model.BookModel;
import java.util.List;

// Throws specific BookServiceException
public interface BookService {

	/** Retrieves all books. */
	List<BookModel> getAllBooks() throws Exception;

	
	/** Finds a book by ID. */
	BookModel getBookById(int bookId) throws Exception;

	/** Adds a new book (Admin operation). */
	BookModel addBook(BookModel book) throws Exception;

	/** Updates an existing book (Admin operation). */
	boolean updateBook(BookModel book) throws Exception;

	/** Deletes a book by ID (Admin operation). */
	boolean deleteBook(int bookId) throws Exception;
	
	 List<BookModel> getTopAddedToCartBooks(int limit) throws Exception;
	 
	 List<BookModel> getRandomBooks(int limit) throws Exception;
	
}