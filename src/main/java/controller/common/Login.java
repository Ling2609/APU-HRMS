/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.*;
import session.UserFacade;

/**
 *
 * @author Ling
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

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

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        User user = userFacade.findByNameAndPassword(name, password);

        if (user == null) {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/common/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        switch (user.getRole()) {
            case MANAGER:
                response.sendRedirect(request.getContextPath() + "/manager/home.jsp");
                break;
            case COUNTER_STAFF:
                response.sendRedirect(request.getContextPath() + "/counter/home.jsp");
                break;
            case HOUSEKEEPER:
                response.sendRedirect(request.getContextPath() + "/housekeeper/home.jsp");
                break;
            case CUSTOMER:
                response.sendRedirect(request.getContextPath() + "/customer/home.jsp");
                break;
        }
    }
}
