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

/**
 *
 * @author Ling
 */
@WebServlet(name = "RegisterCustomer", urlPatterns = {"/counter/RegisterCustomer"})
public class RegisterCustomer extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");
        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String name = request.getParameter("name").trim();
        String password = request.getParameter("password").trim();
        String gender = request.getParameter("gender");
        String identification = request.getParameter("identification").trim();
        String phone = request.getParameter("phone").trim();
        String email = request.getParameter("email").trim();
        String address = request.getParameter("address").trim();

        // Validation
        if (name.isEmpty() || password.isEmpty() || identification.isEmpty()) {
            request.setAttribute("error", "Name, password and IC are required.");
            request.getRequestDispatcher("/counter/registerCustomer.jsp").forward(request, response);
            return;
        }

        // IC must be 12 digits
        if (!identification.matches("\\d{12}")) {
            request.setAttribute("error", "IC must be exactly 12 digits.");
            request.getRequestDispatcher("/counter/registerCustomer.jsp").forward(request, response);
            return;
        }

        // Phone validation
        if (!phone.matches("^[0-9]{10,11}$")) {
            request.setAttribute("error", "Phone must be 10-11 digits.");
            request.getRequestDispatcher("/counter/registerCustomer.jsp").forward(request, response);
            return;
        }

        // Email format
        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            request.setAttribute("error", "Invalid email format.");
            request.getRequestDispatcher("/counter/registerCustomer.jsp").forward(request, response);
            return;
        }

        // Check duplicate IC
        User existing = userFacade.findByIdentification(identification);
        if (existing != null) {
            request.setAttribute("error", "A customer with this IC already exists.");
            request.getRequestDispatcher("/counter/registerCustomer.jsp").forward(request, response);
            return;
        }

        User customer = new User(name, password, gender, identification,
                phone, email, address, User.Role.CUSTOMER, null);
        userFacade.create(customer);
        response.sendRedirect(request.getContextPath() +
                "/counter/ManageCustomers?success=Customer+" + name + "+registered+successfully.");
    }
}