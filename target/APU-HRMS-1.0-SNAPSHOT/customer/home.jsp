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
<head><title>Customer Home</title></head>
<body>
    <h2>Customer Portal</h2>
    <p>Welcome, <%= user.getName() %></p>
    <hr>
    <ul>
        <li><a href="${pageContext.request.contextPath}/common/editProfile.jsp">Edit Profile</a></li>
        <li><a href="${pageContext.request.contextPath}/customer/MyBookings">My Bookings & Payment History</a></li>
        <li><a href="${pageContext.request.contextPath}/customer/WriteComment">Write Comment</a></li>
        <li><a href="${pageContext.request.contextPath}/customer/ViewComments">View My Comments</a></li>
    </ul>
    <hr>
    <a href="${pageContext.request.contextPath}/Logout">Logout</a>
</body>
</html>