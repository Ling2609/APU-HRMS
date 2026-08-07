<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Verify Email</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/common/style.css" />
</head>

<body>
<div class="login-wrapper">
    <div class="login-box">

        <div style="text-align:center; margin-bottom:8px; font-size:22px; font-weight:bold; color:#1a237e;">
            Verify Email
        </div>

        <p style="text-align:center; font-size:14px; color:#666; margin-bottom:22px; line-height:1.5;">
            Enter the 6-digit verification code sent to your registered email.
        </p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="msg-error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form method="post"
              action="${pageContext.request.contextPath}/common/VerifyResetCode">

            <div class="form-group">
                <label>Verification Code</label>
                <input type="text"
                       name="code"
                       maxlength="6"
                       pattern="[0-9]{6}"
                       required
                       placeholder="Enter 6-digit code" />
            </div>

            <button type="submit" class="btn-login">
                Verify Code
            </button>
        </form>

        <div style="text-align:center; margin-top:15px;">
            <a href="${pageContext.request.contextPath}/common/ForgotPassword"
               style="font-size:13px; color:#1a237e; text-decoration:none; font-weight:500;">
                ← Request New Code
            </a>
        </div>

    </div>
</div>
</body>
</html>