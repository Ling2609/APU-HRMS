<%-- 
    Document   : receipt.
    Created on : Aug 4, 2026, 1:24:01 PM
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
    List<Booking> unpaidBookings = (List<Booking>) request.getAttribute("unpaidBookings");
    Booking paidBooking = (Booking) request.getAttribute("paidBooking");
%>
<!DOCTYPE html>
<html>
<head><title>Collect Payment & Receipt</title></head>
<body>
    <h2>Collect Payment & Generate Receipt</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <% if (paidBooking != null) { %>
        <%-- Show receipt --%>
        <div id="receipt">
            <h3>===== OFFICIAL RECEIPT =====</h3>
            <p><strong>APU Hotel Room Management System</strong></p>
            <hr>
            <p>Receipt No: #<%= paidBooking.getId() %></p>
            <p>Date: <%= java.time.LocalDate.now() %></p>
            <hr>
            <p><strong>Customer Details</strong></p>
            <p>Name: <%= paidBooking.getCustomer().getName() %></p>
            <p>IC: <%= paidBooking.getCustomer().getIdentification() %></p>
            <p>Phone: <%= paidBooking.getCustomer().getPhone() %></p>
            <hr>
            <p><strong>Booking Details</strong></p>
            <p>Room Number: <%= paidBooking.getRoom().getRoomNumber() %></p>
            <p>Room Type: <%= paidBooking.getRoom().getRoomType().getRoomTypeName() %></p>
            <p>Rate: RM<%= String.format("%.2f", paidBooking.getRoom().getRoomType().getRoomTypePrice()) %>/night</p>
            <p>Est. Check-in: <%= paidBooking.getEstimatedCheckInTime().toLocalDate() %></p>
            <p>Est. Check-out: <%= paidBooking.getEstimatedCheckOutTime().toLocalDate() %></p>
            <p>Duration: <%= java.time.temporal.ChronoUnit.DAYS.between(paidBooking.getEstimatedCheckInTime(), paidBooking.getEstimatedCheckOutTime()) %> night(s)</p>
            <hr>
            <p><strong>Total Amount: RM<%= String.format("%.2f", paidBooking.getPayment()) %></strong></p>
            <p>Status: PAID / BOOKED</p>
            <p>Served by: <%= user.getName() %></p>
            <hr>
            <p>Thank you for choosing APU Hotel!</p>
            <h3>=============================</h3>
        </div>
        <br>
        <button onclick="window.print()">Print Receipt</button>
        <br><br>
        <a href="${pageContext.request.contextPath}/counter/Receipt">Back to Payment List</a>

    <% } else { %>
        <%-- Show unpaid bookings --%>
        <h3>Unpaid Bookings</h3>
        <% if (unpaidBookings == null || unpaidBookings.isEmpty()) { %>
            <p>No unpaid bookings.</p>
        <% } else { %>
            <table border="1" cellpadding="5">
                <tr>
                    <th>Booking ID</th>
                    <th>Customer</th>
                    <th>Room</th>
                    <th>Room Type</th>
                    <th>Est. Check-in</th>
                    <th>Est. Check-out</th>
                    <th>Amount (RM)</th>
                    <th>Action</th>
                </tr>
                <% for (Booking b : unpaidBookings) { %>
                <tr>
                    <td><%= b.getId() %></td>
                    <td><%= b.getCustomer().getName() %></td>
                    <td><%= b.getRoom().getRoomNumber() %></td>
                    <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                    <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                    <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                    <td><%= String.format("%.2f", b.getPayment()) %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/counter/Receipt?action=pay&id=<%= b.getId() %>"
                           onclick="return confirm('Collect payment of RM<%= String.format("%.2f", b.getPayment()) %> from <%= b.getCustomer().getName() %>?')">
                            Collect Payment
                        </a>
                    </td>
                </tr>
                <% } %>
            </table>
        <% } %>
    <% } %>
</body>
</html>
