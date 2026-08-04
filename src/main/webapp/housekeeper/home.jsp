<%-- 
    Document   : home
    Created on : Aug 4, 2026, 12:18:17 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.HOUSEKEEPER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head><title>Housekeeper Home</title></head>
<body>
    <h2>Housekeeper Dashboard</h2>
    <p>Welcome, <%= user.getName() %></p>
    <ul>
        <li><a href="${pageContext.request.contextPath}/common/editProfile.jsp">Edit Profile</a></li>
    </ul>
    <hr>
    <a href="${pageContext.request.contextPath}/Logout">Logout</a>
</body>
</html>
