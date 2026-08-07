<%@page import="entity.Message"%>
<%@page import="entity.Room"%>
<%@page import="entity.Booking"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Staff, java.util.ArrayList" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.HOUSEKEEPER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    ArrayList<Booking> bookingList = (ArrayList<Booking>)request.getAttribute("bookingList");
    ArrayList<Message> feedbackList = (ArrayList<Message>)request.getAttribute("feedbackList");
    
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Staff</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    </head>
        
    <body>
        
        <jsp:include page="../housekeeper/navbar.jsp"/>
        
        <div class="container">
            
            <div class="page-title">Manage Tasks</div>            
            <br>
            
            
            
            <% if (request.getAttribute("success") != null) { %>
            <div class="msg-success"><%= request.getAttribute("success") %></div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>
            
            
                        
            <% if (bookingList.isEmpty()) { %>
            
                <p>No tasks found.</p>
                
            <% } else { %>
            
                <div class="table-wrapper">
                    
                    <table class="data-table">
                        
                        <colgroup>
                            <col class="bookingId">
                            <col class="room">
                            <col class="roomType">
                            <col class="checkOutTime">
                            <col class="feedback">
                            <col class="action-col" colspan="2">
                        </colgroup>
                        
                        <thead>
                            <tr>
                                <th>Booking ID</th>
                                <th>Room Number</th>
                                <th>Room Type</th>
                                <th>Check Out Time</th>
                                <th>Feedback</th>
                                <th colspan="2">Action</th>
                            </tr>
                        </thead>
                        
                        <tbody>
                            
                            <% for (Booking booking : bookingList) { %>
                            <tr>
                                <td><%= booking.getId() %></td>
                                <td><%= booking.getRoom().getRoomNumber() %></td>
                                <td><%= booking.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= booking.getCheckOutTime() %></td>
                                
                                <td>
                                <% for(Message message : feedbackList) { %>
                                    <% if(message.getBookingUser().getBooking().getId() == booking.getId()) { %>
                                        <%= message.getMessageContent() %>
                                    <% } %>
                                <% } %>
                                </td>
                                <td>
                                    <% if (booking.getBookingStatus() == Booking.BookingStatus.CHECKED_OUT 
                                            && booking.getRoom().getRoomStatus() == Room.RoomStatus.CLEANING) { %>
                                        <a href="${pageContext.request.contextPath}/housekeeper/CompleteTask?action=complete&bookingID=<%= booking.getId()%>" 
                                            class="action-link">
                                            Complete
                                        </a>
                                    <% } else { %>
                                        <span style="color:#888;">Already Completed</span>
                                    <% } %>
                                </td>
                                
                                <td>
                                    <% if(booking.getBookingStatus() == Booking.BookingStatus.CHECKED_OUT 
                                            && booking.getRoom().getRoomStatus() == Room.RoomStatus.CLEANING) { %>
                                            <span style="color:#888;">Task Not Completed</span>
                                    <% } %>
                                    
                                    <% if(booking.getBookingStatus() == Booking.BookingStatus.CHECKED_OUT 
                                        && booking.getRoom().getRoomStatus() == Room.RoomStatus.FREE ) { %>
                                        
                                        <% boolean commented = false; %>

                                        <% for(Message message : feedbackList) { %>
                                            <% if(message.getBookingUser().getBooking().getId() == booking.getId()) { %>
                                                <span style="color:#888;">Already Written</span>
                                            <% commented = true; } %>
                                        <% } %>
                                        
                                        <% if(!commented) { %>
                                            <a href="${pageContext.request.contextPath}/housekeeper/WriteFeedback?action=delete&bookingID=<%= booking.getId()%>" 
                                                class="action-link">
                                                Leave Feedback
                                             </a>   
                                        <% } %>

                                    <% } %>
                                    
                                </td>
                                
                            </tr>
                            
                            <% } %>
                            
                        </tbody>
                        
                    </table>

                </div>
                            
            <% } %>
            
        </div>
        
    </body>
    
</html>
