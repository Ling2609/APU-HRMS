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
<head><title>Manage Customers</title></head>
<body>
    <h2>Manage Customers</h2>
    <a href="${pageContext.request.contextPath}/counter/home.jsp">Back to Home</a>
    <hr>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <a href="${pageContext.request.contextPath}/counter/registerCustomer.jsp">+ Register New Customer</a>
    <br><br>
    
    <h3>Search Customers</h3>
    <form method="get" action="${pageContext.request.contextPath}/counter/ManageCustomers">
        <input type="text" name="keyword" value="<%= keyword %>" placeholder="Search by name or IC" />
        <input type="submit" value="Search" />
    </form>
    <br>
    <% if (customers != null) { %>
        <% if (customers.isEmpty()) { %>
            <p>No customers found.</p>
        <% } else { %>
            <table border="1" cellpadding="5">
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
                        <a href="${pageContext.request.contextPath}/counter/ManageCustomers?action=edit&id=<%= c.getId() %>">Edit</a>
                        &nbsp;|&nbsp;
                        <a href="${pageContext.request.contextPath}/counter/ManageCustomers?action=delete&id=<%= c.getId() %>"
                           onclick="return confirm('Delete this customer?')">Delete</a>
                    </td>
                </tr>
                <% } %>
            </table>
        <% } %>
    <% } %>
</body>
</html>