package com.booknest.service;

import com.booknest.model.BookCartModel;
import java.util.List;

public interface BookService {

	List<BookCartModel> getAllBooks() throws ClassNotFoundException;

	BookCartModel getBookById(int bookId) throws Exception;

	BookCartModel addBook(BookCartModel book) throws Exception;

	List<BookCartModel> getTopAddedToCartBooks(int limit) throws Exception;

	List<BookCartModel> getRandomBooks(int limit) throws Exception;

	boolean updateBook(BookCartModel book) throws Exception;

	boolean deleteBook(int bookId) throws Exception;

	List<BookCartModel> searchBooksByTitle(String title) throws Exception;

	// Add these two new methods
	List<BookCartModel> searchBooksByCategories(String[] categories) throws Exception;

	List<BookCartModel> searchBooksByTitleAndCategories(String title, String[] categories) throws Exception;
	
	List<BookCartModel> getBooksByCategory(Integer categoryId) throws Exception;
}