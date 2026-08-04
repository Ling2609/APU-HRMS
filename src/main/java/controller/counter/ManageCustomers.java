/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.counter;

import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import session.UserFacade;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ling
 */
@WebServlet(name = "ManageCustomers", urlPatterns = {"/counter/ManageCustomers"})
public class ManageCustomers extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    private UserFacade userFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");
        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String success = request.getParameter("success");
        if (success != null) {
            request.setAttribute("success", success.replace("+", " "));
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            User customer = userFacade.find(id);
            if (customer != null && customer.getRole() == User.Role.CUSTOMER) {
                userFacade.remove(customer);
                request.setAttribute("success", "Customer deleted successfully.");
            }
            List<User> customers = userFacade.findAllCustomers();
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("/counter/manageCustomers.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            User customer = userFacade.find(id);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/counter/editCustomer.jsp").forward(request, response);
            return;
        }

        String keyword = request.getParameter("keyword");
        List<User> customers;
        if (keyword != null && !keyword.trim().isEmpty()) {
            customers = userFacade.searchCustomers(keyword.trim());
        } else {
            customers = userFacade.findAllCustomers();
        }
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/counter/manageCustomers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");
        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("update".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            User customer = userFacade.find(id);

            String name = request.getParameter("name").trim();
            String gender = request.getParameter("gender");
            String identification = request.getParameter("identification").trim();
            String phone = request.getParameter("phone").trim();
            String email = request.getParameter("email").trim();
            String address = request.getParameter("address").trim();

            // IC must be 12 digits
            if (!identification.matches("\\d{12}")) {
                request.setAttribute("error", "IC must be exactly 12 digits.");
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/counter/editCustomer.jsp").forward(request, response);
                return;
            }

            // Phone validation
            if (!phone.matches("^[0-9]{10,11}$")) {
                request.setAttribute("error", "Phone must be 10-11 digits.");
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/counter/editCustomer.jsp").forward(request, response);
                return;
            }

            // Email format
            if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                request.setAttribute("error", "Invalid email format.");
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/counter/editCustomer.jsp").forward(request, response);
                return;
            }

            // Check duplicate IC
            User existing = userFacade.findByIdentification(identification);
            if (existing != null && !existing.getId().equals(id)) {
                request.setAttribute("error", "Another customer already has this IC.");
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/counter/editCustomer.jsp").forward(request, response);
                return;
            }

            customer.setName(name);
            customer.setGender(gender);
            customer.setIdentification(identification);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);
            userFacade.edit(customer);

            response.sendRedirect(request.getContextPath() +
                "/counter/ManageCustomers?success=Customer+" + name + "+updated+successfully.");
        }
    }
}