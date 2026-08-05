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
import session.*;
import java.io.IOException;
/**
 *
 * @author Ling
 */
@WebServlet(name = "CancelBooking", urlPatterns = {"/counter/CancelBooking"})
public class CancelBooking extends HttpServlet {

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
    @EJB private BookingUserFacade bookingUserFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");
        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        Booking booking = bookingFacade.find(id);

        if (booking == null) {
            response.sendRedirect(request.getContextPath() + 
                "/counter/ViewBookings?error=Booking+not+found.");
            return;
        }

        if (booking.getBookingStatus() != BookingStatus.UNPAID) {
            response.sendRedirect(request.getContextPath() + 
                "/counter/ViewBookings?error=Only+UNPAID+bookings+can+be+cancelled.");
            return;
        }

        // Delete BookingUser first (foreign key constraint)
        BookingUser bu = bookingUserFacade.findByBookingAndRole(
            booking.getId(), BookingUser.BookingUserRole.CUSTOMER);
        if (bu != null) {
            bookingUserFacade.remove(bu);
        }

        // Delete booking
        bookingFacade.remove(booking);

        response.sendRedirect(request.getContextPath() + 
            "/counter/ViewBookings?success=Booking+cancelled+successfully.");
    }
}