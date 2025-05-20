package com.booknest.service;

import java.util.List;
import com.booknest.model.CartItem;

public interface CartService {

	/**
	 * @author Saroj Pratap Karki 23047612
* */
	
	/**
	 * Get all items in a user's cart
	 * 
	 * @param userId The ID of the user
	 * @return List of CartItem objects representing items in the cart
	 * @throws CartServiceException If there's an error retrieving cart contents
	 */
	List<CartItem> getCartContents(int userId) throws CartServiceException;

	/**
	 * Add an item to a user's cart
	 * 
	 * @param userId   The ID of the user
	 * @param bookId   The ID of the book to add
	 * @param quantity The quantity to add
	 * @return The CartItem that was added
	 * @throws CartServiceException If there's an error adding the item
	 */
	CartItem addItemToCart(int userId, int bookId, int quantity) throws CartServiceException;

	/**
	 * Update the quantity of an item in the cart
	 * 
	 * @param userId      The ID of the user
	 * @param cartItemId  The ID of the cart item
	 * @param newQuantity The new quantity
	 * @return true if successful, false otherwise
	 * @throws CartServiceException If there's an error updating the item
	 */
	boolean updateCartItemQuantity(int userId, int cartItemId, int newQuantity) throws CartServiceException;

	/**
	 * Remove an item from the cart
	 * 
	 * @param userId     The ID of the user
	 * @param cartItemId The ID of the cart item to remove
	 * @return true if successful, false otherwise
	 * @throws CartServiceException If there's an error removing the item
	 */
	boolean removeItemFromCart(int userId, int cartItemId) throws CartServiceException;

	/**
	 * Clear all items from a user's cart
	 * 
	 * @param userId The ID of the user whose cart to clear
	 * @throws CartServiceException If there's an error clearing the cart
	 */
	void clearCart(Integer userId) throws CartServiceException;
}