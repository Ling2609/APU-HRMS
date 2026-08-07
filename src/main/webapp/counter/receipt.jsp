<%-- 
    Document   : receipt.
    Created on : Aug 4, 2026, 1:24:01 PM
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
    List<Booking> unpaidBookings = (List<Booking>) request.getAttribute("unpaidBookings");
    Booking paidBooking = (Booking) request.getAttribute("paidBooking");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Collect Payment & Receipt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    <style>
        @media print {
            .navbar, .no-print { display: none; }
            .receipt-box { box-shadow: none; border: 1px solid #333; }
        }
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
        <% if (paidBooking != null) { %>
            <%-- Receipt view --%>
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #b8860b; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Payment Receipt</div>
                <a href="${pageContext.request.contextPath}/counter/Receipt" class="breadcrumb-link no-print">← Back to Payment List</a>
            </div>

            <div class="receipt-box" style="margin: 0 auto;">
                <h3>APU HOTEL</h3>
                <p style="text-align:center; color:#666; font-size:13px; margin-bottom:15px;">Official Payment Receipt</p>
                <hr class="section-divider" style="margin: 10px 0;">

                <div class="receipt-row">
                    <span>Receipt No:</span>
                    <span>#<%= paidBooking.getId() %></span>
                </div>
                <div class="receipt-row">
                    <span>Date:</span>
                    <span><%= java.time.LocalDate.now() %></span>
                </div>

                <hr class="section-divider" style="margin: 10px 0;">
                <p style="font-weight:bold; color:#1a237e; margin-bottom:8px;">Customer Details</p>
                <div class="receipt-row">
                    <span>Name:</span>
                    <span><%= paidBooking.getCustomer().getName() %></span>
                </div>
                <div class="receipt-row">
                    <span>IC:</span>
                    <span><%= paidBooking.getCustomer().getIdentification() %></span>
                </div>
                <div class="receipt-row">
                    <span>Phone:</span>
                    <span><%= paidBooking.getCustomer().getPhone() %></span>
                </div>

                <hr class="section-divider" style="margin: 10px 0;">
                <p style="font-weight:bold; color:#1a237e; margin-bottom:8px;">Booking Details</p>
                <div class="receipt-row">
                    <span>Room Number:</span>
                    <span><%= paidBooking.getRoom().getRoomNumber() %></span>
                </div>
                <div class="receipt-row">
                    <span>Room Type:</span>
                    <span><%= paidBooking.getRoom().getRoomType().getRoomTypeName() %></span>
                </div>
                <div class="receipt-row">
                    <span>Rate per Night:</span>
                    <span>RM<%= String.format("%.2f", paidBooking.getRoom().getRoomType().getRoomTypePrice()) %></span>
                </div>
                <div class="receipt-row">
                    <span>Est. Check-in:</span>
                    <span><%= paidBooking.getEstimatedCheckInTime().toLocalDate() %></span>
                </div>
                <div class="receipt-row">
                    <span>Est. Check-out:</span>
                    <span><%= paidBooking.getEstimatedCheckOutTime().toLocalDate() %></span>
                </div>
                <div class="receipt-row">
                    <span>Duration:</span>
                    <span><%= java.time.temporal.ChronoUnit.DAYS.between(paidBooking.getEstimatedCheckInTime(), paidBooking.getEstimatedCheckOutTime()) %> night(s)</span>
                </div>
                <div class="receipt-row">
                    <span>Served By:</span>
                    <span><%= user.getName() %></span>
                </div>

                <hr class="section-divider" style="margin: 10px 0;">
                <div class="receipt-row receipt-total">
                    <span>TOTAL AMOUNT:</span>
                    <span>RM<%= String.format("%.2f", paidBooking.getPayment()) %></span>
                </div>
                <div class="receipt-row" style="color:#388e3c; font-weight:bold;">
                    <span>Status:</span>
                    <span>PAID ✓</span>
                </div>

                <hr class="section-divider" style="margin: 10px 0;">
                <p style="text-align:center; color:#666; font-size:12px;">Thank you for choosing APU Hotel!</p>
                </div>
                <%-- Print button outside receipt box --%>
                <div style="text-align:center; margin-top:20px;" class="no-print">
                    <p style="color:#388e3c; font-weight:bold; margin-bottom:15px;">
                        Payment collected successfully. This booking is now ready for check-in.
                    </p>

                    <button onclick="window.print()" class="btn btn-gold">
                        🖨️ Print Receipt
                    </button>

                    <a href="<%= request.getContextPath()%>/counter/CheckIn?id=<%= paidBooking.getId()%>"
                       class="btn btn-primary"
                       style="margin-left:10px; text-decoration:none;">
                        Proceed to Check In
                    </a>
                </div>

        <% } else { %>
            <%-- Unpaid bookings list --%>
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:3px solid #b8860b; padding-bottom:8px; margin-bottom:20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">
                    Collect Payment
                </div>

                <a href="<%= request.getContextPath() %>/counter/ViewBookings"
                   class="breadcrumb-link">
                    ← Back to Booking List
                </a>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>

            <% if (unpaidBookings == null || unpaidBookings.isEmpty()) { %>
                <p>No unpaid bookings.</p>
            <% } else { %>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Booking ID</th>
                                <th>Customer</th>
                                <th>Room</th>
                                <th>Room Type</th>
                                <th>Est. Check-in</th>
                                <th>Est. Check-out</th>
                                <th>Amount (RM)</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Booking b : unpaidBookings) { %>
                            <tr>
                                <td><%= b.getId() %></td>
                                <td><%= b.getCustomer().getName() %></td>
                                <td><%= b.getRoom().getRoomNumber() %></td>
                                <td><%= b.getRoom().getRoomType().getRoomTypeName() %></td>
                                <td><%= b.getEstimatedCheckInTime().toLocalDate() %></td>
                                <td><%= b.getEstimatedCheckOutTime().toLocalDate() %></td>
                                <td>RM<%= String.format("%.2f", b.getPayment()) %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/counter/Receipt?action=pay&id=<%= b.getId() %>"
                                       class="action-link"
                                       onclick="return confirm('Collect RM<%= String.format("%.2f", b.getPayment()) %> from <%= b.getCustomer().getName() %>?')">
                                        Collect Payment
                                    </a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        <% } %>
    </div>
</body>
</html>