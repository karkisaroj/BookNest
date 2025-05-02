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

/**
 * @author Noble Nepal 23047591
 * Role-based access filter to restrict Admins and Users from accessing
 * unauthorized pages.
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
    
    // Common paths
    private static final String LOGIN = "/login";
    private static final String REGISTER = "/register";

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
        
        // Allow access to static resources
        if (uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".css") || 
            uri.endsWith(".js") || uri.endsWith(".ico")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Retrieve role name from session using SessionUtil
        String roleName = (String) SessionUtil.getAttribute(req, "rolename");
        
        if (roleName != null) {
            // Authenticated users
            if ("Admin".equals(roleName)) {
                // Admin is logged in
                if (uri.contains(HOME) || uri.contains(PRODUCTPAGE) || 
                    uri.contains(BOOKS) || uri.contains(CART) || 
                    uri.contains(CHECKOUT) || uri.contains(CONTACTUS)) {
                    resp.sendRedirect(contextPath + ADMIN_DASHBOARD);
                } else {
                    chain.doFilter(request, response);
                }
            } else {
                // User is logged in
                if (uri.contains(ADMIN_DASHBOARD) || uri.contains(ADMIN_CUSTOMER) || 
                    uri.contains(ADMIN_ORDER) || uri.contains(ADMIN_PRODUCT)) {
                    resp.sendRedirect(contextPath + HOME);
                } else {
                    chain.doFilter(request, response);
                }
            }
        } else {
            
            if (uri.contains(LOGIN) || uri.contains(REGISTER)) {
                
                chain.doFilter(request, response);
            } else {
            
                resp.sendRedirect(contextPath + LOGIN);
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic, if required
    }
}