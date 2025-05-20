package com.booknest.service;

/**
 * Custom exception for handling cart-related errors in the service layer. This
 * exception provides specific error handling for operations related to the
 * shopping cart functionality.
 * 
 * @author Saroj Pratap Karki 23047612
 */
public class CartServiceException extends Exception {

	/**
	 * Unique identifier for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new cart service exception with the specified detail message.
	 * 
	 * @param message The detail message explaining the error condition
	 */
	public CartServiceException(String message) {
		super(message);
	}

	/**
	 * Constructs a new cart service exception with the specified detail message and
	 * cause.
	 * 
	 * @param message The detail message explaining the error condition
	 * @param cause   The underlying cause of the exception
	 */
	public CartServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}