/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import entity.*;
import entity.BookingUser.BookingUserRole;
import entity.Message.MessageType;
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
@WebServlet(name = "WriteComment", urlPatterns = {"/customer/WriteComment"})
public class WriteComment extends HttpServlet {

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
    @EJB private MessageFacade messageFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("select".equals(action)) {
            Long bookingId = Long.parseLong(request.getParameter("bookingId"));
            Booking booking = bookingFacade.find(bookingId);

            // Check already commented
            BookingUser bu = bookingUserFacade.findByBookingAndRole(bookingId, BookingUserRole.CUSTOMER);
            if (bu != null && messageFacade.hasCommented(bu.getId())) {
                request.setAttribute("error", "You have already commented on this booking.");
                List<Booking> bookings = bookingFacade.findCheckedOutByCustomer(user.getId());
                request.setAttribute("bookings", bookings);
                request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
                return;
            }

            request.setAttribute("selectedBooking", booking);
            request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
            return;
        }

        List<Booking> bookings = bookingFacade.findCheckedOutByCustomer(user.getId());

        // Build set of already commented booking IDs
        java.util.Set<Long> commentedIds = new java.util.HashSet<>();
        for (Booking b : bookings) {
            BookingUser bu = bookingUserFacade.findByBookingAndRole(b.getId(), BookingUserRole.CUSTOMER);
            if (bu != null && messageFacade.hasCommented(bu.getId())) {
                commentedIds.add(b.getId());
            }
        }

        request.setAttribute("bookings", bookings);
        request.setAttribute("commentedIds", commentedIds);
        request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        Long bookingId = Long.parseLong(request.getParameter("bookingId"));
        String comment = request.getParameter("comment").trim();
        String ratingStr = request.getParameter("rating");

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Please select a rating.");
            Booking booking = bookingFacade.find(bookingId);
            request.setAttribute("selectedBooking", booking);
            request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
            return;
        }

        if (comment.isEmpty()) {
            request.setAttribute("error", "Comment cannot be empty.");
            Booking booking = bookingFacade.find(bookingId);
            request.setAttribute("selectedBooking", booking);
            request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
            return;
        }
        
        if (comment.length() < 10) {
            request.setAttribute("error", "Comment must be at least 10 characters.");
            Booking booking = bookingFacade.find(bookingId);
            request.setAttribute("selectedBooking", booking);
            request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
            return;
        }

        // Find BookingUser record for this customer + booking
        BookingUser bu = bookingUserFacade.findByBookingAndRole(bookingId, BookingUserRole.CUSTOMER);

        if (bu == null) {
            request.setAttribute("error", "Booking not found for this customer.");
            List<Booking> bookings = bookingFacade.findCheckedOutByCustomer(user.getId());
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
            return;
        }

        // Check already commented
        if (messageFacade.hasCommented(bu.getId())) {
            request.setAttribute("error", "You have already commented on this booking.");
            List<Booking> bookings = bookingFacade.findCheckedOutByCustomer(user.getId());
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
            return;
        }

        Message message = new Message(bu, MessageType.COMMENT, comment, rating);
        messageFacade.create(message);

        request.setAttribute("success", "Comment submitted successfully. Thank you for your feedback!");
        List<Booking> bookings = bookingFacade.findCheckedOutByCustomer(user.getId());
        java.util.Set<Long> commentedIds = new java.util.HashSet<>();
        for (Booking b : bookings) {
            BookingUser bu2 = bookingUserFacade.findByBookingAndRole(b.getId(), BookingUserRole.CUSTOMER);
            if (bu2 != null && messageFacade.hasCommented(bu2.getId())) {
                commentedIds.add(b.getId());
            }
        }
        request.setAttribute("bookings", bookings);
        request.setAttribute("commentedIds", commentedIds);
        request.getRequestDispatcher("/customer/writeComment.jsp").forward(request, response);
    }
}