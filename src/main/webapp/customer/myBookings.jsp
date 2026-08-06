<%-- 
    Document   : myBookings
    Created on : Aug 4, 2026, 2:20:46 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Booking, java.util.List, java.util.Set" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.CUSTOMER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
    Set<Long> commentedBookingIds = (Set<Long>) request.getAttribute("commentedBookingIds");
    if (commentedBookingIds == null) commentedBookingIds = new java.util.HashSet<>();
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Bookings</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    <div class="navbar">
        <h1>APU Hotel</h1>
        <div class="nav-right">
            Welcome, <%= user.getName() %>
            <a href="${pageContext.request.contextPath}/customer/home.jsp">Home</a>
            <a href="${pageContext.request.contextPath}/Logout">Logout</a>
        </div>
    </div>
    <div class="container">
        <div class="page-title">My Bookings & Payment History</div>
        <br>
        <% if (bookings == null || bookings.isEmpty()) { %>
            <p>No bookings found.</p>
        <% } else { %>
            <div class="table-wrapper customer-bookings-wrapper">
                <table class="data-table customer-bookings-table">
                    <colgroup>
                        <col class="booking-id-col">
                        <col class="room-col">
                        <col class="room-type-col">
                        <col class="date-col">
                        <col class="date-col">
                        <col class="date-col">
                        <col class="date-col">
                        <col class="payment-col">
                        <col class="status-col">
                        <col class="action-col">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Room</th>
                            <th>Room Type</th>
                            <th>Est. Check-in</th>
                            <th>Est. Check-out</th>
                            <th>Actual Check-in</th>
                            <th>Actual Check-out</th>
                            <th>Payment (RM)</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Booking b : bookings) { %>
                        <tr>
                            <td><%= b.getId() %></td>
                            <td><%= b.getRoom().getRoomNumber() %></td>
                            <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                            <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                            <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                            <td><%= b.getCheckInTime() != null ? b.getCheckInTime().toLocalDate() : "-" %></td>
                            <td><%= b.getCheckOutTime() != null ? b.getCheckOutTime().toLocalDate() : "-" %></td>
                            <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                            <td><%= b.getBookingStatus() %></td>
                            <td>
                                <% if (b.getBookingStatus() == entity.Booking.BookingStatus.CHECKED_OUT) { %>
                                    
                                    <% if (commentedBookingIds.contains(b.getId())) { %>
                                        <a href="${pageContext.request.contextPath}/customer/ViewComments?bookingId=<%= b.getId() %>" class="action-link">View Written Comment</a>
                                    <% } else { %>
                                        <a href="${pageContext.request.contextPath}/customer/WriteComment?bookingId=<%= b.getId() %>" class="action-link">Write Comment</a>
                                    <% } %>
                                    
                                <% } else { %>
                                    -
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>
</body>
</html>