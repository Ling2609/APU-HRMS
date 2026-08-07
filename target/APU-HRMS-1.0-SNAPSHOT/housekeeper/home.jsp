<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.HOUSEKEEPER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Housekeeper Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    
    <jsp:include page="../housekeeper/navbar.jsp"/>
    
    <div class="container">
        <div class="welcome-box">
            <h2>Housekeeper Dashboard</h2>
            <p>Welcome back, <%= user.getName() %>. View your assigned tasks below.</p>
        </div>
        
        <div class="card-grid">
            <a href="${pageContext.request.contextPath}/common/editProfile.jsp" class="card">
                <div class="card-icon">👤</div>
                <h3>Edit Profile</h3>
                <p>Update your personal information</p>
            </a>
            <a href="${pageContext.request.contextPath}/housekeeper/ManageTask" class="card">
                <div class="card-icon">🧹</div>
                <h3>My Tasks</h3>
                <p>Complete assigned tasks, as well as view written feedback.</p>
            </a>
                
        </div>
    </div>
</body>
</html>