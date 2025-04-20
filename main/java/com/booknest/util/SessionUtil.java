package com.booknest.util;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
	public static void setAttribute(HttpServletRequest req,String key,Object value) {
		
			HttpSession session=req.getSession();
			session.setAttribute(key, value);
			session.setMaxInactiveInterval(10);
		
		
	}
	
	public static Object getAttribute(HttpServletRequest req,String key) {
		HttpSession session=req.getSession(false);
		if(session!=null) {
			return session.getAttribute(key);
		}
		return key;
	}
}
