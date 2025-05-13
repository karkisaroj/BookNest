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

import com.booknest.util.SessionUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Noble Nepal 23047591 Role-based access filter to restrict Admins and
 *         Users from accessing unauthorized pages.
 */
@WebFilter(urlPatterns = { "/*" })
public class RoleBasedAccessFilter implements Filter {

	// User paths
	private static final String HOME = "/home";
	private static final String PRODUCTPAGE = "/productpage";
	private static final String BOOKS = "/books";
	private static final String CART = "/cart";
	private static final String CHECKOUT = "/checkout";
	private static final String CONTACTUS = "/contactus";

	// Admin paths
	private static final String ADMIN_DASHBOARD = "/admindashboard";
	private static final String ADMIN_CUSTOMER = "/admincustomer";
	private static final String ADMIN_ORDER = "/adminorder";
	private static final String ADMIN_PRODUCT = "/adminproduct";
	private static final String ADMIN_UPDATE_BOOK = "/adminupdatebook";
	// Common paths
	private static final String LOGIN = "/login";
	private static final String REGISTER = "/register";

	// Public resources - pages that don't require login
	private static final List<String> PUBLIC_RESOURCES = Arrays.asList(HOME, "/", BOOKS, CONTACTUS, PRODUCTPAGE,
			"/resources", "/css", "/js", "/images");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization logic, if required
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		String contextPath = req.getContextPath();
		String path = uri.substring(contextPath.length());

		// Allow access to static resources
		if (uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".css") || uri.endsWith(".js")
				|| uri.endsWith(".ico")) {
			chain.doFilter(request, response);
			return;
		}

		// Check if the requested resource is public
		boolean isPublicResource = PUBLIC_RESOURCES.stream()
				.anyMatch(publicPath -> path.equals(publicPath) || path.startsWith(publicPath));

		// Retrieve role name from session using SessionUtil
		String roleName = (String) SessionUtil.getAttribute(req, "rolename");

		if (roleName != null) {
			// Authenticated users
			if ("Admin".equals(roleName)) {
				// Admin is logged in
				if (uri.contains(HOME) || uri.contains(PRODUCTPAGE) || uri.contains(BOOKS) || uri.contains(CART)
						|| uri.contains(LOGIN) || uri.contains(REGISTER) || uri.contains(CHECKOUT)
						|| uri.contains(CONTACTUS)) {
					resp.sendRedirect(contextPath + ADMIN_DASHBOARD);
				} else {
					chain.doFilter(request, response);
				}
			} else {
				// User is logged in
				if (uri.contains(ADMIN_DASHBOARD) || uri.contains(ADMIN_CUSTOMER) || uri.contains(ADMIN_ORDER)
						|| uri.contains(ADMIN_PRODUCT)|| uri.contains(ADMIN_UPDATE_BOOK)|| uri.contains(LOGIN) || uri.contains(REGISTER)) {
					resp.sendRedirect(contextPath + HOME);
				} else {
					chain.doFilter(request, response);
				}
			}
		} else {
			// Unauthenticated users
			if (uri.contains(LOGIN) || uri.contains(REGISTER) || isPublicResource) {
				// Allow access to login, register, and public resources
				chain.doFilter(request, response);
			} else {
				// Redirect to login for protected resources
				resp.sendRedirect(contextPath + LOGIN);
			}
		}
	}

	@Override
	public void destroy() {
		// Cleanup logic, if required
	}
}