<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="entity.User"%>

<% 
    User user = (User) session.getAttribute("user"); 
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    
    <body>
        <div class="navbar">
            <h1>APU Hotel</h1>
            <div class="nav-right">
                Welcome, <%= user.getName() %>
                <a href="${pageContext.request.contextPath}/manager/home.jsp">Home</a>
                <a href="${pageContext.request.contextPath}/Logout">Logout</a>
            </div>
        </div>
    </body>
    
</html>
