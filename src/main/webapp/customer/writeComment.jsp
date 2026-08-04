<%-- 
    Document   : writeComment
    Created on : Aug 4, 2026, 2:26:40 PM
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
    Booking selectedBooking = (Booking) request.getAttribute("selectedBooking");
    Set<Long> commentedIds = (Set<Long>) request.getAttribute("commentedIds");
    if (commentedIds == null) commentedIds = new java.util.HashSet<>();
%>
<!DOCTYPE html>
<html>
<head><title>Write Comment</title></head>
<body>
    <h2>Write Comment</h2>
    <a href="${pageContext.request.contextPath}/customer/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <% if (selectedBooking == null) { %>
        <h3>Select a Completed Booking to Comment</h3>
        <% if (bookings == null || bookings.isEmpty()) { %>
            <p>No completed bookings to comment on.</p>
        <% } else { %>
            <table border="1" cellpadding="5">
                <tr>
                    <th>Booking ID</th>
                    <th>Room</th>
                    <th>Room Type</th>
                    <th>Check-out Date</th>
                    <th>Action</th>
                </tr>
                <% for (Booking b : bookings) { %>
                <tr>
                    <td><%= b.getId() %></td>
                    <td><%= b.getRoom().getRoomNumber() %></td>
                    <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                    <td><%= b.getCheckOutTime() != null ? b.getCheckOutTime().toLocalDate() : "-" %></td>
                    <td>
                        <% if (commentedIds.contains(b.getId())) { %>
                            <span style="color:gray;">Already Commented</span>
                        <% } else { %>
                            <a href="${pageContext.request.contextPath}/customer/WriteComment?action=select&bookingId=<%= b.getId() %>">
                                Write Comment
                            </a>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </table>
        <% } %>

    <% } else { %>
        <h3>Comment for Booking #<%= selectedBooking.getId() %> - Room <%= selectedBooking.getRoom().getRoomNumber() %></h3>
        <a href="${pageContext.request.contextPath}/customer/WriteComment">← Back to booking list</a>
        <br><br>
        <form method="post" action="${pageContext.request.contextPath}/customer/WriteComment">
            <input type="hidden" name="bookingId" value="<%= selectedBooking.getId() %>" />
            <table>
                <tr>
                    <td>Rating:</td>
                    <td>
                        <select name="rating" required>
                            <option value="">-- Select Rating --</option>
                            <option value="1">1 - Poor</option>
                            <option value="2">2 - Fair</option>
                            <option value="3">3 - Good</option>
                            <option value="4">4 - Very Good</option>
                            <option value="5">5 - Excellent</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Comment:</td>
                    <td><textarea name="comment" rows="4" cols="40" required></textarea></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Submit Comment" /></td>
                </tr>
            </table>
        </form>
    <% } %>
</body>
</html>