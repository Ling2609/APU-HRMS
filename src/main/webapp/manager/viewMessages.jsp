<%@page import="entity.Message"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Staff, java.util.ArrayList" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    ArrayList<Message> messageList = (ArrayList<Message>)request.getAttribute("messageList");
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Messages</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    </head>
        
    <body>
        
        <jsp:include page="../manager/navbar.jsp"/>
        
        <div class="container">
            
            <div class="page-title">All Messages</div>
            
            <br>
                                    
            <% if (messageList.isEmpty()) { %>
            
            <p>No messages found.</p>
                
            <% } else { %>
            
                <div class="table-wrapper">

                <table class="data-table">

                    <tr>
                        <th>No</th>
                        <th>Booking No.</th>
                        <th>User ID</th>
                        <th>User Name</th>
                        <th>Message Type</th>
                        <th>Rating</th>
                        <th>Message</th>
                    </tr>

                <% for(Message message : messageList) { %>

                    <tr>
                        <td><span><%= messageList.indexOf(message) %></span></td>
                        <td><span>Booking #<%= message.getBookingUser().getBooking().getId() %></span></td>
                        <td><span>User #<%= message.getBookingUser().getUser().getId() %></span></td>
                        <td><span><%= message.getBookingUser().getUser().getName() %></span></td>
                        <td><span><%= message.getMessageType() %></span></td>

                        <% if(message.getRating() == null) { %>
                        <td><span>-</span></td>
                        <% } else { %>
                        <td><span><%= message.getRating()%></span></td>
                        <% } %>

                        <td><span><%= message.getMessageContent() %></span></td>
                    </tr>

                <% } %>

                </table>

            </div>
                
            <% } %>
            
        </div>
        
    </body>
    
</html>
