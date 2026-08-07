<%@page import="entity.Report"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Staff, java.util.ArrayList" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    ArrayList<Report> reportList = (ArrayList<Report>)request.getAttribute("reportList");
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Reports</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    </head>
        
    <body>
        
        <jsp:include page="../manager/navbar.jsp"/>
            
        <div class="container">
            
            <div class="page-title">Create Reports</div>
            
            <br>
            
            <div class="form-container">
                
                <form method="post" action="${pageContext.request.contextPath}/manager/CreateReport">
                                               
                    <table class="form-table">
                        
                        <tr>
                            <td>Start Time:</td>
                            <td><input type="date" name="startTime" required /></td>
                        </tr>

                        <tr>
                            <td>End Time:</td>
                            <td><input type="date" name="endTime" required /></td>
                        </tr>

                        <tr>
                            <td>Report Type: </td>
                            <td>
                                <select name="reportType">
                                    <% for(Report.ReportType type : Report.ReportType.values()) { %>
                                    <option value="<%= type%>"><%= type.toString()%></option>
                                    <% } %>
                                </select>

                            </td>
                        </tr>
                        
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-primary" style="width:200px;">Create Report</button>
                    </div>
                </form>
                                
            </div>
                        
        </div>
                                            
        <div class="container">
            
            <div class="page-title">All Reports</div>
            
            <br>
            
            <% if (request.getAttribute("success") != null) { %>
            
            <div class="msg-success"><%= request.getAttribute("success") %></div>
            
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
            
                <div class="msg-error"><%= request.getAttribute("error") %></div>
                
            <% } %>
                        
            <% if (reportList.isEmpty()) { %>
            
                <p>No reports found.</p>
                
            <% } else { %>
            
                <div class="table-wrapper">
                    
                    <table class="data-table">
                        
                        <colgroup>
                            <col class="id">
                            <col class="reportType">
                            <col class="generateTime"
                            <col class="startTime">
                            <col class="endTime">
                            <col class="action-col" colspan=2>
                        </colgroup>
                        
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Report Type</th>
                                <th>Generate Time</th>
                                <th>Start Time</th>
                                <th>End Time</th>
                                <th colspan=2>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Report report : reportList) { %>
                            <tr>
                                <td><%= report.getId() %></td>
                                <td><%= report.getReportType().toString()%></td>
                                <td><%= report.getGenerateTime()%></td>
                                <td><%= report.getStartTime()%></td>
                                <td><%= report.getEndTime()%></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/manager/ViewReport?action=view&reportID=<%= report.getId()%>" class="action-link">View</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/manager/DeleteReport?action=delete&reportID=<%= report.getId()%>" class="action-link">Delete</a>
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
