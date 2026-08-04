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
<head><title>Manager Home</title></head>
<body>
    <h2>Manager Dashboard</h2>
    <p>Welcome, <%= user.getName() %></p>
    <ul>
        <li><a href="${pageContext.request.contextPath}/common/editProfile.jsp">Edit Profile</a></li>
    </ul>
    <hr>
    <a href="${pageContext.request.contextPath}/Logout">Logout</a>
</body>
</html>