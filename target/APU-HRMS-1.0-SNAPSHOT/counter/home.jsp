<%-- 
    Document   : home
    Created on : Aug 4, 2026, 12:16:24 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.COUNTER_STAFF) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head><title>Counter Staff Home</title></head>
<body>
    <h2>Counter Staff Dashboard</h2>
    <p>Welcome, <%= user.getName() %></p>
    <hr>
    <ul>
        <li><a href="${pageContext.request.contextPath}/common/editProfile.jsp">Edit Profile</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/registerCustomer.jsp">Register Customer</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/ManageCustomers">Manage Customers</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/BookRoom">Book Room</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/ViewBookings">View All Bookings</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/Receipt">Generate Receipt & Collect Payment</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/CheckIn">Check In</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/CheckOut">Check Out</a></li>
        <li><a href="${pageContext.request.contextPath}/counter/AssignTask">Assign Cleaning Task</a></li>
    </ul>
    <hr>
    <a href="${pageContext.request.contextPath}/Logout">Logout</a>
</body>
</html>