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
<head>
    <title>Counter Staff - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    <div class="navbar">
        <h1>APU Hotel</h1>
        <div class="nav-right">
            Welcome, <%= user.getName() %>
            <a href="${pageContext.request.contextPath}/Logout">Logout</a>
        </div>
    </div>
    <div class="container">
        <div class="welcome-box">
            <h2>Counter Staff Dashboard</h2>
            <p>Welcome back, <%= user.getName() %>. Manage bookings and customers below.</p>
        </div>
        <div class="card-grid">
            <a href="${pageContext.request.contextPath}/common/editProfile.jsp" class="card">
                <div class="card-icon">👤</div>
                <h3>Edit Profile</h3>
                <p>Update your personal information</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/registerCustomer.jsp" class="card">
                <div class="card-icon">➕</div>
                <h3>Register Customer</h3>
                <p>Create a new customer account</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/ManageCustomers" class="card">
                <div class="card-icon">👥</div>
                <h3>Manage Customers</h3>
                <p>Search, update, delete customers</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/BookRoom" class="card">
                <div class="card-icon">🛏️</div>
                <h3>Book Room</h3>
                <p>Make a booking for a customer</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/ViewBookings" class="card">
                <div class="card-icon">📋</div>
                <h3>View All Bookings</h3>
                <p>View all booking records</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/Receipt" class="card">
                <div class="card-icon">💰</div>
                <h3>Collect Payment</h3>
                <p>Generate receipt and collect payment</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/CheckIn" class="card">
                <div class="card-icon">✅</div>
                <h3>Check In</h3>
                <p>Process customer check-in</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/CheckOut" class="card">
                <div class="card-icon">🚪</div>
                <h3>Check Out</h3>
                <p>Process customer check-out</p>
            </a>
            <a href="${pageContext.request.contextPath}/counter/AssignTask" class="card">
                <div class="card-icon">🧹</div>
                <h3>Assign Cleaning Task</h3>
                <p>Assign housekeeper to clean room</p>
            </a>
        </div>
    </div>
</body>
</html>