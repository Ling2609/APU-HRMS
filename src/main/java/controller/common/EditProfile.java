/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

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
@WebServlet(name = "EditProfile", urlPatterns = {"/common/EditProfile"})
public class EditProfile extends HttpServlet {

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
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("updateProfile".equals(action)) {
            String name = request.getParameter("name").trim();
            String gender = request.getParameter("gender");
            String identification = request.getParameter("identification").trim();
            String phone = request.getParameter("phone").trim();
            String email = request.getParameter("email").trim();
            String address = request.getParameter("address").trim();

            // All fields required
            if (name.isEmpty() || identification.isEmpty() || phone.isEmpty() 
                    || email.isEmpty() || address.isEmpty()) {
                request.setAttribute("error", "All fields are required.");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }
            
            if (name.length() < 2) {
                request.setAttribute("error", "Name must be at least 2 characters.");
                request.getRequestDispatcher("/counter/registerCustomer.jsp").forward(request, response);
                return;
            }

            // IC must be 12 digits numbers only
            if (!identification.matches("\\d{12}")) {
                request.setAttribute("error", "IC must be exactly 12 digits (numbers only).");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }

            // Phone must be 10-11 digits
            if (!phone.matches("^[0-9]{10,11}$")) {
                request.setAttribute("error", "Phone must be 10-11 digits.");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }

            // Email format
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                request.setAttribute("error", "Invalid email format.");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }

            user.setName(name);
            user.setGender(gender);
            user.setIdentification(identification);
            user.setPhone(phone);
            user.setEmail(email);
            user.setAddress(address);
            userFacade.edit(user);
            session.setAttribute("user", user);

            request.setAttribute("success", "Profile updated successfully.");
            request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);

        } else if ("changePassword".equals(action)) {
            String currentPassword = request.getParameter("currentPassword").trim();
            String newPassword = request.getParameter("newPassword").trim();
            String confirmPassword = request.getParameter("confirmPassword").trim();

            if (!user.getPassword().equals(currentPassword)) {
                request.setAttribute("passwordError", "Current password is incorrect.");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }

            if (newPassword.isEmpty()) {
                request.setAttribute("passwordError", "New password cannot be empty.");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("passwordError", "New passwords do not match.");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }
            
            if (newPassword.length() < 6) {
                request.setAttribute("passwordError", "New password must be at least 6 characters.");
                request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
                return;
            }

            user.setPassword(newPassword);
            userFacade.edit(user);
            session.setAttribute("user", user);

            request.setAttribute("passwordSuccess", "Password changed successfully.");
            request.getRequestDispatcher("/common/editProfile.jsp").forward(request, response);
        }
    }
}