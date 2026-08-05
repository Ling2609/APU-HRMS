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
    String homeLink = "";
    switch (user.getRole()) {
        case MANAGER:       homeLink = request.getContextPath() + "/manager/home.jsp"; break;
        case COUNTER_STAFF: homeLink = request.getContextPath() + "/counter/home.jsp"; break;
        case HOUSEKEEPER:   homeLink = request.getContextPath() + "/housekeeper/home.jsp"; break;
        case CUSTOMER:      homeLink = request.getContextPath() + "/customer/home.jsp"; break;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    <div class="navbar">
        <h1>APU Hotel</h1>
        <div class="nav-right">
            Welcome, <%= user.getName() %>
            <a href="<%= homeLink %>">Home</a>
            <a href="${pageContext.request.contextPath}/Logout">Logout</a>
        </div>
    </div>
    <div class="container">
        <div class="page-title" id="page-title">Edit Profile</div>
        <br>

        <%-- Tab buttons --%>
        <div style="display:flex; gap:10px; margin-bottom:20px;">
            <button onclick="showTab('profile')" id="tab-profile" class="btn btn-primary">Update Profile</button>
            <button onclick="showTab('password')" id="tab-password" class="btn" style="background:#e0e0e0; color:#333;">Change Password</button>
        </div>

        <%-- Update Profile Tab --%>
        <div id="section-profile">
            <% if (request.getAttribute("success") != null) { %>
                <div class="msg-success"><%= request.getAttribute("success") %></div>
            <% } %>
            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>
            <div class="form-container">
                <form method="post" action="${pageContext.request.contextPath}/common/EditProfile">
                    <input type="hidden" name="action" value="updateProfile" />
                    <table class="form-table">
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
                            <td><input type="text" name="identification" value="<%= user.getIdentification() != null ? user.getIdentification() : "" %>" placeholder="12 digits" /></td>
                        </tr>
                        <tr>
                            <td>Phone:</td>
                            <td><input type="text" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>" placeholder="10-11 digits" /></td>
                        </tr>
                        <tr>
                            <td>Email:</td>
                            <td><input type="text" name="email" value="<%= user.getEmail() != null ? user.getEmail() : "" %>" placeholder="example@email.com" /></td>
                        </tr>
                        <tr>
                            <td>Address:</td>
                            <td><textarea name="address"><%= user.getAddress() != null ? user.getAddress() : "" %></textarea></td>
                        </tr>
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-primary" style="width:200px;">Update Profile</button>
                    </div>
                </form>
            </div>
        </div>

        <%-- Change Password Tab --%>
        <div id="section-password" style="display:none;">
            <% if (request.getAttribute("passwordSuccess") != null) { %>
                <div class="msg-success"><%= request.getAttribute("passwordSuccess") %></div>
            <% } %>
            <% if (request.getAttribute("passwordError") != null) { %>
                <div class="msg-error"><%= request.getAttribute("passwordError") %></div>
            <% } %>
            <div class="form-container">
                <form method="post" action="${pageContext.request.contextPath}/common/EditProfile">
                    <input type="hidden" name="action" value="changePassword" />
                    <table class="form-table">
                        <tr>
                            <td>Current Password:</td>
                            <td><input type="password" name="currentPassword" required /></td>
                        </tr>
                        <tr>
                            <td>New Password:</td>
                            <td><input type="password" name="newPassword" required /></td>
                        </tr>
                        <tr>
                            <td>Confirm Password:</td>
                            <td><input type="password" name="confirmPassword" required /></td>
                        </tr>
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-gold" style="width:200px;">Change Password</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function showTab(tab) {
            document.getElementById('section-profile').style.display = tab === 'profile' ? 'block' : 'none';
            document.getElementById('section-password').style.display = tab === 'password' ? 'block' : 'none';
            document.getElementById('tab-profile').className = tab === 'profile' ? 'btn btn-primary' : 'btn';
            document.getElementById('tab-profile').style.background = tab === 'profile' ? '' : '#e0e0e0';
            document.getElementById('tab-profile').style.color = tab === 'profile' ? '' : '#333';
            document.getElementById('tab-password').className = tab === 'password' ? 'btn btn-gold' : 'btn';
            document.getElementById('tab-password').style.background = tab === 'password' ? '' : '#e0e0e0';
            document.getElementById('tab-password').style.color = tab === 'password' ? '' : '#333';
            document.getElementById('page-title').innerText = tab === 'profile' ? 'Edit Profile' : 'Change Password';
        }

        <% if (request.getAttribute("passwordSuccess") != null || request.getAttribute("passwordError") != null) { %>
            showTab('password');
        <% } %>
    </script>
</body>
</html>