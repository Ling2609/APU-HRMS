<%-- 
    Document   : registerCustomer
    Created on : Aug 4, 2026, 12:29:18 PM
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
<head><title>Register Customer</title></head>
<body>
    <h2>Register Customer</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form method="post" action="${pageContext.request.contextPath}/counter/RegisterCustomer">
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="name" required /></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" required /></td>
            </tr>
            <tr>
                <td>Gender:</td>
                <td>
                    <select name="gender">
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>IC/Identification:</td>
                <td><input type="text" name="identification" required /></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone" required /></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="text" name="email" /></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><textarea name="address"></textarea></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Register Customer" /></td>
            </tr>
        </table>
    </form>
</body>
</html>