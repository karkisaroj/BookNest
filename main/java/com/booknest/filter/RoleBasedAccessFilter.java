package com.booknest.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Noble Nepal 23047591
 * Role-based access filter to restrict Admins and Users from accessing
 * unauthorized pages.
 */
@WebFilter(urlPatterns = { "/home", "/admindashboard", "/login", "/productpage" })
public class RoleBasedAccessFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		if (session != null) {
			String roleName = (String) session.getAttribute("rolename");

			String requestURI = req.getRequestURI();

			if ("Admin".equals(roleName)) {
				if (requestURI.contains("/home") ||
//						requestURI.contains("/login") ||
						requestURI.contains("/register") || requestURI.contains("/productpage")
						|| requestURI.contains("/books") || requestURI.contains("/cart")
						|| requestURI.contains("/checkout") || requestURI.contains("/contactus")) {
					System.out.println(requestURI);
					resp.sendRedirect(req.getContextPath() + "/admindashboard");
					return;
				}
			} else if (!"Admin".equals(roleName)) {
				if (requestURI.contains("/admindashboard") || requestURI.contains("/register")
						|| requestURI.contains("/admincustomer") || requestURI.contains("/adminorder")
						|| requestURI.contains("/adminproduct")) {
					resp.sendRedirect(req.getContextPath() + "/home");
					return;
				}
			}
		} else {

			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}