<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entity.User"%>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
        <title>Edit Staff</title>
    </head>
    
    <body>
        
        <jsp:include page="../manager/navbar.jsp"/>
        
        <div class="container">
            
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #c9a84c; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Register Staff</div>
                <a href="${pageContext.request.contextPath}/manager/ManageStaff" class="breadcrumb-link">← Manage Staff</a>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <div class="form-container">
                
                <form method="post" action="${pageContext.request.contextPath}/manager/RegisterStaff">
                                               
                    <table class="form-table">
                        <tr>
                            <td>Name:</td>
                            <td><input type="text" name="name" required /></td>
                        </tr>
                        <tr>
                            <td>Password:</td>
                            <td><input type="text" name="password" required /></td>
                        </tr>
                        <tr>
                            <td>Gender:</td>
                            <td>
                                <select name="gender">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>IC/Identification:</td>
                            <td><input type="text" name="identification" required placeholder="12 digits" /></td>
                        </tr>
                        <tr>
                            <td>Phone:</td>
                            <td><input type="text" name="phone" required placeholder="10-11 digits" /></td>
                        </tr>
                        <tr>
                            <td>Email:</td>
                            <td><input type="text" name="email" placeholder="example@email.com" /></td>
                        </tr>
                        <tr>
                            <td>Address:</td>
                            <td><textarea name="address"></textarea></td>
                        </tr>
                        <tr>
                            <td>Role: </td>
                            <td>
                                <select name="role">
                                    <% 
                                        for(User.Role role: User.Role.values()) { 
                                            
                                            if(role != User.Role.CUSTOMER) {
                                    %>
                                    <option value="<%= role%>"><%= role.toString()%></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                                
                            </td>
                        </tr>
                        <tr>
                            <td>Salary:</td>
                            <td><input type="text" name="salary" placeholder="0.00" /></td>
                        </tr>
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-primary" style="width:200px;">Register Staff</button>
                    </div>
                </form>
            </div>
                        
        </div>
        
    </body>
    
</html>
