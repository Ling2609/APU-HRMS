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
<head><title>Book Room</title></head>
<body>
    <h2>Book Room</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <% if (selectedCustomer == null) { %>
        <%-- Step 1: Search customer --%>
        <h3>Search Customer</h3>
        <form method="get" action="${pageContext.request.contextPath}/counter/BookRoom">
            <input type="text" name="keyword" value="<%= keyword %>" placeholder="Search by name or IC" />
            <input type="submit" value="Search" />
            <a href="${pageContext.request.contextPath}/counter/BookRoom?showAll=true">Show All</a>
        </form>
        <br>
        <% if (customers != null) { %>
            <% if (customers.isEmpty()) { %>
                <p>No customers found.</p>
            <% } else { %>
                <table border="1" cellpadding="5">
                    <tr>
                        <th>Name</th>
                        <th>IC</th>
                        <th>Phone</th>
                        <th>Action</th>
                    </tr>
                    <% for (User c : customers) { %>
                    <tr>
                        <td><%= c.getName() %></td>
                        <td><%= c.getIdentification() != null ? c.getIdentification() : "" %></td>
                        <td><%= c.getPhone() %></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/counter/BookRoom?action=select&customerId=<%= c.getId() %>">
                                Book for this customer
                            </a>
                        </td>
                    </tr>
                    <% } %>
                </table>
            <% } %>
        <% } %>

    <% } else { %>
        <%-- Step 2: Booking form for selected customer --%>
        <h3>Booking for: <%= selectedCustomer.getName() %> (IC: <%= selectedCustomer.getIdentification() %>)</h3>
        <a href="${pageContext.request.contextPath}/counter/BookRoom">← Back to customer search</a>
        <br><br>
        <form method="post" action="${pageContext.request.contextPath}/counter/BookRoom">
            <input type="hidden" name="customerId" value="<%= selectedCustomer.getId() %>" />
            <table>
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
                <tr>
                    <td></td>
                    <td><input type="submit" value="Book Room" /></td>
                </tr>
            </table>
        </form>
    <% } %>
</body>
</html>
