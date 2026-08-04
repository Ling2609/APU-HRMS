<%-- 
    Document   : login
    Created on : Aug 4, 2026, 11:47:01 AM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>APU Hotel - Login</title></head>
<body>
    <h2>APU Hotel Room Management System</h2>
    <hr>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form method="post" action="${pageContext.request.contextPath}/Login">
        <table>
            <tr>
                <td>Username:</td>
                <td><input type="text" name="name" required /></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" required /></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Login" /></td>
            </tr>
        </table>
    </form>
</body>
</html>