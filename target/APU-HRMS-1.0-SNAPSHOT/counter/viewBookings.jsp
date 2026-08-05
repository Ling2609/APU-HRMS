<%-- 
    Document   : viewBookings
    Created on : Aug 4, 2026, 1:17:04 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Booking, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.COUNTER_STAFF) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>
<!DOCTYPE html>
<html>
<head>
    <title>All Bookings</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    <div class="navbar">
        <h1>APU Hotel</h1>
        <div class="nav-right">
            Welcome, <%= user.getName() %>
            <a href="${pageContext.request.contextPath}/counter/home.jsp">Home</a>
            <a href="${pageContext.request.contextPath}/Logout">Logout</a>
        </div>
    </div>
    <div class="container">
        <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #b8860b; padding-bottom: 8px; margin-bottom: 20px;">
            <div class="page-title" style="border:none; margin:0; padding:0;">All Bookings</div>
            <a href="${pageContext.request.contextPath}/counter/BookRoom" class="btn btn-gold">+ New Booking</a>
        </div>

        <% if (request.getAttribute("success") != null) { %>
            <div class="msg-success"><%= request.getAttribute("success") %></div>
        <% } %>

        <% if (bookings == null || bookings.isEmpty()) { %>
            <p>No bookings found.</p>
        <% } else { %>
            <div class="booking-table-container">
                <!-- Fixed blue header -->
                <table class="booking-header-table">
                    <colgroup>
                        <col class="col-id">
                        <col class="col-customer">
                        <col class="col-room">
                        <col class="col-roomtype">
                        <col class="col-date">
                        <col class="col-date">
                        <col class="col-date">
                        <col class="col-date">
                        <col class="col-payment">
                        <col class="col-status">
                        <col class="col-action">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Customer</th>
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
                </table>
                <!-- Scrollable body -->
                <div class="booking-body-wrapper">
                    <table class="booking-body-table">
                        <colgroup>
                            <col class="col-id">
                            <col class="col-customer">
                            <col class="col-room">
                            <col class="col-roomtype">
                            <col class="col-date">
                            <col class="col-date">
                            <col class="col-date">
                            <col class="col-date">
                            <col class="col-payment">
                            <col class="col-status">
                            <col class="col-action">
                        </colgroup>
                        <tbody>
                            <% for (Booking b : bookings) { %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getCustomer().getName() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                                <td><%= b.getCheckInTime() != null ? b.getCheckInTime().toLocalDate() : "-" %></td>
                                <td><%= b.getCheckOutTime() != null ? b.getCheckOutTime().toLocalDate() : "-" %></td>
                                <td><%= String.format("%.2f", b.getPayment()) %></td>
                                <td><%= b.getBookingStatus() %></td>
                                <td>
                                    <% if (b.getBookingStatus() == entity.Booking.BookingStatus.UNPAID) { %>
                                        <a href="${pageContext.request.contextPath}/counter/Receipt">Collect Payment</a>
                                    <% } else if (b.getBookingStatus() == entity.Booking.BookingStatus.BOOKED) { %>
                                        <a href="${pageContext.request.contextPath}/counter/CheckIn">Check In</a>
                                    <% } else if (b.getBookingStatus() == entity.Booking.BookingStatus.CHECKED_IN) { %>
                                        <a href="${pageContext.request.contextPath}/counter/CheckOut">Check Out</a>
                                    <% } else if (b.getBookingStatus() == entity.Booking.BookingStatus.CHECKED_OUT) { %>
                                        <a href="${pageContext.request.contextPath}/counter/AssignTask">Assign Task</a>
                                    <% } else { %>
                                        -
                                    <% } %>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        <% } %>
    </div>
</body>
</html>