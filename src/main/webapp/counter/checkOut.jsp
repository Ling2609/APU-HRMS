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
<head>
    <title>Check Out</title>
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
        <div class="page-title">Check Out</div>
        <br>
        <% if (request.getAttribute("success") != null) { %>
            <div class="msg-success"><%= request.getAttribute("success") %></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="msg-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <h3 style="color:#1a237e; margin-bottom:15px;">Currently Checked-in Bookings</h3>

        <% if (bookings == null || bookings.isEmpty()) { %>
            <p>No checked-in bookings.</p>
        <% } else { %>
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
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
                    </thead>
                    <tbody>
                        <% for (Booking b : bookings) { %>
                        <tr>
                            <td><%= b.getId() %></td>
                            <td><%= b.getCustomer().getName() %></td>
                            <td><%= b.getRoom().getRoomNumber() %></td>
                            <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                            <td><%= b.getCheckInTime() != null ? b.getCheckInTime().toLocalDate() + " " + b.getCheckInTime().toLocalTime().withNano(0) : "-" %></td>
                            <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                            <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/counter/CheckOut?action=checkout&id=<%= b.getId() %>"
                                   class="action-link"
                                   onclick="return confirm('Confirm check-out for <%= b.getCustomer().getName() %>?')">
                                    Check Out
                                </a>
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