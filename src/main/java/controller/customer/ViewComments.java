/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import entity.Message;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import session.MessageFacade;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ling
 */
@WebServlet(name = "ViewComments", urlPatterns = {"/customer/ViewComments"})
public class ViewComments extends HttpServlet {

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
    private MessageFacade messageFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }
        
        String success = request.getParameter("success");
        if (success != null) {
            request.setAttribute("success", success.replace("+", " "));
        }

        List<Message> comments = messageFacade.findCommentsByCustomer(user.getId());
        request.setAttribute("comments", comments);
        request.getRequestDispatcher("/customer/viewComments.jsp").forward(request, response);
    }
}