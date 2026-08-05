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
    <head>
        <title>Register Customer</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    </head>
    <body>
        <div class="navbar">
            <h1>APU Hotel</h1>
            <div class="nav-right">
                Welcome, <%= user.getName()%>
                <a href="${pageContext.request.contextPath}/counter/home.jsp">Home</a>
                <a href="${pageContext.request.contextPath}/Logout">Logout</a>
            </div>
        </div>
        <div class="container">
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #c9a84c; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Register Customer</div>
                <a href="${pageContext.request.contextPath}/counter/ManageCustomers" class="breadcrumb-link">← Manage Customers</a>
            </div>
            <% if (request.getAttribute("success") != null) {%>
            <div class="msg-success"><%= request.getAttribute("success")%></div>
            <% } %>
            <% if (request.getAttribute("error") != null) {%>
            <div class="msg-error"><%= request.getAttribute("error")%></div>
            <% }%>

            <div class="form-container">
                <form method="post" action="${pageContext.request.contextPath}/counter/RegisterCustomer">
                    <table class="form-table">
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
                            <td><input type="text" name="identification" required placeholder="12 digits" /></td>
                        </tr>
                        <tr>
                            <td>Phone:</td>
                            <td><input type="text" name="phone" required placeholder="10-11 digits" /></td>
                        </tr>
                        <tr>
                            <td>Email:</td>
                            <td><input type="text" name="email" placeholder="example@email.com" /></td>
                        </tr>
                        <tr>
                            <td>Address:</td>
                            <td><textarea name="address"></textarea></td>
                        </tr>
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-primary" style="width:200px;">Register Customer</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>