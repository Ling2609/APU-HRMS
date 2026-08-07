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

        <jsp:include page="../manager/navbar.jsp"/>

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
                <a href="${pageContext.request.contextPath}/manager/ManageStaff" class="card">
                    <div class="card-icon">👥</div>
                    <h3>Manage Staff</h3>
                    <p>Register, edit or delete staff profiles.</p>
                </a>
                <a href="${pageContext.request.contextPath}/manager/ManageReport" class="card">
                    <div class="card-icon">📊</div>
                    <h3>Reports</h3>
                    <p>Generate reports.</p>
                </a>
                <a href="${pageContext.request.contextPath}/manager/ManageRoomPrice" class="card">
                    <div class="card-icon">$</div>
                    <h3>Set Price</h3>
                    <p>Set prices for different room types.</p>
                </a>
                <a href="${pageContext.request.contextPath}/manager/ViewMessages" class="card">
                    <div class="card-icon">💬</div>
                    <h3>View Messages</h3>
                    <p>View both feedback from housekeepers and comments from customers.</p>
                </a>
            </div>
        </div>
                    
    </body>
    
</html>