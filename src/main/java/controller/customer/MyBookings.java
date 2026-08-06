/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import entity.Booking;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import session.BookingFacade;
import session.MessageFacade;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author Ling
 */
@WebServlet(name = "MyBookings", urlPatterns = {"/customer/MyBookings"})
public class MyBookings extends HttpServlet {

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
    private BookingFacade bookingFacade;
    
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

        List<Booking> bookings = bookingFacade.findByCustomer(user.getId());
        
        Set<Long> commentedBookingIds = new HashSet<>(messageFacade.findCommentedBookingIdsByCustomer(user.getId()));
        
        request.setAttribute("bookings", bookings);
        request.setAttribute("commentedBookingIds", commentedBookingIds);
        request.getRequestDispatcher("/customer/myBookings.jsp").forward(request, response);
    }
}