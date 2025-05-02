package com.booknest.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    // Default session timeout in seconds (e.g., 30 minutes)
    // Adjust as needed, or configure globally in web.xml
    private static final int DEFAULT_SESSION_TIMEOUT = 30 * 60;

    /**
     * Sets an attribute in the session with a default timeout.
     * Creates a session if one doesn't exist.
     *
     * @param req   the HttpServletRequest
     * @param key   the attribute key
     * @param value the attribute value
     */
    public static void setAttribute(HttpServletRequest req, String key, Object value) {
        HttpSession session = req.getSession(true); // Get or create session
        session.setAttribute(key, value);
        // Consider setting timeout globally (web.xml) or only when session is created
        // Setting it every time might reset the timer unexpectedly.
        // Only set if newly created or if specific override needed:
        // if (session.isNew()) {
             session.setMaxInactiveInterval(DEFAULT_SESSION_TIMEOUT);
        // }
        // Or remove setMaxInactiveInterval if handled globally.
    }

    /**
     * Gets an attribute from the session, returning null if not found.
     * Does not create a session if one doesn't exist.
     * Provides type safety via casting.
     *
     * @param req   the HttpServletRequest
     * @param key   the attribute key
     * @param type  the expected Class type of the attribute
     * @return the attribute value cast to the specified type, or null if not found or type mismatch.
     */
    public static <T> T getAttribute(HttpServletRequest req, String key, Class<T> type) {
        HttpSession session = req.getSession(false); // Don't create session just to get attribute
        if (session != null) {
            Object value = session.getAttribute(key);
            if (value != null && type.isInstance(value)) {
                return type.cast(value); // Safely cast to the expected type
            }
        }
        return null; // Return null if session is null, attribute is null, or type doesn't match
    }

     /**
     * Gets an attribute from the session as an Object, returning null if not found.
     * Convenience method without type casting.
     *
     * @param req   the HttpServletRequest
     * @param key   the attribute key
     * @return the attribute value as Object, or null if not found.
     */
    public static Object getAttribute(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return session.getAttribute(key);
        }
        return null; // Return null if session doesn't exist or attribute not found
    }


    /**
     * Removes an attribute from the session.
     *
     * @param req the HttpServletRequest
     * @param key the key of the attribute to remove
     */
    public static void removeAttribute(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    /**
     * Invalidates the current session.
     *
     * @param req the HttpServletRequest
     */
    public static void invalidateSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

     /**
     * Checks if a user session exists and contains a specific attribute (e.g., username).
     * Useful for checking login status without retrieving the actual value.
     *
     * @param req the HttpServletRequest
     * @param userAttributeKey The session key that indicates a logged-in user (e.g., "userName")
     * @return true if the session exists and the user attribute is present, false otherwise.
     */
    public static boolean isLoggedIn(HttpServletRequest req, String userAttributeKey) {
        HttpSession session = req.getSession(false);
        return (session != null && session.getAttribute(userAttributeKey) != null);
    }
}