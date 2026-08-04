<%-- 
    Document   : checkOut
    Created on : Aug 4, 2026, 1:20:45 PM
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
<head><title>Check Out</title></head>
<body>
    <h2>Check Out</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <h3>Currently Checked-in Bookings</h3>
    <% if (bookings == null || bookings.isEmpty()) { %>
        <p>No checked-in bookings.</p>
    <% } else { %>
        <table border="1" cellpadding="5">
            <tr>
                <th>Booking ID</th>
                <th>Customer</th>
                <th>Room</th>
                <th>Room Type</th>
                <th>Check-in Time</th>
                <th>Est. Check-out</th>
                <th>Payment (RM)</th>
                <th>Action</th>
            </tr>
            <% for (Booking b : bookings) { %>
            <tr>
                <td><%= b.getId() %></td>
                <td><%= b.getCustomer().getName() %></td>
                <td><%= b.getRoom().getRoomNumber() %></td>
                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                <td><%= b.getCheckInTime() != null ? b.getCheckInTime().toLocalDate() + " " + b.getCheckInTime().toLocalTime().withNano(0) : "-" %></td>
                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                <td><%= String.format("%.2f", b.getPayment()) %></td>
                <td>
                    <a href="${pageContext.request.contextPath}/counter/CheckOut?action=checkout&id=<%= b.getId() %>"
                       onclick="return confirm('Confirm check-out for <%= b.getCustomer().getName() %>?')">
                        Check Out
                    </a>
                </td>
            </tr>
            <% } %>
        </table>
    <% } %>
</body>
</html>
