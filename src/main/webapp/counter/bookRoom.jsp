<%-- 
    Document   : bookRoom
    Created on : Aug 4, 2026, 12:50:30 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.RoomType, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.COUNTER_STAFF) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<User> customers = (List<User>) request.getAttribute("customers");
    List<RoomType> roomTypes = (List<RoomType>) request.getAttribute("roomTypes");
    User selectedCustomer = (User) request.getAttribute("selectedCustomer");
    String keyword = request.getParameter("keyword");
    if (keyword == null) keyword = "";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Book Room</title>
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

        <% if (selectedCustomer == null) { %>
            <%-- Step 1: Search customer --%>
            <div class="page-title">Book Room</div>
            <br>
            <div class="search-bar">
                <form method="get" action="${pageContext.request.contextPath}/counter/BookRoom" style="display:flex; gap:10px;">
                    <input type="text" name="keyword" value="<%= keyword %>" placeholder="Search by name or IC" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>
            <% if (customers != null) { %>
                <% if (customers.isEmpty()) { %>
                    <p>No customers found.</p>
                <% } else { %>
                    <div class="table-wrapper">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>IC</th>
                                    <th>Phone</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (User c : customers) { %>
                                <tr>
                                    <td><%= c.getName() %></td>
                                    <td><%= c.getIdentification() != null ? c.getIdentification() : "" %></td>
                                    <td><%= c.getPhone() %></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/counter/BookRoom?action=select&customerId=<%= c.getId() %>" class="action-link">Book for this customer</a>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } %>
            <% } %>

        <% } else { %>
            <%-- Step 2: Booking form --%>
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #b8860b; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Book Room</div>
                <a href="${pageContext.request.contextPath}/counter/BookRoom" class="breadcrumb-link">← Back to customer list</a>
            </div>
            
            <% if (request.getAttribute("warning") != null) { %>
                <div class="msg-warning"><%= request.getAttribute("warning") %></div>
                <form method="post" action="${pageContext.request.contextPath}/counter/BookRoom">
                    <input type="hidden" name="customerId" value="<%= request.getAttribute("customerId") %>" />
                    <input type="hidden" name="roomTypeId" value="<%= request.getAttribute("roomTypeId") %>" />
                    <input type="hidden" name="checkInDate" value="<%= request.getAttribute("checkInDate") %>" />
                    <input type="hidden" name="checkOutDate" value="<%= request.getAttribute("checkOutDate") %>" />
                    <input type="hidden" name="confirmed" value="true" />
                    <div style="margin-top:15px;">
                        <button type="submit" class="btn btn-gold">Yes, Book Anyway</button>
                        &nbsp;
                        <a href="${pageContext.request.contextPath}/counter/BookRoom" class="btn btn-primary">Cancel</a>
                    </div>
                </form>
            <% } else { %>
                <div class="form-container">
                    <p style="color:#1a237e; font-weight:bold; margin-bottom:20px; padding-bottom:10px; border-bottom:2px solid #f0f0f0;">
                        Booking for: <%= selectedCustomer.getName() %> (IC: <%= selectedCustomer.getIdentification() %>)
                    </p>
                    <form method="post" action="${pageContext.request.contextPath}/counter/BookRoom">
                        <input type="hidden" name="customerId" value="<%= selectedCustomer.getId() %>" />
                        <table class="form-table">
                            <tr>
                                <td>Room Type:</td>
                                <td>
                                    <select name="roomTypeId" required>
                                        <option value="">-- Select Room Type --</option>
                                        <% if (roomTypes != null) { for (RoomType rt : roomTypes) { %>
                                            <option value="<%= rt.getId() %>">
                                                <%= rt.getRoomTypeName() %> - RM<%= rt.getRoomTypePrice() %>/night
                                            </option>
                                        <% } } %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>Estimated Check-in Date:</td>
                                <td><input type="date" name="checkInDate" required /></td>
                            </tr>
                            <tr>
                                <td>Estimated Check-out Date:</td>
                                <td><input type="date" name="checkOutDate" required /></td>
                            </tr>
                        </table>
                        <div style="text-align:center; margin-top:20px;">
                            <button type="submit" class="btn btn-primary" style="width:200px;">Book Room</button>
                        </div>
                    </form>
                </div>
            <% } %>
        <% } %>
    </div>
</body>
</html>