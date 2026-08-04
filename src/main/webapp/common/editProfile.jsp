<%-- 
    Document   : editProfile
    Created on : Aug 4, 2026, 2:16:16 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    // Determine back link based on role
    String homeLink = "";
    switch (user.getRole()) {
        case MANAGER:      homeLink = request.getContextPath() + "/manager/home.jsp"; break;
        case COUNTER_STAFF: homeLink = request.getContextPath() + "/counter/home.jsp"; break;
        case HOUSEKEEPER:  homeLink = request.getContextPath() + "/housekeeper/home.jsp"; break;
        case CUSTOMER:     homeLink = request.getContextPath() + "/customer/home.jsp"; break;
    }
%>
<!DOCTYPE html>
<html>
<head><title>Edit Profile</title></head>
<body>
    <h2>Edit Profile</h2>
    <a href="<%= homeLink %>">Back to Home</a>
    <hr>

    <%-- Profile update section --%>
    <h3>Update Profile</h3>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form method="post" action="${pageContext.request.contextPath}/common/EditProfile">
        <input type="hidden" name="action" value="updateProfile" />
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="name" value="<%= user.getName() %>" required /></td>
            </tr>
            <tr>
                <td>Gender:</td>
                <td>
                    <select name="gender">
                        <option value="Male" <%= "Male".equals(user.getGender()) ? "selected" : "" %>>Male</option>
                        <option value="Female" <%= "Female".equals(user.getGender()) ? "selected" : "" %>>Female</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>IC/Identification:</td>
                <td><input type="text" name="identification" value="<%= user.getIdentification() != null ? user.getIdentification() : "" %>" /></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone" value="<%= user.getPhone() %>" /></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="text" name="email" value="<%= user.getEmail() != null ? user.getEmail() : "" %>" /></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><textarea name="address"><%= user.getAddress() != null ? user.getAddress() : "" %></textarea></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Update Profile" /></td>
            </tr>
        </table>
    </form>

    <hr>

    <%-- Change password section --%>
    <h3>Change Password</h3>
    <% if (request.getAttribute("passwordSuccess") != null) { %>
        <p style="color:green;"><%= request.getAttribute("passwordSuccess") %></p>
    <% } %>
    <% if (request.getAttribute("passwordError") != null) { %>
        <p style="color:red;"><%= request.getAttribute("passwordError") %></p>
    <% } %>
    <form method="post" action="${pageContext.request.contextPath}/common/EditProfile">
        <input type="hidden" name="action" value="changePassword" />
        <table>
            <tr>
                <td>Current Password:</td>
                <td><input type="password" name="currentPassword" required /></td>
            </tr>
            <tr>
                <td>New Password:</td>
                <td><input type="password" name="newPassword" required /></td>
            </tr>
            <tr>
                <td>Confirm New Password:</td>
                <td><input type="password" name="confirmPassword" required /></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Change Password" /></td>
            </tr>
        </table>
    </form>
</body>
</html>
