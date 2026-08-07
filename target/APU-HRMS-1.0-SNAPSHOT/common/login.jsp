<%-- 
    Document   : login
    Created on : Aug 4, 2026, 11:47:01 AM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>APU Hotel - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    <div class="login-wrapper">
        <div class="login-box">
            <div class="hotel-name">APU Hotel</div>
            <div class="hotel-sub">ROOM MANAGEMENT SYSTEM</div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <% if ("true".equals(request.getParameter("resetSuccess"))) { %>
            <div class="msg-success">
                Password reset successfully. Please log in with your new password.
            </div>
            <% }%>
            
            <form method="post" action="${pageContext.request.contextPath}/Login">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="name" required placeholder="Enter username" />
                </div>

                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" required placeholder="Enter password" />
                </div>

                <div style="text-align:right; margin-top:-4px; margin-bottom:14px;">
                    <a href="<%= request.getContextPath() %>/common/ForgotPassword"
                       style="font-size:13px; color:#1a237e; text-decoration:none; font-weight:500;">
                        Forgot Password?
                    </a>
                </div>

                <button type="submit" class="btn-login">Login</button>
            </form>
        </div>
    </div>
</body>
</html>