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
import java.time.LocalDate;
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
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");

        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String idParameter = request.getParameter("id");
        LocalDate today = LocalDate.now();

        // Change overdue BOOKED bookings to LATE first.
        bookingFacade.updateLateBookings();

        //Perform the actual check-in.
        if ("checkin".equals(action)) {
            try {
                Long bookingId = Long.valueOf(idParameter);
                Booking booking = bookingFacade.find(bookingId);

                if (booking == null) {
                    request.setAttribute("error","Booking not found.");
                } else if (booking.getBookingStatus() != BookingStatus.BOOKED && booking.getBookingStatus() != BookingStatus.LATE) {
                    request.setAttribute("error","This booking is not eligible for check-in.");
                } else if (booking.getEstimatedCheckInTime().toLocalDate().isAfter(today)) {
                    request.setAttribute("error","Check-in is only available on or after the estimated check-in date.");
                } else if (!today.isBefore(booking.getEstimatedCheckOutTime().toLocalDate())){
                    request.setAttribute("error","This booking has already reached or passed its estimated check-out date.");
                } else {
                    booking.setCheckInTime(LocalDateTime.now());
                    booking.setBookingStatus(BookingStatus.CHECKED_IN);
                    bookingFacade.edit(booking);

                    Room room = booking.getRoom();
                    room.setRoomStatus(RoomStatus.OCCUPIED);
                    roomFacade.edit(room);

                    request.setAttribute("success", "Check-in successful for " + booking.getCustomer().getName() + " - Room " + room.getRoomNumber());
                    request.setAttribute("checkedInBooking",booking);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error","Invalid booking ID.");
            }

            List<Booking> remainingBookings = new ArrayList<>();
            List<Booking> bookedBookings = bookingFacade.findByStatus(BookingStatus.BOOKED);

            for (Booking pendingBooking : bookedBookings) {
                LocalDate checkInDate = pendingBooking.getEstimatedCheckInTime().toLocalDate();

                LocalDate checkOutDate = pendingBooking.getEstimatedCheckOutTime().toLocalDate();

                if (!checkInDate.isAfter(today) && today.isBefore(checkOutDate)) {
                    remainingBookings.add(pendingBooking);
                }
            }

            List<Booking> lateBookings
                    = bookingFacade.findByStatus(BookingStatus.LATE);

            for (Booking pendingBooking : lateBookings) {
                LocalDate checkInDate = pendingBooking.getEstimatedCheckInTime().toLocalDate();

                LocalDate checkOutDate = pendingBooking.getEstimatedCheckOutTime().toLocalDate();

                if (!checkInDate.isAfter(today) && today.isBefore(checkOutDate)) {
                    remainingBookings.add(pendingBooking);
                }
            }

            request.setAttribute("bookings", remainingBookings);
            request.getRequestDispatcher("/counter/checkIn.jsp").forward(request, response);

            return;
        }

        //Show only the selected booking when an ID is provided.
        if (idParameter != null && !idParameter.isBlank()) {
            try {
                Long bookingId = Long.valueOf(idParameter);
                Booking selectedBooking = bookingFacade.find(bookingId);

                if (selectedBooking == null) {
                    request.setAttribute("error","Booking not found.");
                    request.setAttribute("bookings",List.of());
                    
                } else if (selectedBooking.getBookingStatus() != BookingStatus.BOOKED && selectedBooking.getBookingStatus() != BookingStatus.LATE) {
                    request.setAttribute("error","This booking is not eligible for check-in.");
                    request.setAttribute("bookings",List.of());

                } else if (selectedBooking.getEstimatedCheckInTime().toLocalDate().isAfter(today)) {
                    request.setAttribute("error", "Check-in is only available on or after the estimated check-in date.");
                    request.setAttribute("bookings",List.of());
                    
                }else if (!today.isBefore(selectedBooking.getEstimatedCheckOutTime().toLocalDate())) {
                    request.setAttribute("error", "This booking has already reached or passed its estimated check-out date.");
                    request.setAttribute("bookings", List.of());
                } else {
                    request.setAttribute("bookings",List.of(selectedBooking));
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error","Invalid booking ID.");
                request.setAttribute("bookings", List.of());
            }
        } else {
            // Opening CheckIn without an ID displays bookings that are within the valid check-in period.
            List<Booking> bookings = new ArrayList<>();
            List<Booking> bookedBookings = bookingFacade.findByStatus(BookingStatus.BOOKED);

            for (Booking booking : bookedBookings) {
                LocalDate estimatedCheckInDate
                        = booking.getEstimatedCheckInTime().toLocalDate();

                LocalDate estimatedCheckOutDate
                        = booking.getEstimatedCheckOutTime().toLocalDate();

                if (!estimatedCheckInDate.isAfter(today)
                        && today.isBefore(estimatedCheckOutDate)) {
                    bookings.add(booking);
                }
            }

            List<Booking> lateBookings = bookingFacade.findByStatus(BookingStatus.LATE);

            for (Booking booking : lateBookings) {
                LocalDate estimatedCheckInDate = booking.getEstimatedCheckInTime().toLocalDate();
                LocalDate estimatedCheckOutDate = booking.getEstimatedCheckOutTime().toLocalDate();
                
                if (!estimatedCheckInDate.isAfter(today) && today.isBefore(estimatedCheckOutDate)) {
                    bookings.add(booking);
                }
            }
            request.setAttribute("bookings", bookings);
        }

        request.getRequestDispatcher("/counter/checkIn.jsp").forward(request, response);
    }
}