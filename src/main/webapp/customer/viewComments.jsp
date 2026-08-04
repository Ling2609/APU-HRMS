<%-- 
    Document   : viewComments
    Created on : Aug 4, 2026, 3:03:34 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Message, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.CUSTOMER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<Message> comments = (List<Message>) request.getAttribute("comments");
%>
<!DOCTYPE html>
<html>
<head><title>My Comments</title></head>
<body>
    <h2>My Comments</h2>
    <a href="${pageContext.request.contextPath}/customer/home.jsp">Back to Home</a>
    <hr>
    <% if (comments == null || comments.isEmpty()) { %>
        <p>No comments yet.</p>
    <% } else { %>
        <table border="1" cellpadding="5">
            <tr>
                <th>Booking ID</th>
                <th>Room</th>
                <th>Room Type</th>
                <th>Rating</th>
                <th>Comment</th>
            </tr>
            <% for (Message m : comments) { %>
            <tr>
                <td><%= m.getBookingUser().getBooking().getId() %></td>
                <td><%= m.getBookingUser().getBooking().getRoom().getRoomNumber() %></td>
                <td><%= m.getBookingUser().getBooking().getRoom().getRoomType().getRoomTypeName() %></td>
                <td><%= m.getRating() %> / 5</td>
                <td><%= m.getMessageContent() %></td>
            </tr>
            <% } %>
        </table>
    <% } %>
</body>
</html>
