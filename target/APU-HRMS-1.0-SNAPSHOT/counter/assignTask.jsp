<%-- 
    Document   : assignTask
    Created on : Aug 4, 2026, 1:42:28 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Room, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.COUNTER_STAFF) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<Room> cleaningRooms = (List<Room>) request.getAttribute("cleaningRooms");
    List<User> housekeepers = (List<User>) request.getAttribute("housekeepers");
    Room selectedRoom = (Room) request.getAttribute("selectedRoom");
%>
<!DOCTYPE html>
<html>
<head><title>Assign Cleaning Task</title></head>
<body>
    <h2>Assign Cleaning Task</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <% if (selectedRoom == null) { %>
        <h3>Rooms Requiring Cleaning</h3>
        <% if (cleaningRooms == null || cleaningRooms.isEmpty()) { %>
            <p>No rooms require cleaning at the moment.</p>
        <% } else { %>
            <table border="1" cellpadding="5">
                <tr>
                    <th>Room Number</th>
                    <th>Floor</th>
                    <th>Room Type</th>
                    <th>Action</th>
                </tr>
                <% for (Room r : cleaningRooms) { %>
                <tr>
                    <td><%= r.getRoomNumber() %></td>
                    <td><%= r.getRoomNumber() / 1000 %></td>
                    <td><%= r.getRoomType().getRoomTypeName() %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/counter/AssignTask?action=select&roomId=<%= r.getId() %>">
                            Assign Housekeeper
                        </a>
                    </td>
                </tr>
                <% } %>
            </table>
        <% } %>

    <% } else { %>
        <h3>Assign Housekeeper for Room <%= selectedRoom.getRoomNumber() %></h3>
        <p>Room Type: <%= selectedRoom.getRoomType().getRoomTypeName() %></p>
        <a href="${pageContext.request.contextPath}/counter/AssignTask">← Back to room list</a>
        <br><br>
        <% if (housekeepers == null || housekeepers.isEmpty()) { %>
            <p>No housekeepers available.</p>
        <% } else { %>
            <form method="post" action="${pageContext.request.contextPath}/counter/AssignTask">
                <input type="hidden" name="roomId" value="<%= selectedRoom.getId() %>" />
                <table border="1" cellpadding="5">
                    <tr>
                        <th>Select</th>
                        <th>Name</th>
                        <th>Phone</th>
                    </tr>
                    <% for (User hk : housekeepers) { %>
                    <tr>
                        <td><input type="radio" name="housekeeperId" value="<%= hk.getId() %>" required /></td>
                        <td><%= hk.getName() %></td>
                        <td><%= hk.getPhone() %></td>
                    </tr>
                    <% } %>
                </table>
                <br>
                <input type="submit" value="Assign Task" />
            </form>
        <% } %>
    <% } %>
</body>
</html>
