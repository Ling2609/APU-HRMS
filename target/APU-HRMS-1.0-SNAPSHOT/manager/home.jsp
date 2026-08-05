<%-- 
    Document   : home
    Created on : Aug 4, 2026, 12:17:56 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manager Home</title>
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
            <h2>Manager Dashboard</h2>
            <p>Welcome back, <%= user.getName() %>. Manage your hotel below.</p>
        </div>
        <div class="card-grid">
            <a href="${pageContext.request.contextPath}/common/editProfile.jsp" class="card">
                <div class="card-icon">👤</div>
                <h3>Edit Profile</h3>
                <p>Update your personal information</p>
            </a>
            <div class="card" style="opacity:0.5; cursor:not-allowed;">
                <div class="card-icon">👥</div>
                <h3>Manage Staff</h3>
                <p>Coming soon</p>
            </div>
            <div class="card" style="opacity:0.5; cursor:not-allowed;">
                <div class="card-icon">📊</div>
                <h3>Reports</h3>
                <p>Coming soon</p>
            </div>
        </div>
    </div>
</body>
</html>