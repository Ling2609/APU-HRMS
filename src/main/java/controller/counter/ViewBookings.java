package controller.counter;

import entity.Booking;
import entity.Room;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import session.BookingFacade;
import session.BookingUserFacade;

@WebServlet(name = "ViewBookings", urlPatterns = {"/counter/ViewBookings"})
public class ViewBookings extends HttpServlet {

    @EJB
    private BookingFacade bookingFacade;

    @EJB
    private BookingUserFacade bookingUserFacade;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        User staff = (User) session.getAttribute("user");

        if (staff == null
                || staff.getRole() != User.Role.COUNTER_STAFF) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/common/login.jsp"
            );

            return;
        }

        String success = request.getParameter("success");

        if (success != null) {
            request.setAttribute(
                    "success",
                    success.replace("+", " ")
            );
        }

        String error = request.getParameter("error");

        if (error != null) {
            request.setAttribute(
                    "error",
                    error.replace("+", " ")
            );
        }

        List<Booking> bookings = bookingFacade.findAllBookings();

        Set<Long> assignedCleaningBookings = new HashSet<>();
        Set<Long> assignableCleaningBookings = new HashSet<>();

        for (Booking b : bookings) {

            if (b.getBookingStatus()
                    != Booking.BookingStatus.CHECKED_OUT) {
                continue;
            }

            boolean assigned
                    = bookingUserFacade.hasHousekeeperAssigned(
                            b.getId()
                    );

            if (assigned) {
                assignedCleaningBookings.add(b.getId());
            }

            Booking latestBooking
                    = bookingFacade.findLatestByRoom(
                            b.getRoom().getId()
                    );

            boolean latestForRoom
                    = latestBooking != null
                    && latestBooking.getId().equals(b.getId());

            boolean roomNeedsCleaning
                    = b.getRoom().getRoomStatus()
                    == Room.RoomStatus.CLEANING;

            if (!assigned
                    && latestForRoom
                    && roomNeedsCleaning) {

                assignableCleaningBookings.add(b.getId());
            }
        }

        request.setAttribute(
                "bookings",
                bookings
        );

        request.setAttribute(
                "assignedCleaningBookings",
                assignedCleaningBookings
        );

        request.setAttribute(
                "assignableCleaningBookings",
                assignableCleaningBookings
        );

        request.getRequestDispatcher(
                "/counter/viewBookings.jsp"
        ).forward(request, response);
    }
}