/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.counter;

import entity.*;
import entity.Booking.BookingStatus;
import entity.Room.RoomStatus;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import session.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ling
 */
@WebServlet(name = "CheckIn", urlPatterns = {"/counter/CheckIn"})
public class CheckIn extends HttpServlet {

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
    private RoomFacade roomFacade;

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

        if ("checkin".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Booking booking = bookingFacade.find(id);

            if (booking == null) {
                request.setAttribute("error", "Booking not found.");
            } else if (booking.getBookingStatus() != BookingStatus.BOOKED
                    && booking.getBookingStatus() != BookingStatus.LATE) {
                request.setAttribute("error", "Payment must be collected before check-in.");
            } else {
                booking.setCheckInTime(LocalDateTime.now());
                booking.setBookingStatus(BookingStatus.CHECKED_IN);
                bookingFacade.edit(booking);
                Room room = booking.getRoom();
                room.setRoomStatus(RoomStatus.OCCUPIED);
                roomFacade.edit(room);
                request.setAttribute("success", "Check-in successful for "
                        + booking.getCustomer().getName()
                        + " - Room " + room.getRoomNumber());
            }
        }

        // Update late bookings first
        bookingFacade.updateLateBookings();

        // Show BOOKED and LATE bookings
        List<Booking> bookings = new ArrayList<>();
        bookings.addAll(bookingFacade.findByStatus(BookingStatus.BOOKED));
        bookings.addAll(bookingFacade.findByStatus(BookingStatus.LATE));
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/counter/checkIn.jsp").forward(request, response);
    }
}
