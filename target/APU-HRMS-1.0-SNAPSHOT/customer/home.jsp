<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.CUSTOMER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer - Home</title>
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
            <h2>Welcome, <%= user.getName() %></h2>
            <p>Customer Portal</p>
        </div>
        <div class="card-grid" style="grid-template-columns: repeat(2, 1fr);">
            <a href="${pageContext.request.contextPath}/common/editProfile.jsp" class="card">
                <div class="card-icon">👤</div>
                <h3>Edit Profile</h3>
                <p>Update your personal information</p>
            </a>
            <a href="${pageContext.request.contextPath}/customer/MyBookings" class="card">
                <div class="card-icon">📋</div>
                <h3>My Bookings</h3>
                <p>View booking and payment history</p>
            </a>
            <a href="${pageContext.request.contextPath}/customer/WriteComment" class="card">
                <div class="card-icon">⭐</div>
                <h3>Write Comment</h3>
                <p>Rate and review your stay</p>
            </a>
            <a href="${pageContext.request.contextPath}/customer/ViewComments" class="card">
                <div class="card-icon">💬</div>
                <h3>View My Comments</h3>
                <p>View all your submitted comments</p>
            </a>
        </div>
    </div>
</body>
</html>