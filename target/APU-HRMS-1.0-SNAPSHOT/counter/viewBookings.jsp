<%-- 
    Document   : viewBookings
    Created on : Aug 4, 2026, 1:17:04 PM
    Author     : Ling
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Booking, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.COUNTER_STAFF) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>
<!DOCTYPE html>
<html>
<head>
    <title>All Bookings</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    <style>
        .tab-btn {
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 13px;
            background: #e0e0e0;
            color: #333;
            margin-right: 5px;
            margin-bottom: 10px;
        }
        .tab-btn.active { background: #1a237e; color: #ffffff; }
        .tab-content { display: none; }
        .tab-content.active { display: block; }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>APU Hotel</h1>
        <div class="nav-right">
            Welcome, <%= user.getName() %>
            <a href="${pageContext.request.contextPath}/counter/home.jsp">Home</a>
            <a href="${pageContext.request.contextPath}/Logout">Logout</a>
        </div>
    </div>
    <div class="container">
        <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #b8860b; padding-bottom: 8px; margin-bottom: 20px;">
            <div class="page-title" style="border:none; margin:0; padding:0;">All Bookings</div>
            <a href="${pageContext.request.contextPath}/counter/BookRoom" class="btn btn-gold">+ New Booking</a>
        </div>

        <% if (request.getAttribute("success") != null) { %>
            <div class="msg-success"><%= request.getAttribute("success") %></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="msg-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <% if (bookings == null || bookings.isEmpty()) { %>
            <p>No bookings found.</p>
        <% } else {
            int unpaidCount = 0, bookedCount = 0, checkedInCount = 0,
                lateCount = 0, checkedOutCount = 0;
            for (Booking b : bookings) {
                switch (b.getBookingStatus()) {
                    case UNPAID: unpaidCount++; break;
                    case BOOKED: bookedCount++; break;
                    case CHECKED_IN: checkedInCount++; break;
                    case LATE: lateCount++; break;
                    case CHECKED_OUT: checkedOutCount++; break;
                }
            }
        %>

        <div>
            <button class="tab-btn active" onclick="showTab('all', this)">All (<%= bookings.size() %>)</button>
            <button class="tab-btn" onclick="showTab('unpaid', this)">Unpaid (<%= unpaidCount %>)</button>
            <button class="tab-btn" onclick="showTab('booked', this)">Booked (<%= bookedCount %>)</button>
            <button class="tab-btn" onclick="showTab('late', this)">Late (<%= lateCount %>)</button>
            <button class="tab-btn" onclick="showTab('checkedin', this)">Checked In (<%= checkedInCount %>)</button>
            <button class="tab-btn" onclick="showTab('checkedout', this)">Checked Out (<%= checkedOutCount %>)</button>
        </div>

        <%!
            private String getAction(Booking b, String contextPath) {
                switch (b.getBookingStatus()) {
                    case UNPAID:
                        return "<a href='" + contextPath + "/counter/Receipt' class='action-link'>Collect Payment</a>"
                             + " | <a href='" + contextPath + "/counter/CancelBooking?id=" + b.getId()
                             + "' class='action-link-danger' onclick=\"return confirm('Cancel this booking?')\">Cancel</a>";
                    case BOOKED: case LATE:
                        return "<a href='" + contextPath + "/counter/CheckIn' class='action-link'>Check In</a>";
                    case CHECKED_IN:
                        return "<a href='" + contextPath + "/counter/CheckOut' class='action-link'>Check Out</a>";
                    case CHECKED_OUT:
                        return "<a href='" + contextPath + "/counter/AssignTask' class='action-link'>Assign Task</a>";
                    default: return "-";
                }
            }
        %>

        <%-- ALL TAB --%>
        <div id="tab-all" class="tab-content active">
            <div class="tab-table-container">
                <table class="tab-header-table">
                    <colgroup>
                        <col style="width:5%">
                        <col style="width:12%">
                        <col style="width:7%">
                        <col style="width:14%">
                        <col style="width:10%">
                        <col style="width:10%">
                        <col style="width:10%">
                        <col style="width:12%">
                        <col style="width:20%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Customer</th>
                            <th>Room</th>
                            <th>Room Type</th>
                            <th>Est. Check-in</th>
                            <th>Est. Check-out</th>
                            <th>Payment (RM)</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                </table>
                <div class="tab-body-scroll">
                    <table class="tab-body-table">
                        <colgroup>
                            <col style="width:5%">
                            <col style="width:12%">
                            <col style="width:7%">
                            <col style="width:14%">
                            <col style="width:10%">
                            <col style="width:10%">
                            <col style="width:10%">
                            <col style="width:12%">
                            <col style="width:20%">
                        </colgroup>
                        <tbody>
                            <% for (Booking b : bookings) {%>
                            <tr>
                                <td><%= b.getId()%></td>
                                <td><%= b.getCustomer().getName()%></td>
                                <td><%= b.getRoom().getRoomNumber()%></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName()%></td>
                                <td><%= b.getEstimatedCheckInTime().toLocalDate()%></td>
                                <td><%= b.getEstimatedCheckOutTime().toLocalDate()%></td>
                                <td>RM<%= String.format("%.2f", b.getPayment())%></td>
                                <td><%= b.getBookingStatus()%></td>
                                <td><% out.print(getAction(b, request.getContextPath())); %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <%-- UNPAID TAB --%>
        <div id="tab-unpaid" class="tab-content">
            <% if (unpaidCount == 0) { %><p>No unpaid bookings.</p><% } else { %>
            <div class="tab-table-container">
                <table class="tab-header-table">
                    <colgroup><col style="width:7%"><col style="width:13%"><col style="width:7%"><col style="width:15%"><col style="width:12%"><col style="width:12%"><col style="width:12%"><col style="width:22%"></colgroup>
                    <thead><tr><th>ID</th><th>Customer</th><th>Room</th><th>Room Type</th><th>Est. Check-in</th><th>Est. Check-out</th><th>Payment (RM)</th><th>Action</th></tr></thead>
                </table>
                <div class="tab-body-scroll">
                    <table class="tab-body-table">
                        <colgroup><col style="width:7%"><col style="width:13%"><col style="width:7%"><col style="width:15%"><col style="width:12%"><col style="width:12%"><col style="width:12%"><col style="width:22%"></colgroup>
                        <tbody>
                            <% for (Booking b : bookings) { if (b.getBookingStatus() != entity.Booking.BookingStatus.UNPAID) continue; %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getCustomer().getName() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                                <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/counter/Receipt" class="action-link">Collect Payment</a>
                                    &nbsp;|&nbsp;
                                    <a href="${pageContext.request.contextPath}/counter/CancelBooking?id=<%= b.getId() %>"
                                       class="action-link-danger"
                                       onclick="return confirm('Cancel booking for <%= b.getCustomer().getName() %>?')">Cancel</a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } %>
        </div>

        <%-- BOOKED TAB --%>
        <div id="tab-booked" class="tab-content">
            <% if (bookedCount == 0) { %><p>No booked bookings.</p><% } else { %>
            <div class="tab-table-container">
                <table class="tab-header-table">
                    <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                    <thead><tr><th>ID</th><th>Customer</th><th>Room</th><th>Room Type</th><th>Est. Check-in</th><th>Est. Check-out</th><th>Payment (RM)</th><th>Action</th></tr></thead>
                </table>
                <div class="tab-body-scroll">
                    <table class="tab-body-table">
                        <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                        <tbody>
                            <% for (Booking b : bookings) { if (b.getBookingStatus() != entity.Booking.BookingStatus.BOOKED) continue; %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getCustomer().getName() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                                <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                                <td><a href="${pageContext.request.contextPath}/counter/CheckIn" class="action-link">Check In</a></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } %>
        </div>

        <%-- LATE TAB --%>
        <div id="tab-late" class="tab-content">
            <% if (lateCount == 0) { %><p>No late bookings.</p><% } else { %>
            <div class="tab-table-container">
                <table class="tab-header-table">
                    <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                    <thead><tr><th>ID</th><th>Customer</th><th>Room</th><th>Room Type</th><th>Est. Check-in</th><th>Est. Check-out</th><th>Payment (RM)</th><th>Action</th></tr></thead>
                </table>
                <div class="tab-body-scroll">
                    <table class="tab-body-table">
                        <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                        <tbody>
                            <% for (Booking b : bookings) { if (b.getBookingStatus() != entity.Booking.BookingStatus.LATE) continue; %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getCustomer().getName() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                                <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                                <td><a href="${pageContext.request.contextPath}/counter/CheckIn" class="action-link">Check In</a></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } %>
        </div>

        <%-- CHECKED IN TAB --%>
        <div id="tab-checkedin" class="tab-content">
            <% if (checkedInCount == 0) { %><p>No checked-in bookings.</p><% } else { %>
            <div class="tab-table-container">
                <table class="tab-header-table">
                    <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                    <thead><tr><th>ID</th><th>Customer</th><th>Room</th><th>Room Type</th><th>Check-in Time</th><th>Est. Check-out</th><th>Payment (RM)</th><th>Action</th></tr></thead>
                </table>
                <div class="tab-body-scroll">
                    <table class="tab-body-table">
                        <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                        <tbody>
                            <% for (Booking b : bookings) { if (b.getBookingStatus() != entity.Booking.BookingStatus.CHECKED_IN) continue; %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getCustomer().getName() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getCheckInTime() != null ? b.getCheckInTime().toLocalDate() : "-" %></td>
                                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                                <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                                <td><a href="${pageContext.request.contextPath}/counter/CheckOut" class="action-link">Check Out</a></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } %>
        </div>

        <%-- CHECKED OUT TAB --%>
        <div id="tab-checkedout" class="tab-content">
            <% if (checkedOutCount == 0) { %><p>No checked-out bookings.</p><% } else { %>
            <div class="tab-table-container">
                <table class="tab-header-table">
                    <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                    <thead><tr><th>ID</th><th>Customer</th><th>Room</th><th>Room Type</th><th>Check-in</th><th>Check-out</th><th>Payment (RM)</th><th>Action</th></tr></thead>
                </table>
                <div class="tab-body-scroll">
                    <table class="tab-body-table">
                        <colgroup><col style="width:8%"><col style="width:15%"><col style="width:8%"><col style="width:17%"><col style="width:13%"><col style="width:13%"><col style="width:13%"><col style="width:13%"></colgroup>
                        <tbody>
                            <% for (Booking b : bookings) { if (b.getBookingStatus() != entity.Booking.BookingStatus.CHECKED_OUT) continue; %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getCustomer().getName() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getCheckInTime() != null ? b.getCheckInTime().toLocalDate() : "-" %></td>
                                <td><%= b.getCheckOutTime() != null ? b.getCheckOutTime().toLocalDate() : "-" %></td>
                                <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                                <td><a href="${pageContext.request.contextPath}/counter/AssignTask" class="action-link">Assign Task</a></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } %>
        </div>

        <% } %>
    </div>

    <script>
        function showTab(tab, btn) {
            document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
            document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));
            document.getElementById('tab-' + tab).classList.add('active');
            btn.classList.add('active');
        }
    </script>
</body>
</html>