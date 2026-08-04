<%-- 
    Document   : checkIn
    Created on : Aug 4, 2026, 1:13:46 PM
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
<head><title>Check In</title></head>
<body>
    <h2>Check In</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <h3>Pending Check-ins (Payment Collected - Ready to Check In)</h3>
    <% if (bookings == null || bookings.isEmpty()) { %>
        <p>No pending check-ins.</p>
    <% } else { %>
        <table border="1" cellpadding="5">
            <tr>
                <th>Booking ID</th>
                <th>Customer</th>
                <th>Room</th>
                <th>Room Type</th>
                <th>Est. Check-in</th>
                <th>Est. Check-out</th>
                <th>Payment (RM)</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            <% for (Booking b : bookings) { %>
            <tr>
                <td><%= b.getId() %></td>
                <td><%= b.getCustomer().getName() %></td>
                <td><%= b.getRoom().getRoomNumber() %></td>
                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                <td><%= String.format("%.2f", b.getPayment()) %></td>
                <td><%= b.getBookingStatus() %></td>
                <td>
                    <a href="${pageContext.request.contextPath}/counter/CheckIn?action=checkin&id=<%= b.getId() %>"
                       onclick="return confirm('Confirm check-in for <%= b.getCustomer().getName() %>?')">
                        Check In
                    </a>
                </td>
            </tr>
            <% } %>
        </table>
    <% } %>
</body>
</html>