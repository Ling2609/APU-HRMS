<%-- 
    Document   : manageCustomers
    Created on : Aug 4, 2026, 12:34:05 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.COUNTER_STAFF) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<User> customers = (List<User>) request.getAttribute("customers");
    String keyword = request.getParameter("keyword");
    if (keyword == null) keyword = "";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Customers</title>
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
        <div class="page-title">Manage Customers</div>

        <% if (request.getAttribute("success") != null) {%>
        <div class="msg-success"><%= request.getAttribute("success")%></div>
        <% } %>
        <% if (request.getAttribute("error") != null) {%>
        <div class="msg-error"><%= request.getAttribute("error")%></div>
        <% }%>

        <div class="search-bar">
            <form method="get" action="${pageContext.request.contextPath}/counter/ManageCustomers" style="display:flex; gap:10px;">
                <input type="text" name="keyword" value="<%= keyword%>" placeholder="Search by name or IC" />
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
            <a href="${pageContext.request.contextPath}/counter/registerCustomer.jsp" class="btn btn-gold">+ Register New Customer</a>
        </div>

        <% if (customers != null) { %>
            <% if (customers.isEmpty()) { %>
                <p>No customers found.</p>
            <% } else { %>
                <table class="data-table">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Gender</th>
                        <th>IC</th>
                        <th>Phone</th>
                        <th>Email</th>
                        <th>Address</th>
                        <th>Actions</th>
                    </tr>
                    <% for (User c : customers) { %>
                    <tr>
                        <td><%= c.getId() %></td>
                        <td><%= c.getName() %></td>
                        <td><%= c.getGender() != null ? c.getGender() : "" %></td>
                        <td><%= c.getIdentification() != null ? c.getIdentification() : "" %></td>
                        <td><%= c.getPhone() %></td>
                        <td><%= c.getEmail() != null ? c.getEmail() : "" %></td>
                        <td><%= c.getAddress() != null ? c.getAddress() : "" %></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/counter/ManageCustomers?action=edit&id=<%= c.getId() %>" class="action-link">Edit</a>
                            <a href="${pageContext.request.contextPath}/counter/ManageCustomers?action=delete&id=<%= c.getId() %>"
                               class="action-link-danger"
                               onclick="return confirm('Delete this customer?')">Delete</a>
                        </td>
                    </tr>
                    <% } %>
                </table>
            <% } %>
        <% } %>
    </div>
</body>
</html>