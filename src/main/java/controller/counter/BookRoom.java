/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.counter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import entity.Booking;
import entity.Booking.BookingStatus;
import entity.BookingUser;
import entity.BookingUser.BookingUserRole;
import entity.Room;
import entity.RoomType;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import session.BookingFacade;
import session.BookingUserFacade;
import session.RoomFacade;
import session.RoomTypeFacade;
import session.UserFacade;

/**
 *
 * @author Ling
 */
@WebServlet(name = "BookRoom", urlPatterns = {"/counter/BookRoom"})
public class BookRoom extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB private UserFacade userFacade;
    @EJB private RoomFacade roomFacade;
    @EJB private RoomTypeFacade roomTypeFacade;
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

        String action = request.getParameter("action");

        // Select a customer → show booking form
        if ("select".equals(action)) {
            Long customerId = Long.parseLong(request.getParameter("customerId"));
            User selectedCustomer = userFacade.find(customerId);
            List<RoomType> roomTypes = roomTypeFacade.findAllRoomTypes();
            request.setAttribute("selectedCustomer", selectedCustomer);
            request.setAttribute("roomTypes", roomTypes);
            request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);
            return;
        }

        // Show all or search
        String keyword = request.getParameter("keyword");
        List<User> customers;
        if (keyword != null && !keyword.trim().isEmpty()) {
            customers = userFacade.searchCustomers(keyword.trim());
        } else {
            customers = userFacade.findAllCustomers();
        }
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User staff = (User) session.getAttribute("user");
        if (staff == null || staff.getRole() != User.Role.COUNTER_STAFF) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String customerIdStr = request.getParameter("customerId");
        String roomTypeIdStr = request.getParameter("roomTypeId");
        String checkInDateStr = request.getParameter("checkInDate");
        String checkOutDateStr = request.getParameter("checkOutDate");

        // Always load these first so JSP can re-display on error
        Long customerId = Long.parseLong(customerIdStr);
        User customer = userFacade.find(customerId);
        List<RoomType> roomTypes = roomTypeFacade.findAllRoomTypes();
        request.setAttribute("selectedCustomer", customer);
        request.setAttribute("roomTypes", roomTypes);

        try {
            Long roomTypeId = Long.parseLong(roomTypeIdStr);
            LocalDate checkInDate = LocalDate.parse(checkInDateStr);
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);
            LocalDate today = LocalDate.now();
            LocalDate maxDate = today.plusDays(5);

            // Declare checkIn and checkOut here
            LocalDateTime checkIn = checkInDate.atStartOfDay();
            LocalDateTime checkOut = checkOutDate.atStartOfDay();

            // Validation
            if (checkInDate.isBefore(today)) {
                request.setAttribute("error", "Check-in date cannot be in the past.");
                request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);
                return;
            }
            if (checkInDate.isAfter(maxDate)) {
                request.setAttribute("error", "Check-in date must be within the next 5 days.");
                request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);
                return;
            }
            if (!checkOutDate.isAfter(checkInDate)) {
                request.setAttribute("error", "Check-out date must be after check-in date.");
                request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);
                return;
            }

            // Check duplicate only if not confirmed
            String confirmed = request.getParameter("confirmed");
            if (!"true".equals(confirmed)) {
                if (bookingFacade.hasDuplicateRoomTypeBooking(customerId, roomTypeId, checkIn, checkOut)) {
                    request.setAttribute("warning", "This customer already has a booking for this room type during the selected dates. Are you sure you want to book another one?");
                    request.setAttribute("confirmBooking", true);
                    request.setAttribute("customerId", customerId);
                    request.setAttribute("roomTypeId", roomTypeId);
                    request.setAttribute("checkInDate", checkInDateStr);
                    request.setAttribute("checkOutDate", checkOutDateStr);
                    request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);
                    return;
                }
            }

            // Find available room of selected type
            List<Room> available = roomFacade.findAvailableByType(roomTypeId);
            if (available.isEmpty()) {
                request.setAttribute("error", "No available rooms of this type. Please choose another type.");
                request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);
                return;
            }

            Room room = available.get(0);
            RoomType roomType = roomTypeFacade.find(roomTypeId);

            long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            double payment = roomType.getRoomTypePrice() * nights;

            Booking booking = new Booking(customer, staff, checkIn, checkOut,
                    payment, room, BookingStatus.UNPAID);
            bookingFacade.create(booking);

            BookingUser bu = new BookingUser(booking, customer, BookingUserRole.CUSTOMER);
            bookingUserFacade.create(bu);

            response.sendRedirect(request.getContextPath()
                    + "/counter/ViewBookings?success=Room+" + room.getRoomNumber()
                    + "+booked+for+" + customer.getName()
                    + ".+Total:+RM" + String.format("%.2f", payment)
                    + "+(" + nights + "+night(s)).+Status:+UNPAID.");
            return;
            
        } catch (Exception e) {
            request.setAttribute("error", "Booking failed: " + e.getMessage());
        }
        request.getRequestDispatcher("/counter/bookRoom.jsp").forward(request, response);
    }
}