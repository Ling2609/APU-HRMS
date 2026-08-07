package controller.manager;

import entity.Staff;
import entity.User;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import session.ManagerFacade;

@WebServlet(name = "RegisterStaff", urlPatterns = {"/manager/RegisterStaff"})
public class RegisterStaff extends HttpServlet {

    @EJB
    private ManagerFacade managerFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.MANAGER) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);        
        request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
        
        String name = request.getParameter("name").trim();
        String password = request.getParameter("password").trim();
        String gender = request.getParameter("gender");
        String identification = request.getParameter("identification").trim();
        String phone = request.getParameter("phone").trim();
        String email = request.getParameter("email").trim();
        String address = request.getParameter("address").trim();
        User.Role role = User.getRole(request.getParameter("role"));
        String salary = request.getParameter("salary").trim();

        if (name.isEmpty() || password.isEmpty() || identification.isEmpty() || phone.isEmpty() 
                || email.isEmpty() || address.isEmpty() || salary.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
            return;
        }

        // IC must be 12 digits numbers only
        if (!identification.matches("\\d{12}")) {
            request.setAttribute("error", "IC must be exactly 12 digits (numbers only).");
            request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
            return;
        }

        // Phone must be 10-11 digits
        if (!phone.matches("^[0-9]{10,11}$")) {
            request.setAttribute("error", "Phone must be 10-11 digits.");
            request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
            return;
        }

        // Email format
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            request.setAttribute("error", "Invalid email format.");
            request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
            return;
        }
        
        // Role
        if (role != User.Role.COUNTER_STAFF && role != User.Role.MANAGER && role != User.Role.HOUSEKEEPER) {
            request.setAttribute("error", "Invalid role.");
            request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
            return;
        }
        
        // Salary
        try {
            Double.valueOf(salary);
            
            if(Double.parseDouble(salary) <= 0) {
                request.setAttribute("error", "Invalid salary value.");
                request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
                return;
            }
        }
        catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid salary value.");
            request.getRequestDispatcher("/manager/registerStaff.jsp").forward(request, response);
            return;
        }

        Staff staff = new Staff();
        staff.setName(name);
        staff.setPassword(password);
        staff.setGender(gender);
        staff.setIdentification(identification);
        staff.setPhone(phone);
        staff.setEmail(email);
        staff.setAddress(address);
        staff.setRole(role);
        staff.setSalary(Double.parseDouble(salary));
        
        User user = new User(staff);
        managerFacade.create(user);

        request.setAttribute("success", "Staff created successfully.");
        request.getRequestDispatcher("/manager/ManageStaff").forward(request, response);
        
    }   

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
