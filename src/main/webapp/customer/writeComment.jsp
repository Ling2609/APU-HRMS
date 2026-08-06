<%-- 
    Document   : writeComment
    Created on : Aug 4, 2026, 2:26:40 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Booking, java.util.List, java.util.Set" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.CUSTOMER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
    Booking selectedBooking = (Booking) request.getAttribute("selectedBooking");
    Set<Long> commentedIds = (Set<Long>) request.getAttribute("commentedIds");
    if (commentedIds == null) commentedIds = new java.util.HashSet<>();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Write Comment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
</head>
<body>
    <div class="navbar">
        <h1>APU Hotel</h1>
        <div class="nav-right">
            Welcome, <%= user.getName() %>
            <a href="${pageContext.request.contextPath}/customer/home.jsp">Home</a>
            <a href="${pageContext.request.contextPath}/Logout">Logout</a>
        </div>
    </div>
    <div class="container">

        <% if (selectedBooking == null) { %>
            <div class="page-title">Write Comment</div>
            
            <%-- Messages moved here for the list view --%>
            <% if (request.getAttribute("success") != null) { %>
                <div class="msg-success"><%= request.getAttribute("success") %></div>
            <% } %>
            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <br>
            <h3 style="color:#1a237e; margin-bottom:15px;">Select a Completed Booking to Comment</h3>
            <% if (bookings == null || bookings.isEmpty()) { %>
                <p>No completed bookings to comment on.</p>
            <% } else { %>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Booking ID</th>
                                <th>Room</th>
                                <th>Room Type</th>
                                <th>Check-out Date</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Booking b : bookings) { %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getCheckOutTime() != null ? b.getCheckOutTime().toLocalDate() : "-" %></td>
                                <td>
                                    <% if (commentedIds.contains(b.getId())) { %>
                                        <span style="color:#888;">Already Commented</span>
                                    <% } else { %>
                                        <a href="${pageContext.request.contextPath}/customer/WriteComment?action=select&bookingId=<%= b.getId() %>" class="action-link">
                                            Write Comment
                                        </a>
                                    <% } %>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>

        <% } else { %>
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #b8860b; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Write Comment</div>
                <a href="${pageContext.request.contextPath}/customer/WriteComment" class="breadcrumb-link">← Back to booking list</a>
            </div>

            <%-- Messages moved here for the form view --%>
            <% if (request.getAttribute("success") != null) { %>
                <div class="msg-success"><%= request.getAttribute("success") %></div>
            <% } %>
            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>

            <div class="form-container">
                <p style="color:#1a237e; font-weight:bold; margin-bottom:20px; padding-bottom:10px; border-bottom:2px solid #f0f0f0;">
                    Booking #<%= selectedBooking.getId() %> — Room <%= selectedBooking.getRoom().getRoomNumber() %>
                    (<%= selectedBooking.getRoom().getRoomType().getRoomTypeName() %>)
                </p>
                <form method="post" action="${pageContext.request.contextPath}/customer/WriteComment">
                    <input type="hidden" name="bookingId" value="<%= selectedBooking.getId() %>" />
                    <table class="form-table">
                        <tr>
                            <td>Rating:</td>
                            <td>
                                <select name="rating" required>
                                    <option value="">-- Select Rating --</option>
                                    <option value="1">1 ⭐ - Poor</option>
                                    <option value="2">2 ⭐ - Fair</option>
                                    <option value="3">3 ⭐ - Good</option>
                                    <option value="4">4 ⭐ - Very Good</option>
                                    <option value="5">5 ⭐ - Excellent</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>Comment:</td>
                            <td><textarea name="comment" rows="5" required placeholder="Share your experience..."></textarea></td>
                        </tr>
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-primary" style="width:200px;">Submit Comment</button>
                    </div>
                </form>
            </div>
        <% } %>
    </div>
</body>
</html>