<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Booking, java.util.List, java.util.Set" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.HOUSEKEEPER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    Booking selectedBooking = (Booking)request.getAttribute("selectedBooking");
    
%>
<!DOCTYPE html>
<html>
<head>
    <title>Write Comment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    
    <jsp:include page="../housekeeper/navbar.jsp"/>

    <div class="container">
        
        
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="msg-success"><%= request.getAttribute("success") %></div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="msg-error"><%= request.getAttribute("error") %></div>
        <% } %>

        
        
        <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #b8860b; padding-bottom: 8px; margin-bottom: 20px;">
            <div class="page-title" style="border:none; margin:0; padding:0;">Write Feedback</div>
            <a href="${pageContext.request.contextPath}/housekeeper/ManageTask" class="breadcrumb-link">← Manage Tasks</a>
        </div>

        <div class="form-container">
            
            <form method="post" action="${pageContext.request.contextPath}/housekeeper/WriteFeedback">
                
                <input type="hidden" name="bookingId" value="<%= selectedBooking.getId() %>" 

                <table class="form-table">
                    
                    <tr>
                        <td>Booking ID: </td>
                        <td><%= selectedBooking.getId() %>
                    </tr>
                        
                    <tr>
                        <td>Room Number: </td>
                        <td><%= selectedBooking.getRoom().getRoomNumber() %>
                    </tr>
                    
                    <tr>
                        <td>Room Type: </td>
                        <td><%= selectedBooking.getRoom().getRoomType().getRoomTypeName() %>
                    </tr>
                    
                    <tr>
                        <td>Check Out Time: </td>
                        <td><%= selectedBooking.getCheckOutTime() %>
                    </tr>
                        
                    <tr>
                        <td>Feedback: </td>
                        <td><textarea name="feedback" rows="5" required placeholder="Share your feedback..."></textarea></td>
                    </tr>
                    
                </table>
                
                <div style="text-align:center; margin-top:20px;">
                    <button type="submit" class="btn btn-primary" style="width:200px;">Submit Comment</button>
                </div>

            </form>

        </div>  

    </div>
    
</body>

</html>