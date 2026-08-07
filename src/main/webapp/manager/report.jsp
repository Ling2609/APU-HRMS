<%@page import="entity.Message"%>
<%@page import="entity.Room"%>
<%@page import="entity.Booking"%>
<%@page import="entity.RoomLog"%>
<%@page import="entity.SalaryLog"%>
<%@page import="entity.BookingLog"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.FinancialReport, entity.TransactionReport, entity.ArrivalDepartureReport, entity.CommentFeedbackReport, entity.RoomStatusReport, entity.Report, entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    Report report = (Report)request.getAttribute("viewingReport");
    
%>

<!DOCTYPE html>

<html>
    
<head>
    <title>Report - <%= report.getReportType() %> - <%= report.getGenerateTime() %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/report.css" />
    <style>
        @media print {
            .navbar, .no-print { display: none; }
            .receipt-box { box-shadow: none; border: 1px solid #333; }
        }
    </style>
</head>

<body>
    
    <jsp:include page="../manager/navbar.jsp"/>
    
    <div class="container">
        
        <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #c9a84c; padding-bottom: 8px; margin-bottom: 20px;">
            <div class="page-title" style="border:none; margin:0; padding:0;">View Report</div>
            <a href="${pageContext.request.contextPath}/manager/ManageReport" class="breadcrumb-link">← Manage Report</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="msg-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <div class="reportContainer">

            <div class="reportHeaderContainer" style="margin: 0 auto;">
                
                <h3>APU HOTEL</h3>
                <p style="text-align:center; color:#666; font-size:13px; margin-bottom:15px;"><%= report.getReportType().toString() %> REPORT</p>
                <hr class="section-divider" style="margin: 10px 0;">

                <div class="headerLine">
                    <span>Report ID:</span>
                    <span>#<%= report.getId().toString() %></span>
                </div>
                
                <div class="headerLine">
                    <span>Report Start Date:</span>
                    <span><%= report.getStartTime() %></span>
                </div>
                
                <div class="headerLine">
                    <span>Report End Date:</span>
                    <span><%= report.getEndTime()%></span>
                </div>
                
                <div class="headerLine">
                    <span>Report Generation Date:</span>
                    <span><%= report.getGenerateTime().toLocalDate()%></span>
                </div>
                
                <div class="headerLine">
                    <span>Report Generation Time:</span>
                    <span><%= report.getGenerateTime().toLocalTime()%></span>
                </div>
                
                <div class="headerLine">
                    <span>Report Type:</span>
                    <span><%= report.getReportType()%></span>
                </div>
                
            <div class="reportBodyContainer">
                
                <hr class="section-divider" style="margin: 10px 0;">
                <p style="font-weight:bold; color:#1a237e; margin-bottom:8px;">Report Details</p>
                    
                <% if(report.getReportType() == Report.ReportType.FINANCIAL) { %>
                
                    <div class="bodySection">

                        <table>
                            
                            <tr>
                                <th>No</th>
                                <th>Details</th>
                                <th>Expenses(RM)</th>
                            </tr>
                            
                            <% FinancialReport viewingReport = (FinancialReport)request.getAttribute("viewingReport"); %>
                            <% ArrayList<BookingLog> bookingLogs = viewingReport.getBookingsLogs(); %>
                            <% Double sum = 0.00; %>
                            <% Double total = 0.00; %>
                            <% for(BookingLog log : bookingLogs) { %>

                            <tr>
                                <td><span><%= bookingLogs.indexOf(log) %></span></td>
                                <td><span>Transaction from Booking #<%= log.getBookingID() %></span></td>
                                <td class="double"><span>+ <%= log.getPayment() %></span></td>
                                <% sum = sum + log.getPayment(); %>
                            </tr>

                            <% } %>

                            <tr>
                                <td></td>
                                <td class="sum">Sum</td>
                                <td class="double">+ <%= sum %></td>
                                <% total = total + sum; %>
                            </tr>
                            
                            <% ArrayList<SalaryLog> salaryLogs = viewingReport.getSalaryLogs(); %>
                            <% sum = 0.00; %>
                            <% for(SalaryLog log : salaryLogs) { %>

                            <tr>
                                <td><span><%= salaryLogs.indexOf(log) %></span></td>
                                <td><span>Salary of staff #<%= log.getUser().getId() %></span></td>
                                <td class="double"><span>- <%= log.getUserSalary() %></span></td>
                                <% sum = sum + log.getUserSalary(); %>
                            </tr>

                            <% } %>

                            <tr>
                                <td></td>
                                <td class="sum">Sum</td>
                                <td class="double">+ <%= sum %></td>
                                <% total = total - sum; %>
                            </tr>
                            
                            <tr>
                                <td></td>
                                <td class="sum">Total</td>
                                <td class="double">+ <%= total %></td>
                            </tr>
                            
                        </table>
                        
                    </div>
                    
                <% } %>
                
                <% if(report.getReportType() == Report.ReportType.TRANSACTION) { %>
                
                    <% TransactionReport viewingReport = (TransactionReport)request.getAttribute("viewingReport"); %>
                    <% ArrayList<BookingLog> bookingLogs = viewingReport.getBookingsLogs(); %>
                    <% Double sum = 0.00; %>
                    
                    <div class="bodySection">
                    
                        <table>
                            
                            <tr>
                                <th>No</th>
                                <th>Transaction</th>
                                <th>Profit(RM)</th>
                            </tr>
                            
                        <% for(BookingLog log : bookingLogs) { %>

                            <tr>
                                <td><span><%= bookingLogs.indexOf(log) %></span></td>
                                <td><span>Transaction from Booking #<%= log.getBookingID() %></span></td>
                                <td class="double"><span>+ <%= log.getPayment() %></span></td>
                                <% sum = sum + log.getPayment(); %>
                            </tr>

                        <% } %>

                            <tr>
                                <td></td>
                                <td class="sum">Sum</td>
                                <td class="double">+ <%= sum %></td>
                            </tr>
                        </table>
                        
                    </div>
                    
                <% } %> 
                
                <% if(report.getReportType() == Report.ReportType.ROOM_STATUS) { %>
                
                    <% RoomStatusReport viewingReport = (RoomStatusReport)request.getAttribute("viewingReport"); %>
                    <% ArrayList<RoomLog> roomLogs = viewingReport.getRoomLogs(); %>
                    
                    <div class="bodySection">
                    
                        <table>
                            
                            <tr>
                                <th>No</th>
                                <th>Room Number</th>
                                <th>Status</th>
                            </tr>
                            
                        <% for(RoomLog log : roomLogs) { %>

                            <tr>
                                <td><span><%= roomLogs.indexOf(log) %></span></td>
                                <td><span>Room #<%= log.getRoom().getId() %></span></td>
                                <td><span><%= log.getRoomStatus().toString() %></span></td>
                            </tr>

                        <% } %>

                        </table>
                        
                    </div>
                    
                <% } %>
                
                <% if(report.getReportType() == Report.ReportType.ARRIVAL_DEPARTURE) { %>
                
                    <% ArrivalDepartureReport viewingReport = (ArrivalDepartureReport)request.getAttribute("viewingReport"); %>
                    <% ArrayList<BookingLog> bookingLogs = viewingReport.getBookingsLogs(); %>
                    
                    <div class="bodySection">
                    
                        <table>
                            
                            <tr>
                                <th>No</th>
                                <th>Booking No.</th>
                                <th>Estimated Check In Time</th>
                                <th>Estimated Check Out Time</th>
                                <th>Actual Check In Time</th>
                                <th>Actual Check Out Time</th>
                            </tr>
                            
                        <% for(BookingLog log : bookingLogs) { %>

                            <tr>
                                <td><span><%= bookingLogs.indexOf(log) %></span></td>
                                <td><span>Booking #<%= log.getBookingID() %></span></td>
                                <td class="time"><span><%= log.getEstimatedCheckInTime() %></span></td>
                                <td class="time"><span><%= log.getEstimatedCheckOutTime() %></span></td>
                                <td class="time"><span><%= log.getCheckInTime() %></span></td>
                                <td class="time"><span><%= log.getCheckOutTime() %></span></td>
                            </tr>

                        <% } %>

                        </table>
                        
                    </div>
                    
                <% } %>
                
                <% if(report.getReportType() == Report.ReportType.COMMENT_FEEDBACK) { %>
                
                    <% CommentFeedbackReport viewingReport = (CommentFeedbackReport)request.getAttribute("viewingReport"); %>
                    <% ArrayList<BookingLog> bookingLogs = viewingReport.getBookingsLogs(); %>
                    
                    <div class="bodySection">
                    
                        <table>
                            
                            <tr>
                                <th>No</th>
                                <th>Booking No.</th>
                                <th>Message Type</th>
                                <th>Message</th>
                            </tr>
                            
                        <% for(BookingLog log : bookingLogs) { %>

                            <tr>
                                
                                <% for(Message msg : viewingReport.getMessages()) { %>
                                
                                    <% if(msg.getBookingUser().getBooking().getId() == log.getBookingID()) { %>
                                    
                                        <td><span><%= bookingLogs.indexOf(log) %></span></td>
                                        <td><span>Booking #<%= log.getBookingID() %></span></td>
                                        <td class="time"><span><%= msg.getMessageType() %></span></td>
                                        <td class="time"><span><%= msg.getMessageContent() %></span></td>
                                        
                                    <% } %>
                                
                                <% } %>
                                
                            </tr>

                        <% } %>

                        </table>
                        
                    </div>
                    
                <% } %>
                                    
                <%-- Print button outside receipt box --%>
                <div style="text-align:center; margin-top:20px;" class="no-print">
                    <button onclick="window.print()" class="btn btn-gold">🖨️ Print Receipt</button>
                </div>
                
            </div>

        </div>
                
    </div>
                
</body>

</html>