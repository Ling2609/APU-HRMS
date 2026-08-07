<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>APU Hotel - Forgot Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>

<body>
    <div class="login-wrapper">
        <div class="login-box">

            <div style="text-align:center; margin-bottom:8px; font-size:22px; font-weight:bold; color:#1a237e;">
                Forgot Password
            </div>

            <p style="text-align:center; font-size:14px; color:#666; margin-bottom:22px; line-height:1.5;">
                Enter your registered email address.
                A verification code will be sent to your email.
            </p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form method="post" action="${pageContext.request.contextPath}/common/ForgotPassword">

                <div class="form-group">
                    <label>Email</label>
                    <input type="email"
                           name="email"
                           required
                           placeholder="Enter registered email" />
                </div>

                <button type="submit" class="btn-login">
                    Send Verification Code
                </button>
            </form>

            <div style="text-align:center; margin-top:15px;">
                <a href="${pageContext.request.contextPath}/common/login.jsp"
                   style="font-size:13px; color:#1a237e; text-decoration:none; font-weight:500;">
                    ← Back to Login
                </a>
            </div>

        </div>
    </div>
</body>
</html>