package com.booknest.util;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
	public static void setAttribute(HttpServletRequest req,String key,Object value) {
		
			HttpSession session=req.getSession();
			session.setAttribute(key, value);
			session.setMaxInactiveInterval(10*60);
		
		
	}
	
	public static Object getAttribute(HttpServletRequest req,String key) {
		HttpSession session=req.getSession(false);
		if(session!=null) {
			return session.getAttribute(key);
		}
		return key;
	}
	  /**
     * Invalidates the current session.
     *
     * @param request the HttpServletRequest from which the session is obtained
     */
    public static void invalidateSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    /**
     * Removes an attribute from the session.
     *
     * @param request the HttpServletRequest from which the session is obtained
     * @param key     the key of the attribute to remove
     */
    public static void removeAttribute(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(key);
        }
    }
	
}