package com.booknest.service;

import com.booknest.model.BookCartModel;
import java.util.List;

// Throws specific BookServiceException
public interface BookService {

	/** Retrieves all books. */
	List<BookCartModel> getAllBooks() throws Exception;

	
	/** Finds a book by ID. */
	BookCartModel getBookById(int bookId) throws Exception;

	/** Adds a new book (Admin operation). */
	BookCartModel addBook(BookCartModel book) throws Exception;

	/** Updates an existing book (Admin operation). */
	boolean updateBook(BookCartModel book) throws Exception;

	/** Deletes a book by ID (Admin operation). */
	boolean deleteBook(int bookId) throws Exception;
	
	 List<BookCartModel> getTopAddedToCartBooks(int limit) throws Exception;
	 
	 List<BookCartModel> getRandomBooks(int limit) throws Exception;
	
}