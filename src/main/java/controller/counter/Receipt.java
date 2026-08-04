/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.counter;

import entity.*;
import entity.Booking.BookingStatus;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import session.BookingFacade;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ling
 */
@WebServlet(name = "Receipt", urlPatterns = {"/counter/Receipt"})
public class Receipt extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @EJB private BookingFacade bookingFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");
        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("pay".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Booking booking = bookingFacade.find(id);

            if (booking == null) {
                request.setAttribute("error", "Booking not found.");
            } else if (booking.getBookingStatus() != BookingStatus.UNPAID) {
                request.setAttribute("error", "This booking is not unpaid.");
            } else {
                booking.setBookingStatus(BookingStatus.BOOKED);
                bookingFacade.edit(booking);
                request.setAttribute("paidBooking", booking);
                request.getRequestDispatcher("/counter/receipt.jsp").forward(request, response);
                return;
            }
        }

        List<Booking> unpaidBookings = bookingFacade.findByStatus(BookingStatus.UNPAID);
        request.setAttribute("unpaidBookings", unpaidBookings);
        request.getRequestDispatcher("/counter/receipt.jsp").forward(request, response);
    }
}