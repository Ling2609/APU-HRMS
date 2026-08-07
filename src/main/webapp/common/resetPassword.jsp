<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Reset Password</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/common/style.css" />
</head>

<body>
<div class="login-wrapper">
    <div class="login-box">

        <div style="text-align:center; margin-bottom:8px; font-size:22px; font-weight:bold; color:#1a237e;">
            Reset Password
        </div>

        <p style="text-align:center; font-size:14px; color:#666; margin-bottom:22px;">
            Enter your new password below.
        </p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="msg-error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form method="post"
              action="${pageContext.request.contextPath}/common/ResetPassword">

            <div class="form-group">
                <label>New Password</label>
                <input type="password"
                       name="password"
                       minlength="6"
                       required
                       placeholder="Enter new password" />
            </div>

            <div class="form-group">
                <label>Confirm Password</label>
                <input type="password"
                       name="confirmPassword"
                       minlength="6"
                       required
                       placeholder="Confirm new password" />
            </div>

            <button type="submit" class="btn-login">
                Reset Password
            </button>

        </form>

    </div>
</div>
</body>
</html>