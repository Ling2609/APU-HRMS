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
<head><title>All Bookings</title></head>
<body>
    <h2>All Bookings</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (bookings == null || bookings.isEmpty()) { %>
        <p>No bookings found.</p>
    <% } else { %>
        <table border="1" cellpadding="5">
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
            </tr>
            <% for (Booking b : bookings) { %>
            <tr>
                <td><%= b.getId() %></td>
                <td><%= b.getCustomer().getName() %></td>
                <td><%= b.getRoom().getRoomNumber() %></td>
                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                <td><%= b.getCheckInTime() != null ? b.getCheckInTime() : "-" %></td>
                <td><%= b.getCheckOutTime() != null ? b.getCheckOutTime() : "-" %></td>
                <td><%= String.format("%.2f", b.getPayment()) %></td>
                <td><%= b.getBookingStatus() %></td>
            </tr>
            <% } %>
        </table>
    <% } %>
</body>
</html>