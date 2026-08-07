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
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");

        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String idParameter = request.getParameter("id");

        //Process the payment only when action=pay.
        if ("pay".equals(action)) {
            try {
                Long bookingId = Long.valueOf(idParameter);
                Booking booking = bookingFacade.find(bookingId);

                if (booking == null) {
                    request.setAttribute("error","Booking not found.");
                } else if (booking.getBookingStatus()!= BookingStatus.UNPAID) {
                    request.setAttribute("error","This booking is no longer awaiting payment.");
                } else {
                    booking.setBookingStatus(BookingStatus.BOOKED);
                    bookingFacade.edit(booking);

                    request.setAttribute("paidBooking", booking);
                    request.getRequestDispatcher("/counter/receipt.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error","Invalid booking ID.");
            }
        }

        //When an ID is provided without action=pay, show only the selected unpaid booking.
        if (idParameter != null && !idParameter.isBlank()) {
            try {
                Long bookingId = Long.valueOf(idParameter);
                Booking selectedBooking = bookingFacade.find(bookingId);

                if (selectedBooking == null) {
                    request.setAttribute("error","Booking not found.");
                    request.setAttribute("unpaidBookings",List.of());
                } else if (selectedBooking.getBookingStatus()!= BookingStatus.UNPAID) {
                    request.setAttribute("error","This booking is not awaiting payment.");
                    request.setAttribute("unpaidBookings",List.of());
                } else {
                    request.setAttribute("unpaidBookings",List.of(selectedBooking));
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error","Invalid booking ID.");
                request.setAttribute("unpaidBookings",List.of());
            }
        } else {
            //Opening /counter/Receipt without an ID still shows every unpaid booking.
            List<Booking> unpaidBookings = bookingFacade.findByStatus(BookingStatus.UNPAID);
            request.setAttribute("unpaidBookings",unpaidBookings);
        }

        request.getRequestDispatcher("/counter/receipt.jsp").forward(request, response);
    }
}