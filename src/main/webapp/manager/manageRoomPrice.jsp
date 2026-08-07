<%@page import="entity.RoomType"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Staff, java.util.ArrayList" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    ArrayList<RoomType> roomTypeList = (ArrayList<RoomType>)request.getAttribute("roomTypeList");
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Room Price</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    </head>
        
    <body>
        
        <jsp:include page="../manager/navbar.jsp"/>
        
        <div class="container">
            
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #c9a84c; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Set Room Price</div>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <% if (request.getAttribute("success") != null) { %>
            
            <div class="msg-success"><%= request.getAttribute("success") %></div>
            
            <% } %>
            
            <div class="form-container">
                
                <form method="post" action="${pageContext.request.contextPath}/manager/ManageRoomPrice">
                           
                    <table class="form-table">
                        
                        <tr>
                            <th>Room Type</th>
                            <th>Room Price</th>
                        </tr>
                        
                        <% for(RoomType type : roomTypeList) { %>
                        
                            <tr>
                                <td><%= type.getRoomTypeName() %></td>
                                <td><input type="text" name="<%= type.getRoomTypeName()%>" value="<%= type.getRoomTypePrice() %>" required /></td>
                            </tr>
                        
                        <% } %>
                        
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-primary" style="width:200px;">Update Prices</button>
                    </div>
                </form>
            </div>
                        
        </div>
        
    </body>
    
</html>
