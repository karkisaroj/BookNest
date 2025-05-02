package com.booknest.service;

import com.booknest.model.CartItem;
import java.util.List;

public interface CartService {
	CartItem addItemToCart(int userId, int bookId, int quantityToAdd) throws CartServiceException;

	List<CartItem> getCartContents(int userId) throws CartServiceException;

	boolean updateCartItemQuantity(int userId, int cartItemId, int newQuantity) throws CartServiceException;

	boolean removeItemFromCart(int userId, int cartItemId) throws CartServiceException;
}