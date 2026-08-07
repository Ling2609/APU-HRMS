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
import java.util.List;

/**
 *
 * @author Ling
 */
@WebServlet(name = "CheckOut", urlPatterns = {"/counter/CheckOut"})
public class CheckOut extends HttpServlet {

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
    @EJB private RoomFacade roomFacade;

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
        String idParameter = request.getParameter("id");

        // Perform actual check-out.
        if ("checkout".equals(action)) {
            try {
                Long bookingId = Long.valueOf(idParameter);
                Booking booking = bookingFacade.find(bookingId);

                if (booking == null) {
                    request.setAttribute("error", "Booking not found.");

                } else if (booking.getBookingStatus() != BookingStatus.CHECKED_IN) {
                    request.setAttribute("error", "This booking is not currently checked in.");

                } else if (booking.getCheckInTime() == null) {
                    request.setAttribute("error", "This booking has no recorded check-in time.");

                } else {
                    booking.setCheckOutTime(LocalDateTime.now());
                    booking.setBookingStatus(BookingStatus.CHECKED_OUT);
                    bookingFacade.edit(booking);

                    Room room = booking.getRoom();
                    room.setRoomStatus(RoomStatus.CLEANING);
                    roomFacade.edit(room);

                    request.setAttribute(
                            "success",
                            "Check-out successful for "
                            + booking.getCustomer().getName()
                            + " - Room "
                            + room.getRoomNumber()
                    );

                    request.setAttribute("checkedOutBooking", booking);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid booking ID.");
            }

            List<Booking> remainingBookings = bookingFacade.findByStatus(BookingStatus.CHECKED_IN);
            request.setAttribute("bookings", remainingBookings);

            request.getRequestDispatcher("/counter/checkOut.jsp").forward(request, response);
            return;
        }

        // Show only the selected checked-in booking when an ID is provided.
        if (idParameter != null && !idParameter.isBlank()) {
            try {
                Long bookingId = Long.valueOf(idParameter);
                Booking selectedBooking = bookingFacade.find(bookingId);

                if (selectedBooking == null) {
                    request.setAttribute("error", "Booking not found.");
                    request.setAttribute("bookings", List.of());

                } else if (selectedBooking.getBookingStatus() != BookingStatus.CHECKED_IN) {
                    request.setAttribute("error", "This booking is not currently checked in.");
                    request.setAttribute("bookings", List.of());

                } else if (selectedBooking.getCheckInTime() == null) {
                    request.setAttribute("error", "This booking has no recorded check-in time.");
                    request.setAttribute("bookings", List.of());

                } else {
                    request.setAttribute("bookings", List.of(selectedBooking));
                }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid booking ID.");
                request.setAttribute("bookings", List.of());
            }

        } else {
            List<Booking> bookings = bookingFacade.findByStatus(BookingStatus.CHECKED_IN);
            request.setAttribute("bookings", bookings);
        }

        request.getRequestDispatcher("/counter/checkOut.jsp").forward(request, response);
    }
}