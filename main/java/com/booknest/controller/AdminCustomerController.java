package com.booknest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.booknest.model.UserModel;
import com.booknest.service.AdminCustomerService;
import com.booknest.util.RedirectionUtil;

/**
 * @author Noble Nepal 23047591
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admincustomer" })
public class AdminCustomerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminCustomerService customerService;
	private RedirectionUtil redirectionUtil;
	private final String customerpagepath = "/WEB-INF/pages/customer.jsp";
    @Override
    public void init() throws ServletException {
        customerService = new AdminCustomerService();
        this.redirectionUtil = new RedirectionUtil();
    }
       
    /**
     * Handles GET requests for the customer management page
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get all customers (users with User role)
        List<UserModel> customers = customerService.getAllCustomers();
        
        // Set the customers list as an attribute to be used in the JSP
        request.setAttribute("customers", customers);
        
        // Forward to the customer.jsp page
        request.getRequestDispatcher("/WEB-INF/pages/customer.jsp").forward(request, response);
    }
   

/**
 * Handles POST requests for deleting a customer.
 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdParam = request.getParameter("userId");

        if (userIdParam != null) {
            try {
                int userId = Integer.parseInt(userIdParam);
                boolean isDeleted = customerService.deleteUserById(userId);

                if (isDeleted) {
                    // Fetch the updated customer list after deletion
                    List<UserModel> customers = customerService.getAllCustomers();
                    request.setAttribute("customers", customers);
                    System.out.println("Success Message: " + request.getAttribute("success"));
                    System.out.println("Error Message: " + request.getAttribute("error"));
                    // Set success message and forward to JSP
                    redirectionUtil.setMsgAndRedirect(request, response, customerpagepath, "success", "User deleted successfully!");
                } else {
                    // Set error message and forward to JSP
                    List<UserModel> customers = customerService.getAllCustomers();
                    request.setAttribute("customers", customers);

                    redirectionUtil.setMsgAndRedirect(request, response, customerpagepath, "error", "Failed to delete user.");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle invalid user ID and fetch updated customer list
                List<UserModel> customers = customerService.getAllCustomers();
                request.setAttribute("customers", customers);

                redirectionUtil.setMsgAndRedirect(request, response, customerpagepath, "error", "Invalid user ID.");
            }
        } else {
            // Handle missing user ID and fetch updated customer list
            List<UserModel> customers = customerService.getAllCustomers();
            request.setAttribute("customers", customers);

            redirectionUtil.setMsgAndRedirect(request, response, customerpagepath, "error", "User ID not provided.");
        }
    }
}