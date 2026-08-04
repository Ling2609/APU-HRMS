<%-- 
    Document   : editCustomer
    Created on : Aug 4, 2026, 12:34:30 PM
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
    User customer = (User) request.getAttribute("customer");
    if (customer == null) {
        response.sendRedirect(request.getContextPath() + "/counter/ManageCustomers");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head><title>Edit Customer</title></head>
<body>
    <h2>Edit Customer</h2>
    <a href="${pageContext.request.contextPath}/counter/ManageCustomers">Back to Customer List</a>
    <hr>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form method="post" action="${pageContext.request.contextPath}/counter/ManageCustomers">
        <input type="hidden" name="action" value="update" />
        <input type="hidden" name="id" value="<%= customer.getId() %>" />
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="name" value="<%= customer.getName() %>" required /></td>
            </tr>
            <tr>
                <td>Gender:</td>
                <td>
                    <select name="gender">
                        <option value="Male" <%= "Male".equals(customer.getGender()) ? "selected" : "" %>>Male</option>
                        <option value="Female" <%= "Female".equals(customer.getGender()) ? "selected" : "" %>>Female</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>IC/Identification:</td>
                <td><input type="text" name="identification" value="<%= customer.getIdentification() != null ? customer.getIdentification() : "" %>" required /></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone" value="<%= customer.getPhone() %>" required /></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="text" name="email" value="<%= customer.getEmail() != null ? customer.getEmail() : "" %>" /></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><textarea name="address"><%= customer.getAddress() != null ? customer.getAddress() : "" %></textarea></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Update Customer" /></td>
            </tr>
        </table>
    </form>
</body>
</html>
