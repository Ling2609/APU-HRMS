package controller.counter;

import entity.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import session.BookingFacade;
import session.BookingUserFacade;
import session.RoomFacade;
import session.UserFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AssignTask", urlPatterns = {"/counter/AssignTask"})
public class AssignTask extends HttpServlet {

    @EJB
    private RoomFacade roomFacade;

    @EJB
    private UserFacade userFacade;

    @EJB
    private BookingFacade bookingFacade;

    @EJB
    private BookingUserFacade bookingUserFacade;

    private List<Room> getUnassignedCleaningRooms() {
        List<Room> cleaningRooms
                = roomFacade.findRoomsByStatus(Room.RoomStatus.CLEANING);

        List<Room> unassignedRooms = new ArrayList<>();

        for (Room room : cleaningRooms) {
            Booking latestBooking
                    = bookingFacade.findLatestByRoom(room.getId());

            if (latestBooking != null
                    && !bookingUserFacade.hasHousekeeperAssigned(
                            latestBooking.getId())) {

                unassignedRooms.add(room);
            }
        }

        return unassignedRooms;
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");

        if (staff == null
                || staff.getRole() != User.Role.COUNTER_STAFF) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/common/login.jsp");

            return;
        }

        String success = request.getParameter("success");

        if (success != null) {
            request.setAttribute(
                    "success",
                    success.replace("+", " "));
        }

        String action = request.getParameter("action");

        if ("select".equals(action)) {

            try {
                Long roomId
                        = Long.parseLong(request.getParameter("roomId"));

                Room room = roomFacade.find(roomId);

                if (room == null
                        || room.getRoomStatus()
                        != Room.RoomStatus.CLEANING) {

                    request.setAttribute(
                            "error",
                            "This room is no longer available for cleaning assignment.");

                    request.setAttribute(
                            "cleaningRooms",
                            getUnassignedCleaningRooms());

                    request.getRequestDispatcher(
                            "/counter/assignTask.jsp")
                            .forward(request, response);

                    return;
                }

                Booking latestBooking
                        = bookingFacade.findLatestByRoom(roomId);

                if (latestBooking == null) {

                    request.setAttribute(
                            "error",
                            "No completed booking found for this room.");

                    request.setAttribute(
                            "cleaningRooms",
                            getUnassignedCleaningRooms());

                    request.getRequestDispatcher(
                            "/counter/assignTask.jsp")
                            .forward(request, response);

                    return;
                }

                if (bookingUserFacade.hasHousekeeperAssigned(
                        latestBooking.getId())) {

                    request.setAttribute(
                            "error",
                            "A housekeeper is already assigned to this room.");

                    request.setAttribute(
                            "cleaningRooms",
                            getUnassignedCleaningRooms());

                    request.getRequestDispatcher(
                            "/counter/assignTask.jsp")
                            .forward(request, response);

                    return;
                }

                List<User> housekeepers
                        = userFacade.findAllHousekeepers();

                request.setAttribute("selectedRoom", room);
                request.setAttribute("housekeepers", housekeepers);

                request.getRequestDispatcher(
                        "/counter/assignTask.jsp")
                        .forward(request, response);

                return;

            } catch (Exception e) {

                request.setAttribute(
                        "error",
                        "Invalid room selection.");

                request.setAttribute(
                        "cleaningRooms",
                        getUnassignedCleaningRooms());

                request.getRequestDispatcher(
                        "/counter/assignTask.jsp")
                        .forward(request, response);

                return;
            }
        }

        request.setAttribute(
                "cleaningRooms",
                getUnassignedCleaningRooms());

        request.getRequestDispatcher(
                "/counter/assignTask.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");

        if (staff == null
                || staff.getRole() != User.Role.COUNTER_STAFF) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/common/login.jsp");

            return;
        }

        try {
            Long roomId
                    = Long.parseLong(request.getParameter("roomId"));

            Long housekeeperId
                    = Long.parseLong(
                            request.getParameter("housekeeperId"));

            Room room = roomFacade.find(roomId);
            User housekeeper = userFacade.find(housekeeperId);

            if (room == null
                    || room.getRoomStatus()
                    != Room.RoomStatus.CLEANING) {

                request.setAttribute(
                        "error",
                        "This room is no longer available for cleaning assignment.");

                request.setAttribute(
                        "cleaningRooms",
                        getUnassignedCleaningRooms());

                request.getRequestDispatcher(
                        "/counter/assignTask.jsp")
                        .forward(request, response);

                return;
            }

            if (housekeeper == null
                    || housekeeper.getRole()
                    != User.Role.HOUSEKEEPER) {

                request.setAttribute(
                        "error",
                        "Please select a valid housekeeper.");

                request.setAttribute("selectedRoom", room);
                request.setAttribute(
                        "housekeepers",
                        userFacade.findAllHousekeepers());

                request.getRequestDispatcher(
                        "/counter/assignTask.jsp")
                        .forward(request, response);

                return;
            }

            Booking booking
                    = bookingFacade.findLatestByRoom(roomId);

            if (booking == null) {

                request.setAttribute(
                        "error",
                        "No completed booking found for this room.");

                request.setAttribute(
                        "cleaningRooms",
                        getUnassignedCleaningRooms());

                request.getRequestDispatcher(
                        "/counter/assignTask.jsp")
                        .forward(request, response);

                return;
            }

            if (bookingUserFacade.hasHousekeeperAssigned(
                    booking.getId())) {

                request.setAttribute(
                        "error",
                        "A housekeeper is already assigned to this room.");

                request.setAttribute(
                        "cleaningRooms",
                        getUnassignedCleaningRooms());

                request.getRequestDispatcher(
                        "/counter/assignTask.jsp")
                        .forward(request, response);

                return;
            }

            BookingUser bookingUser
                    = new BookingUser(
                            booking,
                            housekeeper,
                            BookingUser.BookingUserRole.HOUSEKEEPER);

            bookingUserFacade.create(bookingUser);

            booking.setCheckOutHousekeeper(housekeeper);
            bookingFacade.edit(booking);

            request.setAttribute(
                    "success",
                    "Cleaning task assigned successfully.");

            request.setAttribute(
                    "cleaningRooms",
                    getUnassignedCleaningRooms());

            request.getRequestDispatcher(
                    "/counter/assignTask.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            request.setAttribute(
                    "error",
                    "Unable to assign cleaning task. Please try again.");

            request.setAttribute(
                    "cleaningRooms",
                    getUnassignedCleaningRooms());

            request.getRequestDispatcher(
                    "/counter/assignTask.jsp")
                    .forward(request, response);
        }
    }
}