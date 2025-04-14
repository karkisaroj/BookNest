package com.booknest.util;

import java.util.regex.Pattern;
import jakarta.servlet.http.Part;

public class ValidationUtil {

	// 1. Validate if a field is null or empty
	public boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	// 2. Validate if a string contains only letters
	public boolean isAlphabetic(String value) {
		return value != null && value.matches("^[a-zA-Z]+$");
	}

	// 3. Validate if a string starts with a letter and is composed of letters and
	// numbers
	public boolean isAlphanumericStartingWithLetter(String value) {
		return value != null && value.matches("^[a-zA-Z][a-zA-Z0-9]*$");
	}

	// 4. Validate if a string is a valid email address
	public boolean isValidEmail(String email) {
		String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		return email != null && Pattern.matches(emailRegex, email);
	}

	// 5. Validate if a number is of 10 digits and starts with 98
	public boolean isValidPhoneNumber(String number) {
		return number != null && number.matches("^98\\d{8}$");
	}

	// 7. Validate if a password is composed of at least 1 capital letter, 1 number,
	// and 1 symbol
	public boolean isValidPassword(String password) {
		String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		return password != null && password.matches(passwordRegex);
	}

	// 8. Validate if a Part's file extension matches with image extensions (jpg,
	// jpeg, png, gif)
	public boolean isValidImageExtension(Part imagePart) {
		if (imagePart == null || isNullOrEmpty(imagePart.getSubmittedFileName())) {
			return false;
		}
		String fileName = imagePart.getSubmittedFileName().toLowerCase();
		return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
				|| fileName.endsWith(".gif");
	}

	// 9. Validate if password and retype password match
	public boolean doPasswordsMatch(String password, String retypePassword) {
		return password != null && password.equals(retypePassword);
	}
}