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
<head>
    <title>Assign Cleaning Task</title>
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
        <% if (request.getAttribute("success") != null) { %>
            <div class="msg-success"><%= request.getAttribute("success") %></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="msg-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <% if (selectedRoom == null) { %>
            <div class="page-title">Assign Cleaning Task</div>
            <br>
            <h3 style="color:#1a237e; margin-bottom:15px;">Rooms Requiring Cleaning</h3>
            <% if (cleaningRooms == null || cleaningRooms.isEmpty()) { %>
                <p>No rooms require cleaning at the moment.</p>
            <% } else { %>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Room Number</th>
                                <th>Floor</th>
                                <th>Room Type</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Room r : cleaningRooms) { %>
                            <tr>
                                <td><%= r.getRoomNumber() %></td>
                                <td><%= r.getRoomNumber() / 1000 %></td>
                                <td><%= r.getRoomType().getRoomTypeName() %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/counter/AssignTask?action=select&roomId=<%= r.getId() %>" class="action-link">
                                        Assign Housekeeper
                                    </a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>

        <% } else { %>
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #b8860b; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Assign Cleaning Task</div>
                <a href="${pageContext.request.contextPath}/counter/AssignTask" class="breadcrumb-link">← Back to room list</a>
            </div>

            <div class="form-container">
                <p style="color:#1a237e; font-weight:bold; margin-bottom:20px; padding-bottom:10px; border-bottom:2px solid #f0f0f0;">
                    Room <%= selectedRoom.getRoomNumber() %> — <%= selectedRoom.getRoomType().getRoomTypeName() %>
                </p>
                <% if (housekeepers == null || housekeepers.isEmpty()) { %>
                    <p>No housekeepers available.</p>
                <% } else { %>
                    <form method="post" action="${pageContext.request.contextPath}/counter/AssignTask">
                        <input type="hidden" name="roomId" value="<%= selectedRoom.getId() %>" />
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Select</th>
                                    <th>Name</th>
                                    <th>Phone</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (User hk : housekeepers) { %>
                                <tr>
                                    <td><input type="radio" name="housekeeperId" value="<%= hk.getId() %>" required /></td>
                                    <td><%= hk.getName() %></td>
                                    <td><%= hk.getPhone() %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <div style="text-align:center; margin-top:20px;">
                            <button type="submit" class="btn btn-primary" style="width:200px;">Assign Task</button>
                        </div>
                    </form>
                <% } %>
            </div>
        <% } %>
    </div>
</body>
</html>