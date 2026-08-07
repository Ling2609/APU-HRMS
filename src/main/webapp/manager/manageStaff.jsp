<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="entity.User, entity.Staff, java.util.ArrayList" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    ArrayList<Staff> staffList = (ArrayList<Staff>)request.getAttribute("staffList");
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Staff</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
    </head>
        
    <body>
        
        <jsp:include page="../manager/navbar.jsp"/>
        
        <div class="container">
            
            <div class="page-title">Manage Staff</div>
            <div><a href="${pageContext.request.contextPath}/manager/RegisterStaff" class="action-link">Register</a></div>
            
            <br>
            
            <% if (request.getAttribute("success") != null) { %>
            
            <div class="msg-success"><%= request.getAttribute("success") %></div>
            
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
            
                <div class="msg-error"><%= request.getAttribute("error") %></div>
                
            <% } %>
                        
            <% if (staffList.isEmpty()) { %>
            
                <p>No staff found.</p>
                
            <% } else { %>
            
                <div class="table-wrapper">
                    
                    <table class="data-table">
                        
                        <colgroup>
                            <col class="name">
                            <col class="identification">
                            <col class="phone"
                            <col class="email">
                            <col class="role">
                            <col class="action-col" colspan="2">
                        </colgroup>
                        
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Identification</th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th colspan="2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Staff staff : staffList) { %>
                            <tr>
                                <td><%= staff.getName() %></td>
                                <td><%= staff.getIdentification() %></td>
                                <td><%= staff.getPhone() %></td>
                                <td><%= staff.getEmail() %></td>
                                <td><%= staff.getRole().toString() %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/manager/EditStaff?action=select&staffID=<%= staff.getId()%>" class="action-link">Edit</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/manager/DeleteStaff?action=delete&staffID=<%= staff.getId()%>" class="action-link">Delete</a>
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
