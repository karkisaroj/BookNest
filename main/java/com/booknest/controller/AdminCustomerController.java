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
 * Controller servlet for managing customers in the admin panel. Handles
 * customer listing and deletion operations.
 * 
 * @author Noble Nepal 23047591
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admincustomer" })
public class AdminCustomerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Constant error messages
	private final String invalidUserIdMessage = "Invalid user ID.";
	private final String missingUserIdMessage = "User ID not provided.";
	private final String deleteSuccessMessage = "User deleted successfully!";

	// Path constants
	private final String customerPagePath = "/WEB-INF/pages/customer.jsp";

	private AdminCustomerService customerService;
	private RedirectionUtil redirectionUtil;

	/**
	 * Initializes the servlet by creating service instances. This method is called
	 * once when the servlet is first loaded.
	 * 
	 * @throws ServletException if a servlet-specific error occurs during
	 *                          initialization
	 */
	@Override
	public void init() throws ServletException {
		customerService = new AdminCustomerService();
		this.redirectionUtil = new RedirectionUtil();
	}

	/**
	 * Handles GET requests for the customer management page. Retrieves all
	 * customers with 'User' role and forwards to the customer JSP page.
	 * 
	 * @param request  The HttpServletRequest object containing client request
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during response handling
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<UserModel> customers = customerService.getAllCustomers();

		// Set the customers list as an attribute to be used in the JSP
		request.setAttribute("customers", customers);

		// Forward to the customer.jsp page
		request.getRequestDispatcher(customerPagePath).forward(request, response);
	}

	/**
	 * Handles POST requests for deleting a customer. Processes the userId
	 * parameter, performs deletion operation, and redirects with appropriate
	 * success or error messages.
	 * 
	 * @param request  The HttpServletRequest object containing client request and
	 *                 deletion parameters
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during response handling
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIdParam = request.getParameter("userId");

		if (userIdParam != null) {
			try {
				int userId = Integer.parseInt(userIdParam);
				String deleteResult = customerService.deleteUserById(userId);

				// Fetch the updated customer list after attempted deletion
				List<UserModel> customers = customerService.getAllCustomers();
				request.setAttribute("customers", customers);

				if (deleteResult.equals("success")) {
					
					redirectionUtil.setMsgAndRedirect(request, response, customerPagePath, "success",
							deleteSuccessMessage);
				} else {
					
					redirectionUtil.setMsgAndRedirect(request, response, customerPagePath, "error", deleteResult);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				
				List<UserModel> customers = customerService.getAllCustomers();
				request.setAttribute("customers", customers);

				redirectionUtil.setMsgAndRedirect(request, response, customerPagePath, "error", invalidUserIdMessage);
			}
		} else {
			
			List<UserModel> customers = customerService.getAllCustomers();
			request.setAttribute("customers", customers);

			redirectionUtil.setMsgAndRedirect(request, response, customerPagePath, "error", missingUserIdMessage);
		}
	}
}