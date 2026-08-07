<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entity.Staff, entity.User"%>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.MANAGER) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }
    
    Staff staff = (Staff) request.getAttribute("selectedStaff");
%>

<!DOCTYPE html>

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css" />
        <title>Edit Staff</title>
    </head>
    
    <body>
        
        <jsp:include page="/manager/navbar.jsp"/>
        
        <div class="container">
            
            <div style="display:flex; justify-content:space-between; align-items:center; border-bottom: 3px solid #c9a84c; padding-bottom: 8px; margin-bottom: 20px;">
                <div class="page-title" style="border:none; margin:0; padding:0;">Edit Staff</div>
                <a href="${pageContext.request.contextPath}/manager/ManageStaff" class="breadcrumb-link">← Manage Staff</a>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <div class="form-container">
                
                <form method="post" action="${pageContext.request.contextPath}/manager/EditStaff">
                    
                    <input type="hidden" name="id" value="<%= staff.getId() %>" >
                           
                    <table class="form-table">
                        <tr>
                            <td>Name:</td>
                            <td><input type="text" name="name" value="<%= staff.getName() %>" required /></td>
                        </tr>
                        <tr>
                            <td>Gender:</td>
                            <td>
                                <select name="gender">
                                    <option value="Male" <%= "Male".equals(staff.getGender()) ? "selected" : "" %>>Male</option>
                                    <option value="Female" <%= "Female".equals(staff.getGender()) ? "selected" : "" %>>Female</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>IC/Identification:</td>
                            <td><input type="text" name="identification" value="<%= staff.getIdentification() != null ? staff.getIdentification() : "" %>" required placeholder="12 digits" /></td>
                        </tr>
                        <tr>
                            <td>Phone:</td>
                            <td><input type="text" name="phone" value="<%= staff.getPhone() != null ? staff.getPhone() : "" %>" required placeholder="10-11 digits" /></td>
                        </tr>
                        <tr>
                            <td>Email:</td>
                            <td><input type="text" name="email" value="<%= staff.getEmail() != null ? staff.getEmail() : "" %>" placeholder="example@email.com" /></td>
                        </tr>
                        <tr>
                            <td>Address:</td>
                            <td><textarea name="address"><%= staff.getAddress() != null ? staff.getAddress() : "" %></textarea></td>
                        </tr>
                        <tr>
                            <td>Role: </td>
                            <td>
                                <select name="role">
                                    <% 
                                        for(User.Role role: User.Role.values()) { 
                                            
                                            if(role != User.Role.CUSTOMER) {
                                    %>
                                    <option value="<%= role%>" <%= role.equals(staff.getRole()) ? "selected" : "" %>><%= role.toString()%></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                                
                            </td>
                        </tr>
                        <tr>
                            <td>Salary:</td>
                            <td><input type="text" name="salary" value="<%= Double.toString(staff.getSalary()) != null ? Double.toString(staff.getSalary()): "" %>" placeholder="0.00" /></td>
                        </tr>
                    </table>
                    <div style="text-align:center; margin-top:20px;">
                        <button type="submit" class="btn btn-primary" style="width:200px;">Update Staff</button>
                    </div>
                </form>
            </div>
                        
        </div>
        
    </body>
    
</html>
