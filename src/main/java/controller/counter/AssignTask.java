/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.counter;

import entity.*;
import entity.BookingUser.BookingUserRole;
import entity.Room.RoomStatus;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import session.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ling
 */
@WebServlet(name = "AssignTask", urlPatterns = {"/counter/AssignTask"})
public class AssignTask extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB private RoomFacade roomFacade;
    @EJB private UserFacade userFacade;
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
        
        String success = request.getParameter("success");
        if (success != null) {
            request.setAttribute("success", success.replace("+", " "));
        }

        String action = request.getParameter("action");

        if ("select".equals(action)) {
            Long roomId = Long.parseLong(request.getParameter("roomId"));
            Room room = roomFacade.find(roomId);
            List<User> housekeepers = userFacade.findAllHousekeepers();
            request.setAttribute("selectedRoom", room);
            request.setAttribute("housekeepers", housekeepers);
            request.getRequestDispatcher("/counter/assignTask.jsp").forward(request, response);
            return;
        }

        List<Room> cleaningRooms = roomFacade.findRoomsByStatus(RoomStatus.CLEANING);
        List<Room> unassignedRooms = new java.util.ArrayList<>();
        for (Room r : cleaningRooms) {
            Booking latestBooking = bookingFacade.findLatestByRoom(r.getId());
            if (latestBooking != null && !bookingUserFacade.hasHousekeeperAssigned(latestBooking.getId())) {
                unassignedRooms.add(r);
            }
        }
        request.setAttribute("cleaningRooms", unassignedRooms);
        request.getRequestDispatcher("/counter/assignTask.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");
        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        Long roomId = Long.parseLong(request.getParameter("roomId"));
        Long housekeeperId = Long.parseLong(request.getParameter("housekeeperId"));

        Room room = roomFacade.find(roomId);
        User housekeeper = userFacade.find(housekeeperId);

        // Find the latest checked-out booking for this room
        Booking booking = bookingFacade.findLatestByRoom(roomId);

        if (booking == null) {
            request.setAttribute("error", "No completed booking found for this room.");
            List<Room> cleaningRooms = roomFacade.findRoomsByStatus(RoomStatus.CLEANING);
            request.setAttribute("cleaningRooms", cleaningRooms);
            request.getRequestDispatcher("/counter/assignTask.jsp").forward(request, response);
            return;
        }

        // Check if already assigned
        if (bookingUserFacade.hasHousekeeperAssigned(booking.getId())) {
            request.setAttribute("error", "A housekeeper is already assigned to this room.");
            List<Room> cleaningRooms = roomFacade.findRoomsByStatus(RoomStatus.CLEANING);
            request.setAttribute("cleaningRooms", cleaningRooms);
            request.getRequestDispatcher("/counter/assignTask.jsp").forward(request, response);
            return;
        }

        // Assign housekeeper via BookingUser
        BookingUser bu = new BookingUser(booking, housekeeper, BookingUserRole.HOUSEKEEPER);
        bookingUserFacade.create(bu);

        // Also set the housekeeper on the booking
        booking.setCheckOutHousekeeper(housekeeper);
        bookingFacade.edit(booking);

        request.setAttribute("success", "Housekeeper " + housekeeper.getName() +
            " assigned to clean Room " + room.getRoomNumber() + " successfully.");

        // Only show rooms without housekeeper assigned
        List<Room> cleaningRooms = roomFacade.findRoomsByStatus(RoomStatus.CLEANING);
        List<Room> unassignedRooms = new java.util.ArrayList<>();
        for (Room r : cleaningRooms) {
            Booking latestBooking = bookingFacade.findLatestByRoom(r.getId());
            if (latestBooking != null && !bookingUserFacade.hasHousekeeperAssigned(latestBooking.getId())) {
                unassignedRooms.add(r);
            }
        }
        request.setAttribute("cleaningRooms", unassignedRooms);
        request.getRequestDispatcher("/counter/assignTask.jsp").forward(request, response);
    }
}