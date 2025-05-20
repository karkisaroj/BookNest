package com.booknest.service;

import com.booknest.model.BookCartModel;
import java.util.List;

/**
 * Service interface for book-related operations.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public interface BookService {

	/**
	 * Retrieves all books from the database.
	 * 
	 * @return A list of all books
	 * @throws ClassNotFoundException If the database driver class is not found
	 */
	List<BookCartModel> getAllBooks() throws ClassNotFoundException;

	/**
	 * Retrieves a book by its ID.
	 * 
	 * @param bookId The ID of the book to retrieve
	 * @return The book with the specified ID
	 * @throws Exception If the book is not found or a database error occurs
	 */
	BookCartModel getBookById(int bookId) throws Exception;

	/**
	 * Adds a new book to the database.
	 * 
	 * @param book The book to add
	 * @return The added book with its generated ID
	 * @throws Exception If the book data is invalid or a database error occurs
	 */
	BookCartModel addBook(BookCartModel book) throws Exception;

	/**
	 * Retrieves the most frequently added books to carts.
	 * 
	 * @param limit The maximum number of books to retrieve
	 * @return A list of the most popular books
	 * @throws Exception If a database error occurs
	 */
	List<BookCartModel> getTopAddedToCartBooks(int limit) throws Exception;

	/**
	 * Retrieves a random selection of books.
	 * 
	 * @param limit The maximum number of books to retrieve
	 * @return A list of random books
	 * @throws Exception If a database error occurs
	 */
	List<BookCartModel> getRandomBooks(int limit) throws Exception;

	/**
	 * Updates an existing book in the database.
	 * 
	 * @param book The book with updated information
	 * @return true if the update was successful, false otherwise
	 * @throws Exception If the book data is invalid or a database error occurs
	 */
	boolean updateBook(BookCartModel book) throws Exception;

	/**
	 * Deletes a book from the database.
	 * 
	 * @param bookId The ID of the book to delete
	 * @return true if the deletion was successful, false otherwise
	 * @throws Exception If the book ID is invalid or a database error occurs
	 */
	boolean deleteBook(int bookId) throws Exception;

	/**
	 * Searches for books with titles containing the given search term.
	 * 
	 * @param title The search term to look for in book titles
	 * @return A list of books matching the search criteria
	 * @throws Exception If a database error occurs
	 */
	List<BookCartModel> searchBooksByTitle(String title) throws Exception;

	/**
	 * Searches for books by category names.
	 * 
	 * @param categories Array of category names to search for
	 * @return A list of books matching the specified categories
	 * @throws Exception If a database error occurs
	 */
	List<BookCartModel> searchBooksByCategories(String[] categories) throws Exception;

	/**
	 * Searches for books matching both title and categories.
	 * 
	 * @param title      The title search term
	 * @param categories Array of category names to filter by
	 * @return A list of books matching both title and categories
	 * @throws Exception If a database error occurs
	 */
	List<BookCartModel> searchBooksByTitleAndCategories(String title, String[] categories) throws Exception;

	/**
	 * Retrieves books belonging to a specific category.
	 * 
	 * @param categoryId The ID of the category to filter by
	 * @return A list of books in the specified category
	 * @throws Exception If a database error occurs
	 */
	List<BookCartModel> getBooksByCategory(Integer categoryId) throws Exception;
}