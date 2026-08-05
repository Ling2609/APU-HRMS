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
        String successMsg = request.getParameter("success");
        if (successMsg != null) {
            successMsg = successMsg.replace("+", " ");
        }
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Comments</title>
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
        <div class="page-title">My Comments</div>
        <br>
        <% if (successMsg != null) { %>
            <div class="msg-success"><%= successMsg %></div>
        <% } %>
        <% if (comments == null || comments.isEmpty()) { %>
            <p>No comments yet.</p>
        <% } else { %>
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Room</th>
                            <th>Room Type</th>
                            <th>Rating</th>
                            <th>Comment</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Message m : comments) { %>
                        <tr>
                            <td><%= m.getBookingUser().getBooking().getId() %></td>
                            <td><%= m.getBookingUser().getBooking().getRoom().getRoomNumber() %></td>
                            <td><%= m.getBookingUser().getBooking().getRoom().getRoomType().getRoomTypeName() %></td>
                            <td>
                                <% int rating = m.getRating(); %>
                                <%= rating %> ⭐
                            </td>
                            <td style="white-space: normal;"><%= m.getMessageContent() %></td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>
</body>
</html>