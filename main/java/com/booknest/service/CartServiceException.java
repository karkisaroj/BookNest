package com.booknest.service;

/**
 * Custom exception for errors occurring specifically within the CartService layer.
 */
public class CartServiceException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CartServiceException(String message) {
        super(message);
    }

    public CartServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}